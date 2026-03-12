package com.amarDokan.amarDokan.service.implementations;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import com.amarDokan.amarDokan.models.Category;
import com.amarDokan.amarDokan.models.Product;
import com.amarDokan.amarDokan.repository.CategoryRepository;
import com.amarDokan.amarDokan.repository.ProductRepository;
import com.amarDokan.amarDokan.service.ProductService;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @org.springframework.beans.factory.annotation.Value("${image.upload.path:uploads/img}")
    private String uploadPath;

    @Override
    public Product saveProduct(Product product) {
        if (product.getCategory() == null || product.getCategory().getId() == null)
            return null;

        Category category = categoryRepository.findById(product.getCategory().getId()).orElse(null);
        if (ObjectUtils.isEmpty(category))
            return null;

        product.setCategory(category);
        return productRepository.save(product);
    }

    @Override
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @Override
    public Page<Product> getAllProductsPagination(Integer pageNo, Integer pageSize) {
        Pageable pageable = PageRequest.of(pageNo, pageSize);
        return productRepository.findAll(pageable);
    }

    @Override
    public Boolean deleteProduct(Long id) {
        Product product = productRepository.findById(id).orElse(null);

        if (!ObjectUtils.isEmpty(product)) {
            productRepository.delete(product);
            return true;
        }
        return false;
    }

    @Override
    public Product getProductById(Long id) {
        return productRepository.findById(id).orElse(null);
    }

    @Override
    public Product updateProduct(Product product, MultipartFile image) {
        Product dbProduct = getProductById(product.getId());

        if (ObjectUtils.isEmpty(dbProduct))
            return null;

        boolean hasImage = image != null && !image.isEmpty();
        String imageName = hasImage ? image.getOriginalFilename() : dbProduct.getImage();
        Category category = null;
        if (product.getCategory() != null && product.getCategory().getId() != null)
            category = categoryRepository.findById(product.getCategory().getId()).orElse(null);
        if (ObjectUtils.isEmpty(category))
            return null;

        dbProduct.setTitle(product.getTitle());
        dbProduct.setDescription(product.getDescription());
        dbProduct.setCategory(category);
        dbProduct.setPrice(product.getPrice());
        dbProduct.setStock(product.getStock());
        dbProduct.setImage(imageName);
        dbProduct.setIsActive(product.getIsActive());
        dbProduct.setDiscount(product.getDiscount());

        // Recalculating discount price
        Double discount = product.getPrice() * (product.getDiscount() / 100.0);
        dbProduct.setDiscountPrice(product.getPrice() - discount);

        Product updatedProduct = productRepository.save(dbProduct);

        if (!ObjectUtils.isEmpty(updatedProduct) && hasImage) {
            try {
                File saveFile = new File(uploadPath);
                if (!saveFile.exists()) 
                    saveFile.mkdirs();
                
                Path path = Paths.get(saveFile.getAbsolutePath() + File.separator
                        + "product_img" + File.separator + image.getOriginalFilename());
                
                File imgFolder = path.getParent().toFile();
                if (!imgFolder.exists()) 
                    imgFolder.mkdirs();
                
                Files.copy(image.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return updatedProduct;
    }

    @Override
    public List<Product> getAllActiveProducts(String categoryName) {
        if (ObjectUtils.isEmpty(categoryName))
            return productRepository.findByIsActiveTrue();

        // Finding the Category entity by name and filtering products
        Category category = categoryRepository.findAll()
                .stream()
                .filter(c -> c.getName().equalsIgnoreCase(categoryName))
                .findFirst()
                .orElse(null);

        if (!ObjectUtils.isEmpty(category))
            return productRepository.findByCategory(category);

        return productRepository.findByIsActiveTrue();
    }

    @Override
    public List<Product> searchProduct(String keyword) {
        return productRepository.findByTitleContainingIgnoreCaseOrCategoryNameContainingIgnoreCase(keyword, keyword);
    }

    @Override
    public Page<Product> searchProductPagination(Integer pageNo, Integer pageSize, String keyword) {
        Pageable pageable = PageRequest.of(pageNo, pageSize);
        return productRepository.findByTitleContainingIgnoreCaseOrCategoryNameContainingIgnoreCase(keyword, keyword,
                pageable);
    }

    @Override
    public Page<Product> getAllActiveProductPagination(Integer pageNo, Integer pageSize, String categoryName) {
        Pageable pageable = PageRequest.of(pageNo, pageSize);

        if (ObjectUtils.isEmpty(categoryName))
            return productRepository.findByIsActiveTrue(pageable);

        Category category = categoryRepository.findAll()
                .stream()
                .filter(c -> c.getName().equalsIgnoreCase(categoryName))
                .findFirst()
                .orElse(null);

        if (!ObjectUtils.isEmpty(category))
            return productRepository.findByCategory(category, pageable);

        return productRepository.findByIsActiveTrue(pageable);
    }

    @Override
    public Page<Product> searchActiveProductPagination(Integer pageNo, Integer pageSize, String category,
            String keyword) {
        Pageable pageable = PageRequest.of(pageNo, pageSize);
        return productRepository.searchActiveProducts(keyword, pageable);
    }

}

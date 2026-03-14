package com.amarDokan.amarDokan.service;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;
import com.amarDokan.amarDokan.models.Product;

public interface ProductService {

    public Product saveProduct(Product product);

    public List<Product> getAllProducts();

    public Boolean deleteProduct(Long id);

    public Product getProductById(Long id);

    public Product updateProduct(Product product, MultipartFile image);

    public List<Product> getAllActiveProducts(String category);

    public List<Product> searchProduct(String keyword);

    public Page<Product> getAllActiveProductPagination(Integer pageNo, Integer pageSize, String category);

    public Page<Product> searchProductPagination(Integer pageNo, Integer pageSize, String keyword);

    public Page<Product> getAllProductsPagination(Integer pageNo, Integer pageSize);

    public Page<Product> searchActiveProductPagination(Integer pageNo, Integer pageSize, String category,
            String keyword);

}

package com.amarDokan.amarDokan.controller.api;

import com.amarDokan.amarDokan.models.Product;
import com.amarDokan.amarDokan.models.Category;
import com.amarDokan.amarDokan.service.ProductService;
import com.amarDokan.amarDokan.exception.ErrorResponse;
import com.amarDokan.amarDokan.exception.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductRestController {

    private final ProductService productService;

    @Value("${image.upload.path:uploads/img}")
    private String uploadPath;

    public ProductRestController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public ResponseEntity<List<Product>> getAllProducts() {
        List<Product> products = productService.getAllProducts();
        return new ResponseEntity<>(products, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable Long id) {
        Product product = productService.getProductById(id);
        if (product == null) 
            throw new ResourceNotFoundException("Product not found with id: " + id);
        
        return new ResponseEntity<>(product, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<?> createProduct(@ModelAttribute Product product,
                                           @RequestParam("category.id") Long categoryId,
                                           @RequestParam("file") MultipartFile image) throws IOException {
        Category category = new Category();
        category.setId(categoryId);
        product.setCategory(category);

        String imageName = image.isEmpty() ? "default.jpg" : image.getOriginalFilename();
        product.setImage(imageName);
        product.setDiscount(0);
        product.setDiscountPrice(product.getPrice());
        
        Product savedProduct = productService.saveProduct(product);
        if (savedProduct == null) {
            ErrorResponse error = new ErrorResponse(
                    HttpStatus.BAD_REQUEST.value(),
                    "Invalid product data. Select a valid category and fill all required fields.",
                    LocalDateTime.now()
            );
            return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
        }

        if (savedProduct != null && !image.isEmpty()) {
            File saveFile = new File(uploadPath);
            if (!saveFile.exists()) 
                saveFile.mkdirs();
            

            Path path = Paths.get(saveFile.getAbsolutePath() + File.separator + "product_img" + File.separator + image.getOriginalFilename());

            File imgFolder = path.getParent().toFile();
            if (!imgFolder.exists()) 
                imgFolder.mkdirs();
            

            Files.copy(image.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
        }
        return new ResponseEntity<>(savedProduct, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateProduct(@PathVariable Long id,
                                           @ModelAttribute Product product,
                                           @RequestParam("category.id") Long categoryId,
                                           @RequestParam(value = "file", required = false) MultipartFile image) throws IOException {
        Product existingProduct = productService.getProductById(id);
        if (existingProduct == null) 
            throw new ResourceNotFoundException("Product not found with id: " + id);

        Category category = new Category();
        category.setId(categoryId);
        product.setCategory(category);
        product.setId(id);
        Product updatedProduct = productService.updateProduct(product, image);
        if (updatedProduct == null) {
            ErrorResponse error = new ErrorResponse(
                    HttpStatus.BAD_REQUEST.value(),
                    "Invalid product update request. Select a valid category and try again.",
                    LocalDateTime.now()
            );
            return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(updatedProduct, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        Boolean deleted = productService.deleteProduct(id);
        if (!deleted) 
            throw new ResourceNotFoundException("Product not found with id: " + id);
        
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}

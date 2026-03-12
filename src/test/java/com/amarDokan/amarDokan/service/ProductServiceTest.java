package com.amarDokan.amarDokan.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;

import com.amarDokan.amarDokan.models.Product;
import com.amarDokan.amarDokan.repository.CategoryRepository;
import com.amarDokan.amarDokan.repository.ProductRepository;
import com.amarDokan.amarDokan.service.implementations.ProductServiceImpl;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private ProductServiceImpl productService;

    private Product product;

    @BeforeEach
    void setUp() {
        product = new Product();
        product.setId(1L);
        product.setTitle("Test Product");
        product.setPrice(100.0);
        product.setDiscount(10);
        product.setIsActive(true);
    }

    // Product saving test

    @Test
    void saveProduct_Success() {
        when(productRepository.save(any(Product.class))).thenReturn(product);

        Product savedProduct = productService.saveProduct(new Product());

        assertNotNull(savedProduct);
        assertEquals("Test Product", savedProduct.getTitle());
        verify(productRepository, times(1)).save(any(Product.class));
    }

    // Product deleting test

    @Test
    void deleteProduct_Success() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        doNothing().when(productRepository).delete(product);

        Boolean result = productService.deleteProduct(1L);

        assertTrue(result);
        verify(productRepository, times(1)).delete(product);
    }

    // Trying to delete a product which is not in the database

    @Test
    void deleteProduct_NotFound() {
        when(productRepository.findById(1L)).thenReturn(Optional.empty());

        Boolean result = productService.deleteProduct(1L);

        assertFalse(result);
        verify(productRepository, never()).delete(any());
    }

    // Product updating test

    @Test
    void updateProduct_Calculation() {
        // Mock existing product in DB check
        Product dbProduct = new Product();
        dbProduct.setId(1L);
        dbProduct.setPrice(200.0);
        dbProduct.setDiscount(0);

        // Requested update
        Product requestProduct = new Product();
        requestProduct.setId(1L);
        requestProduct.setPrice(100.0); // New Price 100
        requestProduct.setDiscount(10); // 10% Discount

        when(productRepository.findById(1L)).thenReturn(Optional.of(dbProduct));
        when(productRepository.save(any(Product.class))).thenAnswer(i -> i.getArguments()[0]);

        MockMultipartFile emptyFile = new MockMultipartFile("image", new byte[0]);
        Product updatedProduct = productService.updateProduct(requestProduct, emptyFile);

        assertNotNull(updatedProduct);
        assertEquals(90.0, updatedProduct.getDiscountPrice()); // 100 - (100 * 0.1) = 90
        verify(productRepository, times(1)).save(any(Product.class));
    }

    // Trying to update a product which is not in the database

    @Test
    void updateProduct_NotFound() {
        when(productRepository.findById(1L)).thenReturn(Optional.empty());
        
        MockMultipartFile emptyFile = new MockMultipartFile("image", new byte[0]);
        Product result = productService.updateProduct(product, emptyFile);

        assertNull(result);
        verify(productRepository, never()).save(any());
    }

    // If user dont select a category then it checks if the system returns all active products or not

    @Test
    void getAllActiveProducts_NoCategory() {
        when(productRepository.findByIsActiveTrue()).thenReturn(List.of(product));

        List<Product> products = productService.getAllActiveProducts(null);

        assertEquals(1, products.size());
        assertTrue(products.get(0).getIsActive());
        verify(productRepository, times(1)).findByIsActiveTrue();
    }
}

package com.amarDokan.amarDokan.controller.api;

import com.amarDokan.amarDokan.models.Category;
import com.amarDokan.amarDokan.models.Product;
import com.amarDokan.amarDokan.repository.CategoryRepository;
import com.amarDokan.amarDokan.repository.ProductRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class ProductRestControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @BeforeEach
    void setup() {
        productRepository.deleteAll();
        categoryRepository.deleteAll();

        Category category = new Category();
        category.setName("Electronics");
        category.setIsActive(true);
        category.setImageName("electronics.jpg");

        Category savedCategory = categoryRepository.save(category);

        Product product = new Product();
        product.setTitle("Laptop");
        product.setPrice(50000.0);
        product.setStock(10);
        product.setCategory(savedCategory);
        product.setIsActive(true);

        productRepository.save(product);
    }

    @Test
    void testGetAllProducts() throws Exception {
        mockMvc.perform(get("/api/products"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void testGetProductById() throws Exception {
        Product product = productRepository.findAll().get(0);

        mockMvc.perform(get("/api/products/" + product.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.title").value("Laptop"));
    }

    @Test
    void testCreateProduct() throws Exception {
        Category category = categoryRepository.findAll().get(0);

        MockMultipartFile file = new MockMultipartFile(
                "file",
                "test.jpg",
                "image/jpeg",
                "image".getBytes()
        );

        mockMvc.perform(multipart("/api/products")
                        .file(file)
                        .param("title", "Phone")
                        .param("price", "20000")
                        .param("stock", "5")
                        .param("category.id", category.getId().toString())
                        .param("isActive", "true"))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void testUpdateProduct() throws Exception {
        Product product = productRepository.findAll().get(0);
        Category category = categoryRepository.findAll().get(0);

        MockMultipartFile file = new MockMultipartFile(
                "file",
                "updated.jpg",
                "image/jpeg",
                "image".getBytes()
        );

        mockMvc.perform(multipart("/api/products/" + product.getId())
                        .file(file)
                        .param("title", "Updated Laptop")
                        .param("price", "55000")
                        .param("stock", "15")
                        .param("category.id", category.getId().toString())
                        .param("isActive", "true"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void testDeleteProduct() throws Exception {
        Product product = productRepository.findAll().get(0);

        mockMvc.perform(delete("/api/products/" + product.getId()))
                .andExpect(status().isNoContent());
    }
}
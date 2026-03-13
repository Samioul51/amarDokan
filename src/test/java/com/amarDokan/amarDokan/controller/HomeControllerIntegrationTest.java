package com.amarDokan.amarDokan.controller;

import com.amarDokan.amarDokan.models.Category;
import com.amarDokan.amarDokan.models.Product;
import com.amarDokan.amarDokan.repository.CategoryRepository;
import com.amarDokan.amarDokan.repository.ProductRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class HomeControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ProductRepository productRepository;

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
    void testHomePageLoads() throws Exception {

        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("index"))
                .andExpect(model().attributeExists("products"))
                .andExpect(model().attributeExists("category"));
    }

    @Test
    void testProductsPage() throws Exception {

        mockMvc.perform(get("/products"))
                .andExpect(status().isOk())
                .andExpect(view().name("product"))
                .andExpect(model().attributeExists("products"));
    }

    @Test
    void testViewProduct() throws Exception {

        List<Product> products = productRepository.findAll();
        Long id = products.get(0).getId();

        mockMvc.perform(get("/product/" + id))
                .andExpect(status().isOk())
                .andExpect(view().name("view_product"))
                .andExpect(model().attributeExists("product"));
    }

}
package com.amarDokan.amarDokan.controller;

import com.amarDokan.amarDokan.models.Category;
import com.amarDokan.amarDokan.models.Product;
import com.amarDokan.amarDokan.models.User;
import com.amarDokan.amarDokan.repository.CategoryRepository;
import com.amarDokan.amarDokan.repository.ProductRepository;
import com.amarDokan.amarDokan.repository.UserRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class AdminControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setup() {
        productRepository.deleteAll();
        categoryRepository.deleteAll();
        userRepository.deleteAll();

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

        User admin = new User();
        admin.setName("Admin");
        admin.setEmail("admin@test.com");
        admin.setPassword("password");
        admin.setRole("ROLE_ADMIN");
        admin.setIsEnable(true);

        userRepository.save(admin);
    }

    @Test
    void testAdminIndexPage() throws Exception {
        mockMvc.perform(get("/admin/"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/index"));
    }

    @Test
    void testCategoryPage() throws Exception {
        mockMvc.perform(get("/admin/category"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/category"))
                .andExpect(model().attributeExists("categorys"));
    }

    @Test
    void testProductsPage() throws Exception {
        mockMvc.perform(get("/admin/products"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/products"))
                .andExpect(model().attributeExists("products"));
    }

    @Test
    void testUsersPage() throws Exception {
        mockMvc.perform(get("/admin/users?type=1"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/users"))
                .andExpect(model().attributeExists("users"));
    }

    @Test
    void testOrdersPage() throws Exception {
        mockMvc.perform(get("/admin/orders"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/orders"))
                .andExpect(model().attributeExists("orders"));
    }
}
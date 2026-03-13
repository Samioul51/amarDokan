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

import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class UserControllerIntegrationTest {

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

        User user = new User();
        user.setName("User");
        user.setEmail("user@test.com");
        user.setPassword("password");
        user.setRole("ROLE_USER");
        user.setIsEnable(true);

        userRepository.save(user);
    }

    @Test
    @WithMockUser(username = "user@test.com", roles = {"USER"})
    void testUserHomePage() throws Exception {
        mockMvc.perform(get("/user/"))
                .andExpect(status().isOk())
                .andExpect(view().name("user/home"));
    }

    @Test
    @WithMockUser(username = "user@test.com", roles = {"USER"})
    void testUserCartPage() throws Exception {
        mockMvc.perform(get("/user/cart"))
                .andExpect(status().isOk())
                .andExpect(view().name("user/cart"));
    }

    @Test
    @WithMockUser(username = "user@test.com", roles = {"USER"})
    void testUserOrdersPage() throws Exception {
        mockMvc.perform(get("/user/orders"))
                .andExpect(status().isOk())
                .andExpect(view().name("user/order"));
    }

    @Test
    @WithMockUser(username = "user@test.com", roles = {"USER"})
    void testUserMyOrdersPage() throws Exception {
        mockMvc.perform(get("/user/user-orders"))
                .andExpect(status().isOk())
                .andExpect(view().name("user/my_orders"));
    }

    @Test
    @WithMockUser(username = "user@test.com", roles = {"USER"})
    void testUserProfilePage() throws Exception {
        mockMvc.perform(get("/user/profile"))
                .andExpect(status().isOk())
                .andExpect(view().name("user/profile"));
    }

    @Test
    void testUserRegistration() throws Exception {
        MockMultipartFile file = new MockMultipartFile(
                "img",
                "profile.jpg",
                "image/jpeg",
                "image".getBytes()
        );

        mockMvc.perform(multipart("/saveUser")
                        .file(file)
                        .param("name", "Pritom Banik")
                        .param("email", "pritom@kuet.com.bd")
                        .param("password", "123456")
                        .param("mobileNumber", "01700000000")
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/register"));
    }
}
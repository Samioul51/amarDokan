package com.amarDokan.amarDokan.controller.api;

import com.amarDokan.amarDokan.models.Category;
import com.amarDokan.amarDokan.repository.CategoryRepository;

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
class CategoryRestControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CategoryRepository categoryRepository;

    @BeforeEach
    void setup() {
        categoryRepository.deleteAll();

        Category category = new Category();
        category.setName("Electronics");
        category.setIsActive(true);
        category.setImageName("electronics.jpg");

        categoryRepository.save(category);
    }

    @Test
    void testGetAllCategories() throws Exception {
        mockMvc.perform(get("/api/categories"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void testGetCategoryById() throws Exception {
        Category category = categoryRepository.findAll().get(0);

        mockMvc.perform(get("/api/categories/" + category.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name").value("Electronics"));
    }

    @Test
    void testCreateCategory() throws Exception {
        MockMultipartFile file = new MockMultipartFile(
                "file",
                "test.jpg",
                "image/jpeg",
                "image".getBytes()
        );

        mockMvc.perform(multipart("/api/categories")
                        .file(file)
                        .param("name", "Books")
                        .param("isActive", "true"))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void testUpdateCategory() throws Exception {
        Category category = categoryRepository.findAll().get(0);

        String updateJson = """
                {
                    "name": "Updated Electronics",
                    "isActive": true,
                    "imageName": "electronics.jpg"
                }
                """;

        mockMvc.perform(put("/api/categories/" + category.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updateJson))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void testDeleteCategory() throws Exception {
        Category category = categoryRepository.findAll().get(0);

        mockMvc.perform(delete("/api/categories/" + category.getId()))
                .andExpect(status().isNoContent());
    }
}
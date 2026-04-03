package com.amarDokan.amarDokan.controller.api;

import com.amarDokan.amarDokan.models.User;
import com.amarDokan.amarDokan.repository.UserRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class UserRestControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setup() {
        userRepository.deleteAll();

        User user = new User();
        user.setName("Test User");
        user.setEmail("test@example.com");
        user.setPassword("password");
        user.setRole("ROLE_USER");
        user.setIsEnable(true);

        userRepository.save(user);
    }

    @Test
    void testGetAllUsers() throws Exception {
        mockMvc.perform(get("/api/users"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void testGetUserByEmail() throws Exception {
        mockMvc.perform(get("/api/users/test@example.com"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.email").value("test@example.com"));
    }

    @Test
    void testUpdateAccountStatus() throws Exception {
        User user = userRepository.findAll().get(0);

        mockMvc.perform(put("/api/users/" + user.getId() + "/status?status=false"))
                .andExpect(status().isOk());
    }

    @Test
    void testCreateUser() throws Exception {
        String requestBody = """
        {
          "name": "New User",
          "mobileNumber": "01700000000",
          "email": "new@example.com",
          "address": "Dhaka",
          "city": "Dhaka",
          "state": "Dhaka",
          "pincode": "1207",
          "password": "123456"
        }
        """;

        mockMvc.perform(post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.email").value("new@example.com"))
                .andExpect(jsonPath("$.password").doesNotExist());
    }

}

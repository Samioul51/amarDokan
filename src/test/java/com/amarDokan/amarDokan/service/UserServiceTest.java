package com.amarDokan.amarDokan.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.amarDokan.amarDokan.models.User;
import com.amarDokan.amarDokan.repository.UserRepository;
import com.amarDokan.amarDokan.service.implementations.UserServiceImpl;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServiceImpl userService;

    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setEmail("test@example.com");
        user.setPassword("password123");
        user.setFailedAttempt(0);
        user.setAccountNonLocked(true);
        user.setIsEnable(true);
    }

    // Test for saving regular user with encoded password and ROLE_USER

    @Test
    void saveUser_Details() {
        when(passwordEncoder.encode("password123")).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenAnswer(i -> i.getArguments()[0]);

        User savedUser = userService.saveUser(user);

        assertNotNull(savedUser);
        assertEquals("ROLE_USER", savedUser.getRole());
        assertEquals("encodedPassword", savedUser.getPassword());
        assertTrue(savedUser.getIsEnable());
        verify(userRepository).save(any(User.class));
    }

    // Test for saving admin with ROLE_ADMIN
    
    @Test
    void saveAdmin_Details() {
        when(passwordEncoder.encode("password123")).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenAnswer(i -> i.getArguments()[0]);

        User savedAdmin = userService.saveAdmin(user);

        assertNotNull(savedAdmin);
        assertEquals("ROLE_ADMIN", savedAdmin.getRole());
        assertEquals("encodedPassword", savedAdmin.getPassword());
        verify(userRepository).save(any(User.class));
    }

    // Test for enabling and disabling user account

    @Test
    void updateAccountStatus_Success() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        Boolean result = userService.updateAccountStatus(1L, false);

        assertTrue(result);
        assertFalse(user.getIsEnable());
        verify(userRepository).save(user);
    }

    // Test for incrementing failed login attempts

    @Test
    void increaseFailedAttempt_Increment() {
        userService.increaseFailedAttempt(user);

        assertEquals(1, user.getFailedAttempt());
        verify(userRepository).save(user);
    }

    // Test for locking user account after too many failed attempts

    @Test
    void userAccountLock_Timestamp() {
        userService.userAccountLock(user);

        assertFalse(user.getAccountNonLocked());
        assertNotNull(user.getLockTime());
        verify(userRepository).save(user);
    }

    // Test for unlocking account after lock duration has expired (15 mins) 

    @Test
    void unlockAccount_Expired() {
        user.setAccountNonLocked(false);
        user.setLockTime(LocalDateTime.now().minusMinutes(30)); 

        boolean result = userService.unlockAccountTimeExpired(user);

        assertTrue(result);
        assertTrue(user.getAccountNonLocked());
        assertEquals(0, user.getFailedAttempt());
        assertNull(user.getLockTime());
        verify(userRepository).save(user);
    }

    // Test for keeping account locked if duration has not expired

    @Test
    void unlockAccount_NotExpired() {
        user.setAccountNonLocked(false);
        user.setLockTime(LocalDateTime.now().minusMinutes(5)); 

        boolean result = userService.unlockAccountTimeExpired(user);

        assertFalse(result);
        assertFalse(user.getAccountNonLocked());
        verify(userRepository, never()).save(user);
    }

    // Test for checking if an email already exists in the database

    @Test
    void existsEmail_Check() {
        when(userRepository.existsByEmail("test@example.com")).thenReturn(true);

        Boolean exists = userService.existsEmail("test@example.com");

        assertTrue(exists);
        verify(userRepository).existsByEmail("test@example.com");
    }
}

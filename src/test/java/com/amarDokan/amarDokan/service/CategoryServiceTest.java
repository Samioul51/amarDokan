package com.amarDokan.amarDokan.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.amarDokan.amarDokan.models.Category;
import com.amarDokan.amarDokan.repository.CategoryRepository;
import com.amarDokan.amarDokan.service.implementations.CategoryServiceImpl;

@ExtendWith(MockitoExtension.class)
public class CategoryServiceTest {

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private CategoryServiceImpl categoryService;

    private Category category;

    @BeforeEach
    void setUp() {
        category = new Category();
        category.setId(1L);
        category.setName("Electronics");
        category.setIsActive(true);
    }

    // Test for saving a category

    @Test
    void saveCategory_Success() {
        when(categoryRepository.save(any(Category.class))).thenReturn(category);

        Category savedCategory = categoryService.saveCategory(new Category());

        assertNotNull(savedCategory);
        assertEquals("Electronics", savedCategory.getName());
        verify(categoryRepository).save(any(Category.class));
    }

    // Test for checking if a category exists by name

    @Test
    void existCategory_ReturnsTrue() {
        when(categoryRepository.existsByName("Electronics")).thenReturn(true);

        Boolean result = categoryService.existCategory("Electronics");

        assertTrue(result);
        verify(categoryRepository).existsByName("Electronics");
    }

    // Test for deleting a category successfully

    @Test
    void deleteCategory_Success() {
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        doNothing().when(categoryRepository).delete(category);

        Boolean result = categoryService.deleteCategory(1L);

        assertTrue(result);
        verify(categoryRepository).delete(category);
    }

    // Test for trying to delete a category that doesnt exist

    @Test
    void deleteCategory_NotFound() {
        when(categoryRepository.findById(1L)).thenReturn(Optional.empty());

        Boolean result = categoryService.deleteCategory(1L);

        assertFalse(result);
        verify(categoryRepository, never()).delete(any());
    }

    // Test for retrieving all active categories
    
    @Test
    void getAllActiveCategory_Success() {
        when(categoryRepository.findByIsActiveTrue()).thenReturn(List.of(category));

        List<Category> categories = categoryService.getAllActiveCategory();

        assertFalse(categories.isEmpty());
        assertEquals(1, categories.size());
        assertTrue(categories.get(0).getIsActive());
        verify(categoryRepository).findByIsActiveTrue();
    }
}

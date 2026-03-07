package com.amarDokan.amarDokan.service;

import java.util.List;

import org.springframework.data.domain.Page;

import com.amarDokan.amarDokan.models.Category;

public interface CategoryService {

    public Category saveCategory(Category category);

    public Boolean existCategory(String name);

    public List<Category> getAllCategory();

    public Boolean deleteCategory(Long id);

    public Category getCategoryById(Long id);

    public List<Category> getAllActiveCategory();

    public Page<Category> getAllCategorPagination(Integer pageNo, Integer pageSize);

}

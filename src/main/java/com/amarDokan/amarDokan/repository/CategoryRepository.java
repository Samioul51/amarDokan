package com.amarDokan.amarDokan.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.amarDokan.amarDokan.models.Category;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    public Boolean existsByName(String name);

    public List<Category> findByIsActiveTrue();

}

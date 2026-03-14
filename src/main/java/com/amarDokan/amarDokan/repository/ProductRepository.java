package com.amarDokan.amarDokan.repository;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.amarDokan.amarDokan.models.Product;
import com.amarDokan.amarDokan.models.Category;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    List<Product> findByIsActiveTrue();

    Page<Product> findByIsActiveTrue(Pageable pageable);

    List<Product> findByCategory(Category category);

    Page<Product> findByCategory(Category category, Pageable pageable);

    List<Product> findByTitleContainingIgnoreCaseOrCategoryNameContainingIgnoreCase(String title, String categoryName);

    Page<Product> findByTitleContainingIgnoreCaseOrCategoryNameContainingIgnoreCase(String title, String categoryName,
            Pageable pageable);

    // For searching active products by keyword in both product title and category name

    @Query("SELECT p FROM Product p WHERE p.isActive = true AND (lower(p.title) LIKE lower(concat('%', :keyword, '%')) OR lower(p.category.name) LIKE lower(concat('%', :keyword, '%')))")
    Page<Product> searchActiveProducts(@Param("keyword") String keyword, Pageable pageable);
}

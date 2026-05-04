package com.app.productbackend.repository;

import com.app.productbackend.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {

    // 🔍 SEARCH
    List<Product> findByNameContainingIgnoreCase(String name);

    // 📦 FILTER
    List<Product> findByCategoryIgnoreCase(String category);

    // 🔥 SEARCH + FILTER (NON-PAGED)
    List<Product> findByNameContainingIgnoreCaseAndCategoryIgnoreCase(String name, String category);

    // 🔥 PAGINATION + FILTER
    Page<Product> findByNameContainingIgnoreCaseAndCategoryContainingIgnoreCase(
            String name,
            String category,
            Pageable pageable
    );
}
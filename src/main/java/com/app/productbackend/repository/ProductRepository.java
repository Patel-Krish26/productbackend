package com.app.productbackend.repository;

import com.app.productbackend.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Integer> {

    // =========================
    // 🔍 SEARCH
    // =========================
    List<Product> findByNameContainingIgnoreCase(String name);

    // =========================
    // 📦 CATEGORY
    // =========================
    List<Product> findByCategoryIgnoreCase(String category);

    // =========================
    // 🔥 SEARCH + CATEGORY (NON-PAGED)
    // =========================
    List<Product> findByNameContainingIgnoreCaseAndCategoryIgnoreCase(
            String name,
            String category
    );

    // =========================
    // 🔥 PAGINATION (SEARCH)
    // =========================
    Page<Product> findByNameContainingIgnoreCase(
            String name,
            Pageable pageable
    );

    // =========================
    // 🔥 PAGINATION (CATEGORY)
    // =========================
    Page<Product> findByCategoryIgnoreCase(
            String category,
            Pageable pageable
    );

    // =========================
    // 🔥 PAGINATION (SEARCH + CATEGORY)
    // =========================
    Page<Product> findByNameContainingIgnoreCaseAndCategoryIgnoreCase(
            String name,
            String category,
            Pageable pageable
    );

    // =====================================================
    // ✅ FIX: FORCE LOAD IMAGES (USED IN ORDERS)
    // =====================================================
    @Query("SELECT p FROM Product p LEFT JOIN FETCH p.images WHERE p.id = :id")
    Optional<Product> findByIdWithImages(@Param("id") int id);
}
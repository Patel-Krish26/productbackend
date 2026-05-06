package com.app.productbackend.repository;

import com.app.productbackend.entity.ProductImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ImageRepository extends JpaRepository<ProductImage, Integer> {

    void deleteByProductId(int productId);

    List<ProductImage> findByProductId(int productId);
}
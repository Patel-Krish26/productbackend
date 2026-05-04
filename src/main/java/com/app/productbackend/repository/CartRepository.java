package com.app.productbackend.repository;

import com.app.productbackend.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CartRepository extends JpaRepository<Cart, Integer> {

    List<Cart> findByUserId(int userId);

    Cart findByUserIdAndProductId(int userId, int productId);

    void deleteByUserIdAndProductId(int userId, int productId);
}
package com.app.productbackend.repository;

import com.app.productbackend.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CartRepository extends JpaRepository<Cart, Integer> {

    Cart findByUser_IdAndProduct_Id(int userId, int productId);

    List<Cart> findByUser_Id(int userId);
}
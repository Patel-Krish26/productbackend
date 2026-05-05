package com.app.productbackend.repository;

import com.app.productbackend.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Integer> {

    // USER ORDER HISTORY
    List<Order> findByUserId(int userId);

    // ADMIN VIEW ALL = findAll() already
}
package com.app.productbackend.controller;

import com.app.productbackend.entity.Order;
import com.app.productbackend.service.OrderService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    // =========================
    // 🧾 PLACE ORDER (USER)
    // =========================
    @PostMapping("/place")
    public Order placeOrder(@RequestParam int userId) {
        return orderService.placeOrder(userId);
    }

    // =========================
    // 📦 USER ORDER HISTORY
    // =========================
@GetMapping("/user/{userId}")
public List<Order> getUserOrders(@PathVariable int userId) {
        return orderService.getUserOrders(userId);
    }

    // =========================
    // 🔥 ADMIN: ALL ORDERS
    // =========================
    @GetMapping
    public List<Order> getAllOrders() {
        return orderService.getAllOrders();
    }

    // =========================
    // 🔄 UPDATE STATUS (ADMIN)
    // =========================
    @PutMapping("/{orderId}/status")
    public Order updateStatus(
            @PathVariable int orderId,
            @RequestParam String status
    ) {
        return orderService.updateStatus(orderId, status);
    }
}
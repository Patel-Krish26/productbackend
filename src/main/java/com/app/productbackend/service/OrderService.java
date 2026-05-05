package com.app.productbackend.service;

import com.app.productbackend.entity.*;
import com.app.productbackend.repository.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class OrderService {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ProductRepository productRepository;

    // =========================
    // 🧾 PLACE ORDER
    // =========================
    public Order placeOrder(int userId) {

        List<Cart> cartItems = cartRepository.findByUserId(userId);

        if (cartItems.isEmpty()) {
            throw new RuntimeException("Cart is empty");
        }

        Order order = new Order();
        order.setUserId(userId);
        order.setStatus("PENDING");

        List<OrderItem> items = new ArrayList<>();
        double total = 0;

        for (Cart cart : cartItems) {

            Product p = cart.getProduct();

            OrderItem item = new OrderItem();
            item.setProductId(p.getId());
            item.setProductName(p.getName());
            item.setPrice(p.getPrice());
            item.setQuantity(cart.getQuantity());
            item.setOrder(order);

            total += p.getPrice() * cart.getQuantity();

            items.add(item);

            // optional stock reduce
            p.setStock(p.getStock() - cart.getQuantity());
            productRepository.save(p);
        }

        order.setItems(items);
        order.setTotalAmount(total);

        Order savedOrder = orderRepository.save(order);

        // 🧹 CLEAR CART AFTER ORDER
        cartRepository.deleteAll(cartItems);

        return savedOrder;
    }

    // =========================
    // 📦 USER ORDER HISTORY
    // =========================
    public List<Order> getUserOrders(int userId) {
        return orderRepository.findByUserId(userId);
    }

    // =========================
    // 🔥 ADMIN: ALL ORDERS
    // =========================
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    // =========================
    // 🔄 UPDATE ORDER STATUS (ADMIN)
    // =========================
    public Order updateStatus(int orderId, String status) {

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        order.setStatus(status);

        return orderRepository.save(order);
    }
}
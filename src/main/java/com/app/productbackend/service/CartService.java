package com.app.productbackend.service;

import com.app.productbackend.repository.CartRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Map;

@Service
public class CartService {

    private final CartRepository repo;

    public CartService(CartRepository repo) {
        this.repo = repo;
    }

    public void add(int userId, int productId) {
        repo.addToCart(userId, productId, 1);
    }

    public List<Map<String, Object>> get(int userId) {
        return repo.getCart(userId);
    }

    public void remove(int userId, int productId) {
        repo.remove(userId, productId);
    }
}
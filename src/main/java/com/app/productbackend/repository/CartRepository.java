package com.app.productbackend.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public class CartRepository {

    private final JdbcTemplate jdbc;

    public CartRepository(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    public void addToCart(int userId, int productId, int qty) {
        jdbc.update("EXEC sp_cart ?, ?, ?", userId, productId, qty);
    }

    public List<Map<String, Object>> getCart(int userId) {
        return jdbc.queryForList("EXEC sp_cart_get ?", userId);
    }

    public void remove(int userId, int productId) {
        jdbc.update("EXEC sp_cart_remove ?, ?", userId, productId);
    }
}
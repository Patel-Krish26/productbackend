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

    // ➕ ADD TO CART
    public void addToCart(int userId, int productId, int qty) {
        jdbc.update("EXEC sp_cart @action='ADD', @userId=?, @productId=?, @qty=?",
                userId, productId, qty);
    }

    // 📦 GET CART
    public List<Map<String, Object>> getCart(int userId) {
        return jdbc.queryForList(
                "EXEC sp_cart @action='GET', @userId=?",
                userId
        );
    }

    // ❌ REMOVE
    public void remove(int userId, int productId) {
        jdbc.update("EXEC sp_cart @action='REMOVE', @userId=?, @productId=?",
                userId, productId);
    }
}
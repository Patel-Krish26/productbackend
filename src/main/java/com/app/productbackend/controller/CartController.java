package com.app.productbackend.controller;

import com.app.productbackend.dto.CartResponse;
import com.app.productbackend.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cart")
@CrossOrigin("*")
public class CartController {

    @Autowired
    private CartService cartService;

    // =========================
    // ➕ ADD TO CART
    // =========================
    @PostMapping("/add/{productId}")
    public void addToCart(@PathVariable int productId,
                          @RequestParam int userId) {
        cartService.addToCart(userId, productId);
    }

    // =========================
    // 📦 GET CART
    // =========================
    @GetMapping("/{userId}")
    public List<CartResponse> getCart(@PathVariable int userId) {
        return cartService.getCart(userId);
    }

    // =========================
    // ❌ REMOVE ITEM
    // =========================
    @DeleteMapping("/remove")
    public void remove(@RequestParam int userId,
                       @RequestParam int productId) {
        cartService.removeFromCart(userId, productId);
    }

    @DeleteMapping("/clear/{userId}")
public void clearCart(@PathVariable int userId) {
    cartService.clearCart(userId);
}

    // =========================
    // 🔥 UPDATE QUANTITY (NEW)
    // =========================
    @PutMapping("/update")
    public void updateQty(@RequestParam int userId,
                        @RequestParam int productId,
                        @RequestParam int quantity) {
        cartService.updateQuantity(userId, productId, quantity);
    }
}
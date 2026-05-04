package com.app.productbackend.controller;

import com.app.productbackend.entity.Cart;
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

    // ✅ ADD TO CART
    @PostMapping("/add")
    public Cart addToCart(@RequestParam int userId,
                          @RequestParam int productId) {
        return cartService.addToCart(userId, productId);
    }

    // ✅ GET CART
    @GetMapping("/{userId}")
    public List<Cart> getCart(@PathVariable int userId) {
        return cartService.getCart(userId);
    }

    // ✅ REMOVE
    @DeleteMapping("/remove")
    public String remove(@RequestParam int userId,
                         @RequestParam int productId) {
        cartService.removeFromCart(userId, productId);
        return "Removed";
    }
}
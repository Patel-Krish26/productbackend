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

    @PostMapping("/add/{productId}")
    public void addToCart(@PathVariable int productId,
                          @RequestParam int userId) {
        cartService.addToCart(userId, productId);
    }

    @GetMapping("/{userId}")
    public List<CartResponse> getCart(@PathVariable int userId) {
        return cartService.getCart(userId);
    }

    @DeleteMapping("/remove")
    public void remove(@RequestParam int userId,
                       @RequestParam int productId) {
        cartService.removeFromCart(userId, productId);
    }
}
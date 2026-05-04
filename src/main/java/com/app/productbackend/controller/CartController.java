package com.app.productbackend.controller;

import com.app.productbackend.service.CartService;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/cart")
@CrossOrigin
public class CartController {

    private final CartService service;

    public CartController(CartService service) {
        this.service = service;
    }

    // ➕ ADD
    @PostMapping("/add/{productId}")
    public String add(@PathVariable int productId,
                      @RequestHeader("userId") int userId) {

        service.add(userId, productId);
        return "Added";
    }

    // 📦 GET
    @GetMapping
    public List<Map<String, Object>> get(
            @RequestHeader("userId") int userId) {

        return service.get(userId);
    }

    // ❌ REMOVE
    @DeleteMapping("/{productId}")
    public String remove(@PathVariable int productId,
                         @RequestHeader("userId") int userId) {

        service.remove(userId, productId);
        return "Removed";
    }
}
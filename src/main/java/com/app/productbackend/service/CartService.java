package com.app.productbackend.service;

import com.app.productbackend.dto.CartResponse;
import com.app.productbackend.entity.Cart;
import com.app.productbackend.entity.Product;
import com.app.productbackend.entity.User;
import com.app.productbackend.repository.CartRepository;
import com.app.productbackend.repository.ProductRepository;
import com.app.productbackend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CartService {

    @Autowired
    private CartRepository cartRepo;

    @Autowired
    private ProductRepository productRepo;

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private ProductRepository productRepository;

    // ✅ ADD TO CART
    public Cart addToCart(int userId, int productId) {

        User user = userRepo.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Product product = productRepo.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        // 🔥 FIXED QUERY
        Cart existing = cartRepo.findByUserIdAndProductId(userId, productId);

        if (existing != null) {
            existing.setQuantity(existing.getQuantity() + 1);
            return cartRepo.save(existing);
        }

        Cart cart = new Cart();
        cart.setUser(user);
        cart.setProduct(product);
        cart.setQuantity(1);

        return cartRepo.save(cart);
    }

    // ✅ GET CART
public List<CartResponse> getCart(int userId) {

    List<Cart> cartList = cartRepository.findByUserId(userId);

    return cartList.stream().map(cart -> {

        Product p = cart.getProduct(); // ✅ FIXED

        CartResponse res = new CartResponse();

        res.setProductId(p.getId());
        res.setQuantity(cart.getQuantity());

        if (p != null) {
            res.setProductName(p.getName());
            res.setPrice(p.getPrice());

            if (p.getImages() != null && !p.getImages().isEmpty()) {
                res.setImage(p.getImages().get(0).getImageUrl());
            }
        }

        return res;

    }).collect(Collectors.toList()); // ✅ FIXED
}

    // ✅ REMOVE
    public void removeFromCart(int userId, int productId) {
        Cart cart = cartRepo.findByUserIdAndProductId(userId, productId);
        if (cart != null) {
            cartRepo.delete(cart);
        }
    }
}
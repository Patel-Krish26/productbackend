package com.app.productbackend.service;

import com.app.productbackend.entity.Cart;
import com.app.productbackend.entity.Product;
import com.app.productbackend.entity.User;
import com.app.productbackend.repository.CartRepository;
import com.app.productbackend.repository.ProductRepository;
import com.app.productbackend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CartService {

    @Autowired
    private CartRepository cartRepo;

    @Autowired
    private ProductRepository productRepo;

    @Autowired
    private UserRepository userRepo;

    // ✅ ADD TO CART
    public Cart addToCart(int userId, int productId) {

        User user = userRepo.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Product product = productRepo.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        // 🔥 FIXED QUERY
        Cart existing = cartRepo.findByUser_IdAndProduct_Id(userId, productId);

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
    public List<Cart> getCart(int userId) {
        return cartRepo.findByUser_Id(userId);
    }

    // ✅ REMOVE
    public void removeFromCart(int userId, int productId) {
        Cart cart = cartRepo.findByUser_IdAndProduct_Id(userId, productId);
        if (cart != null) {
            cartRepo.delete(cart);
        }
    }
}
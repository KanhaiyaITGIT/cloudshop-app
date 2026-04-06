package com.shopeasy.service;

import com.shopeasy.exception.ResourceNotFoundException;
import com.shopeasy.model.*;
import com.shopeasy.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CartService {

    private final CartItemRepository cartItemRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    public List<CartItem> getCart(String email) {
        User user = getUser(email);
        return cartItemRepository.findByUser(user);
    }

    public CartItem addToCart(String email, Long productId, Integer quantity) {
        User user = getUser(email);
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException(
                    "Product not found: " + productId));

        if (product.getStock() < quantity) {
            throw new RuntimeException("Insufficient stock");
        }

        // Already in cart? Quantity badhao
        return cartItemRepository.findByUserAndProduct(user, product)
                .map(item -> {
                    item.setQuantity(item.getQuantity() + quantity);
                    return cartItemRepository.save(item);
                })
                .orElseGet(() -> {
                    CartItem newItem = CartItem.builder()
                            .user(user)
                            .product(product)
                            .quantity(quantity)
                            .priceAtTime(product.getPrice())
                            .build();
                    return cartItemRepository.save(newItem);
                });
    }

    public void removeFromCart(String email, Long cartItemId) {
        User user = getUser(email);
        CartItem item = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new ResourceNotFoundException(
                    "Cart item not found"));

        if (!item.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Unauthorized");
        }
        cartItemRepository.delete(item);
    }

    public void clearCart(String email) {
        User user = getUser(email);
        cartItemRepository.deleteByUser(user);
    }

    private User getUser(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException(
                    "User not found: " + email));
    }
}
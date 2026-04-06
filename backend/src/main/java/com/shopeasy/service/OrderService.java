package com.shopeasy.service;

import com.shopeasy.dto.OrderRequest;
import com.shopeasy.exception.ResourceNotFoundException;
import com.shopeasy.model.*;
import com.shopeasy.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final CartItemRepository cartItemRepository;
    private final UserRepository userRepository;
    private final SnsService snsService;

    @Transactional
    public Order placeOrder(String email, OrderRequest request) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException(
                    "User not found"));

        List<CartItem> cartItems = cartItemRepository.findByUser(user);

        if (cartItems.isEmpty()) {
            throw new RuntimeException("Cart is empty");
        }

        // Cart items → Order items
        List<OrderItem> orderItems = cartItems.stream()
                .map(cartItem -> OrderItem.builder()
                        .product(cartItem.getProduct())
                        .quantity(cartItem.getQuantity())
                        .priceAtTime(cartItem.getPriceAtTime())
                        .build())
                .collect(Collectors.toList());

        // Total calculate karo
        BigDecimal total = orderItems.stream()
                .map(item -> item.getPriceAtTime()
                    .multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // Order banao
        Order order = Order.builder()
                .user(user)
                .items(orderItems)
                .totalAmount(total)
                .shippingAddress(request.getShippingAddress())
                .status(Order.Status.CONFIRMED)
                .build();

        orderItems.forEach(item -> item.setOrder(order));
        Order savedOrder = orderRepository.save(order);

        // Cart clear karo
        cartItemRepository.deleteByUser(user);

        // SNS notification bhejo
        snsService.sendOrderConfirmation(
            email, savedOrder.getId(), total.toString());

        return savedOrder;
    }

    public List<Order> getUserOrders(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException(
                    "User not found"));
        return orderRepository.findByUserOrderByCreatedAtDesc(user);
    }
}
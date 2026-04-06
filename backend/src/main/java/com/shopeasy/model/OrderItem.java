package com.shopeasy.model;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;

@Entity
@Table(name = "order_items")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    private Integer quantity;

    private BigDecimal priceAtTime;
}
// ```

// ---

// ## 🧠 Samjho — CartItem vs OrderItem kyun alag?

// Bhai yeh confuse karta hai sabko — samjhao:
// ```
// CartItem   → Temporary  → Jab tak order nahi hua
// OrderItem  → Permanent  → Jab order place ho gaya
// ```

// Real life:
// ```
// 1. Tune shoes cart mein daale      → CartItem bana
// 2. Tune "Place Order" click kiya   → OrderItem bana, CartItem delete hua
// 3. Order history mein hamesha      → OrderItem rahega
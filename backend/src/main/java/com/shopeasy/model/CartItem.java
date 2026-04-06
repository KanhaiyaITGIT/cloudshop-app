package com.shopeasy.model;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;

@Entity
@Table(name = "cart_items")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    private Integer quantity = 1;

    private BigDecimal priceAtTime;
}
// ```

// ---

// ## 🧠 Samjho — Relationships

// **`@ManyToOne`** — Yeh relationship batata hai:
// - Ek User ke **many** CartItems ho sakte hain
// - Ek CartItem ka sirf **one** User hoga

// Real life example:
// ```
// Mukesh → [Shoes, T-Shirt, Watch]   ← Mukesh ke 3 cart items
// Rahul  → [Laptop, Mouse]           ← Rahul ke 2 cart items
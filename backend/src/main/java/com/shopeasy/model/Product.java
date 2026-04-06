package com.shopeasy.model;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "products")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    private String description;

    private String category;

    private String imageUrl;

    @Column(nullable = false)
    private BigDecimal price;

    private Integer stock = 0;

    private Boolean active = true;

    private LocalDateTime createdAt = LocalDateTime.now();
}
// ```

// ---

// ## 🧠 Nayi Cheezein Samjho

// **`BigDecimal price`** — Price ke liye `double` ya `float` kyun nahi? Kyunki:
// ```
// double:  99.99 + 0.01 = 99.99999999  ← Galat!
// BigDecimal: 99.99 + 0.01 = 100.00    ← Sahi!
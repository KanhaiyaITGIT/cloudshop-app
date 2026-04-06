package com.shopeasy.repository;

import com.shopeasy.model.Order;
import com.shopeasy.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findByUserOrderByCreatedAtDesc(User user);

    List<Order> findByStatus(Order.Status status);
}
// ```

// ---

// ## 🧠 Samjho

// **`findByUserOrderByCreatedAtDesc`** — Yeh ek hi method mein do kaam kar raha hai:
// ```
// findByUser          → Is user ke orders lao
// OrderByCreatedAtDesc → Naye orders pehle dikhao (latest first)
// ```

// Real life:
// ```
// Order #105 → Aaj        ← Pehle dikhega
// Order #98  → Kal        ← Doosra
// Order #45  → Last month ← Teesra
// ```

// **`findByStatus(Order.Status status)`** — Admin ke liye useful:
// ```
// PENDING orders    → Jinhe confirm karna hai
// CONFIRMED orders  → Jinhe ship karna hai
// SHIPPED orders    → Jo raaste mein hain
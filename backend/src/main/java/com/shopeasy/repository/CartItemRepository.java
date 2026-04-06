package com.shopeasy.repository;

import com.shopeasy.model.CartItem;
import com.shopeasy.model.User;
import com.shopeasy.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {

    List<CartItem> findByUser(User user);

    Optional<CartItem> findByUserAndProduct(User user, Product product);

    void deleteByUser(User user);
}
// ```

// ---

// ## 🧠 Samjho — Yeh Methods Kyun?

// **`findByUser(User user)`** — Ek user ka poora cart lao:
// ```
// Mukesh ka cart → [Shoes, T-Shirt, Watch]
// ```

// **`findByUserAndProduct(User user, Product product)`** — Check karo ki yeh product already cart mein hai ya nahi:
// ```
// Mukesh ne dobara Shoes add kiya?
// → Already hai → quantity badhao
// → Nahi hai    → naya CartItem banao
// ```

// **`void deleteByUser(User user)`** — Order place hone ke baad poora cart saaf karo:
// ```
// Order place hua ✅
// → Cart empty karo
// → User fresh start kare
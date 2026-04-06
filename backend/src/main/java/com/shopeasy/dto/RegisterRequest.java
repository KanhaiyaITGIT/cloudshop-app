package com.shopeasy.dto;

import jakarta.validation.constraints.*;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequest {

    @NotBlank(message = "Name cannot be empty")
    private String name;

    @Email(message = "Valid email required")
    @NotBlank(message = "Email cannot be empty")
    private String email;

    @NotBlank(message = "Password cannot be empty")
    @Size(min = 6, message = "Password must be at least 6 characters")
    private String password;

    @Pattern(regexp = "^[0-9]{10}$", message = "Phone must be 10 digits")
    private String phone;
}
// ```

// ---

// ## 🧠 Nayi Cheezein Samjho

// **`@Size(min = 6)`** — Password minimum 6 characters ka hona chahiye. Backend validate karega — frontend pe sirf depend nahi karte kyunki koi directly API call kar sakta hai Postman se.

// **`@Pattern(regexp = "^[0-9]{10}$")`** — Phone number exactly 10 digits ka hona chahiye:
// ```
// 9876543210  ✅
// 98765       ❌ (5 digits)
// 987654321a  ❌ (letter hai)
// ```

// **LoginRequest vs RegisterRequest ka difference:**
// ```
// LoginRequest    → email, password        (2 fields — sirf verify karo)
// RegisterRequest → name, email, 
//                   password, phone        (4 fields — naya account bana raha hai)
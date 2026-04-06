package com.shopeasy.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final UserDetailsServiceImpl userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = authHeader.substring(7);

        if (jwtUtil.isValid(token)) {
            String email = jwtUtil.extractEmail(token);
            UserDetails userDetails = userDetailsService.loadUserByUsername(email);

            UsernamePasswordAuthenticationToken authToken =
                new UsernamePasswordAuthenticationToken(
                    userDetails, null, userDetails.getAuthorities()
                );

            authToken.setDetails(
                new WebAuthenticationDetailsSource().buildDetails(request)
            );

            SecurityContextHolder.getContext().setAuthentication(authToken);
        }

        filterChain.doFilter(request, response);
    }
}
// ```

// ---

// ## 🧠 Samjho — Yeh Filter Kya Karta Hai

// **Filter ka kaam** — Har request backend pe aane se pehle iss filter se guzarti hai. Jaise building ka security guard — pehle ID check, phir andar jaao.
// ```
// Request aai
//     ↓
// Authorization header hai?  → Nahi → Seedha jaane do (public route)
//     ↓
// "Bearer " se start hota?   → Nahi → Seedha jaane do
//     ↓
// Token valid hai?           → Nahi → Seedha jaane do (unauthorized)
//     ↓
// Email nikalo token se
//     ↓
// User load karo database se
//     ↓
// Spring ko batao "yeh user logged in hai"
//     ↓
// Request aage jaaye
// ```

// **`authHeader.substring(7)`** — Header aisa hota hai:
// ```
// "Bearer eyJhbGciOiJIUzI1NiJ9..."
//          ↑
//          7 characters — "Bearer " hata ke sirf token lo
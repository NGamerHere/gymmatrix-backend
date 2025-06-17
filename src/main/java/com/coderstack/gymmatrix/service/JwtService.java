package com.coderstack.gymmatrix.service;

import com.coderstack.gymmatrix.enums.UserType;
import com.coderstack.gymmatrix.utils.JwtUtil;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class JwtService {

    @Autowired
    private JwtUtil jwtUtil;

    public Claims extractClaimsFromRequest(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            if (!jwtUtil.isTokenExpired(token)) {
                return jwtUtil.extractAllClaims(token);
            }
        }
        return null;
    }

    public boolean hasRole(Claims claims, UserType role) {
        System.out.println(" role.name() " +role.name());
        System.out.println(" clamis " +claims.get("role"));
        return claims != null && role.name().equals(claims.get("role"));
    }

    public Integer getGymId(Claims claims) {
        return claims != null ? (Integer) claims.get("gym_id") : null;
    }
}

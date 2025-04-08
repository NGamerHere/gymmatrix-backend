package com.coderstack.gymmatrix.service;

import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;

@Service
public class GymAccessValidator {

    public boolean validateAccess(HttpServletRequest request, Claims claims) {
        String[] parts = request.getRequestURI().split("/");
        if (parts.length >= 4) {
            try {
                int pathGymId = Integer.parseInt(parts[3]);
                Integer tokenGymId = (Integer) claims.get("gym_id");
                return pathGymId == tokenGymId;
            } catch (NumberFormatException e) {
                return false;
            }
        }
        return false;
    }
}
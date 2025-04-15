package com.coderstack.gymmatrix.service;

import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class GymAccessValidator {

    @Value("${application.env.config}")
    private String config;


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

    public boolean Ipvalidate(HttpServletRequest request, Claims claims) {
        String ipAddress;

        if (Objects.equals(config, "prod")) {
            ipAddress = request.getHeader("X-Forwarded-For");
            if (ipAddress != null && ipAddress.contains(",")) {
                ipAddress = ipAddress.split(",")[0];
            }
        } else {
            ipAddress = request.getRemoteAddr();
        }
        System.out.println("IP from the request: " + ipAddress + " | IP from the token: " + claims.get("Ip"));
        return ipAddress != null && ipAddress.equalsIgnoreCase((String) claims.get("Ip"));
    }
}
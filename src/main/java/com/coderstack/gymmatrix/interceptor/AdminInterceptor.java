package com.coderstack.gymmatrix.interceptor;

import com.coderstack.gymmatrix.enums.UserType;
import com.coderstack.gymmatrix.service.GymAccessValidator;
import com.coderstack.gymmatrix.service.JwtService;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.io.IOException;

@Component
public class AdminInterceptor implements HandlerInterceptor {

    @Autowired
    JwtService jwtService;

    @Autowired
    GymAccessValidator gymAccessValidator;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException {
        try {
            if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
                response.setStatus(HttpServletResponse.SC_OK);
                return false;
            }
            String[] parts = request.getRequestURI().split("/");
            UserType userType = UserType.valueOf(parts[4]);
            System.out.println(userType);
            if (userType == null) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Invalid role");
                return false;
            }
            Claims claims = jwtService.extractClaimsFromRequest(request);
            if (claims == null || !jwtService.hasRole(claims, userType)) {
                response.sendError(HttpServletResponse.SC_FORBIDDEN, "Invalid or expired token");
                return false;
            }
            if (!gymAccessValidator.Ipvalidate(request, claims)) {
                response.sendError(HttpServletResponse.SC_FORBIDDEN, "you cant access this resource");
                return false;
            }

            if (!gymAccessValidator.validateAccess(request, claims)) {
                response.sendError(HttpServletResponse.SC_FORBIDDEN, "Access to this gym is forbidden");
                return false;
            }

            request.setAttribute("sessionData", claims);
            return true;
        }catch (IllegalArgumentException e) {
            System.out.println("Illegal Argument Exception: " + e.getMessage());
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid status value");
            return false;
        }
    }
}


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
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            response.setStatus(HttpServletResponse.SC_OK);
            return false;
        }
        Claims claims = jwtService.extractClaimsFromRequest(request);
        if (claims == null || !jwtService.hasRole(claims, UserType.admin)) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Invalid or expired token");
            return false;
        }

        if (!gymAccessValidator.validateAccess(request, claims)) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Access to this gym is forbidden");
            return false;
        }

        request.setAttribute("sessionData", claims);
        return true;
    }
}


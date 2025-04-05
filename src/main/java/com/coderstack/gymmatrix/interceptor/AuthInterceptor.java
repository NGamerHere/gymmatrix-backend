package com.coderstack.gymmatrix.interceptor;

import com.coderstack.gymmatrix.utils.JwtUtil;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.io.IOException;

@Component
public class AuthInterceptor implements HandlerInterceptor {

    @Autowired
    JwtUtil jwtUtil;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException {

        String bearerToken=request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")){
            try{
                String token = bearerToken.substring(7);
                if(!jwtUtil.isTokenExpired(token)){
                    Claims claims=jwtUtil.extractAllClaims(token);
                    request.setAttribute("sessionData",claims);
                    return true;
                }else {
                    response.sendError(HttpServletResponse.SC_FORBIDDEN,"Token expired");
                    return false;
                }
            } catch (Exception e) {
                response.sendError(HttpServletResponse.SC_FORBIDDEN,"Invalid Token");
                return false;
            }
        }
        response.sendError(HttpServletResponse.SC_FORBIDDEN, "Forbidden: Invalid or Missing Token");
        return false;
    }
}

package com.coderstack.gymmatrix.controller;

import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.HashMap;
import java.util.Map;


@Controller
@RequestMapping("/api/dashboard")
public class UserController {

    @GetMapping("info")
    public ResponseEntity<?> getDashboardInfo(HttpServletRequest request){
        Claims claims = (Claims) request.getAttribute("sessionData");
        Integer userIdInt = (Integer) claims.get("user_id");
        String role= (String) claims.get("role");
        Integer gym_id=(Integer) claims.get("gym_id");
        Map<String, Object> res = new HashMap<>();
        res.put("user_id", userIdInt);
        res.put("role", role);
        res.put("gym_id", gym_id);
        return ResponseEntity.ok(res);
    }

}

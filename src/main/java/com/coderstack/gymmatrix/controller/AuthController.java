package com.coderstack.gymmatrix.controller;

import com.coderstack.gymmatrix.dto.SignIn;
import com.coderstack.gymmatrix.enums.UserType;
import com.coderstack.gymmatrix.models.Admin;
import com.coderstack.gymmatrix.models.Gym;
import com.coderstack.gymmatrix.models.User;
import com.coderstack.gymmatrix.repository.AdminRepository;
import com.coderstack.gymmatrix.repository.UserRepository;
import com.coderstack.gymmatrix.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("signin")
    public ResponseEntity<?> adminSignIn(@RequestBody SignIn signData) {
        Map<String, String> res = new HashMap<>();
        if (signData.role == UserType.admin) {
            Admin admin = adminRepository.findByEmail(signData.email);
            Gym gym = admin.getGym();
            Map<String, Object> claims = Map.of("role", UserType.admin
                    , "user_id", admin.getId()
                    , "gym_id", gym.getId()
            );
            String token = jwtUtil.generateToken(admin.getEmail(), claims);
            res.put("token", token);
            return ResponseEntity.ok(res);
        } else {
            res.put("message", "currently only admin can login");
            return ResponseEntity.status(404).body(res);
        }
    }
}

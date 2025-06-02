package com.coderstack.gymmatrix.controller;

import com.coderstack.gymmatrix.dto.SignIn;
import com.coderstack.gymmatrix.enums.UserType;
import com.coderstack.gymmatrix.models.*;
import com.coderstack.gymmatrix.repository.AdminRepository;
import com.coderstack.gymmatrix.repository.MemberRepository;
import com.coderstack.gymmatrix.repository.TrainerRepository;
import com.coderstack.gymmatrix.repository.UserRepository;
import com.coderstack.gymmatrix.utils.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Controller
@CrossOrigin
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserRepository userRepository;

    @PostMapping("signin")
    public ResponseEntity<?> adminSignIn(HttpServletRequest request, @RequestBody SignIn signData) {
        Map<String, Object> res = new HashMap<>();
        String ipAddress = request.getRemoteAddr();

        User user = userRepository.findUserByEmailAndUserType(signData.email,signData.role);
        if(user == null || !user.comparePassword(signData.password) ) {
            res.put("error", "Invalid email or password");
            return ResponseEntity.status(401).body(res);
        }
        Gym gym = user.getGym();
        Map<String, Object> claims = Map.of(
                "role", signData.role
                , "user_id", user.getId()
                , "gym_id", gym.getId()
                , "Ip",ipAddress
        );
        String token = jwtUtil.generateToken(user.getEmail(), claims);
        res.put("token", token);
        res.put("role",user.getUserType());
        res.put("user_id",user.getId());
        res.put("gym_id",gym.getId());
        return ResponseEntity.ok(res);
    }

    @PostMapping("forget-password")
    public ResponseEntity<?> forgetPassword(String email){
        return ResponseEntity.ok(email);
    }
}

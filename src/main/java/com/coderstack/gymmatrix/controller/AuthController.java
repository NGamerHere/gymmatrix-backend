package com.coderstack.gymmatrix.controller;

import com.coderstack.gymmatrix.dto.SignIn;
import com.coderstack.gymmatrix.enums.UserType;
import com.coderstack.gymmatrix.models.Admin;
import com.coderstack.gymmatrix.models.Gym;
import com.coderstack.gymmatrix.models.Trainer;
import com.coderstack.gymmatrix.repository.AdminRepository;
import com.coderstack.gymmatrix.repository.TrainerRepository;
import com.coderstack.gymmatrix.utils.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

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
    private TrainerRepository trainerRepository;

    @PostMapping("signin")
    public ResponseEntity<?> adminSignIn(HttpServletRequest request, @RequestBody SignIn signData) {
        Map<String, Object> res = new HashMap<>();
        String ipAddress = request.getRemoteAddr();
        if (signData.role == UserType.admin) {
            Admin admin = adminRepository.findByEmail(signData.email);
            if(admin == null || !admin.comparePassword(signData.password) ) {
                res.put("error", "Invalid email or password");
                return ResponseEntity.status(401).body(res);
            }
            Gym gym = admin.getGym();
            Map<String, Object> claims = Map.of("role", UserType.admin
                    , "user_id", admin.getId()
                    , "gym_id", gym.getId()
                    , "Ip",ipAddress
            );
            String token = jwtUtil.generateToken(admin.getEmail(), claims);
            res.put("token", token);
            res.put("role",UserType.admin);
            res.put("user_id",admin.getId());
            res.put("gym_id",gym.getId());
            return ResponseEntity.ok(res);
        } else if (signData.role == UserType.trainer) {
            Trainer trainer = trainerRepository.findByEmail(signData.email);
            if(trainer == null || !trainer.comparePassword(signData.password) ) {
                res.put("error", "Invalid email or password");
                return ResponseEntity.status(401).body(res);
            }
            Gym gym = trainer.getGym();
            Map<String, Object> claims = Map.of("role", UserType.admin
                    , "user_id", trainer.getId()
                    , "gym_id", gym.getId()
                    , "Ip",ipAddress
            );
            String token = jwtUtil.generateToken(trainer.getEmail(), claims);
            res.put("token", token);
            res.put("role",UserType.trainer);
            res.put("user_id",trainer.getId());
            res.put("gym_id",gym.getId());
            return ResponseEntity.ok(res);
        } else {
            res.put("message", "currently only admin can login");
            return ResponseEntity.status(404).body(res);
        }
    }
}

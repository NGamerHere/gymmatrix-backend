package com.coderstack.gymmatrix.controller;

import com.coderstack.gymmatrix.dto.SignIn;
import com.coderstack.gymmatrix.models.Gym;
import com.coderstack.gymmatrix.models.User;
import com.coderstack.gymmatrix.repository.UserRepository;
import com.coderstack.gymmatrix.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;

@Controller
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("signin")
    public ResponseEntity<?> signin(@RequestBody SignIn signData){
        User user=userRepository.findByEmail(signData.email);
        if(user!=null){
            if(user.comparePassword(signData.password)){
                Gym gym=user.getGym();
                Map<String, Object> claims = Map.of("role", user.getUserType()
                        ,"user_id",user.getId()
                        ,"gym_id",gym.getId()
                );
                String token=jwtUtil.generateToken(user.getEmail(),claims);
                return ResponseEntity.ok("token :"+token);
            }else {
                return ResponseEntity.ok("wrong password");
            }
        }else {
            return ResponseEntity.ok("you have been logged in");
        }
    }



}

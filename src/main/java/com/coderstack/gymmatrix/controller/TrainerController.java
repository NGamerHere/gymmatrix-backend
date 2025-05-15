package com.coderstack.gymmatrix.controller;


import com.coderstack.gymmatrix.models.Gym;
import com.coderstack.gymmatrix.models.Trainer;
import com.coderstack.gymmatrix.repository.GymRepository;
import com.coderstack.gymmatrix.repository.MemberRepository;
import com.coderstack.gymmatrix.repository.TrainerRepository;
import com.coderstack.gymmatrix.service.MailService;
import com.coderstack.gymmatrix.service.PasswordGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

@RestController
@CrossOrigin
@RequestMapping("/api/admin/{gym_id}")
public class TrainerController {

    @Autowired
    private GymRepository gymRepository;

    @Autowired
    private TrainerRepository trainerRepository;

    @Autowired
    private MailService mailService;


    @PostMapping("/trainer")
    public ResponseEntity<?> trainer(@PathVariable int gym_id, @RequestBody Trainer trainer) {
        HashMap<String, Object> res = new HashMap<>();
        Gym gym = gymRepository.findById(gym_id)
                .orElseThrow(() -> new RuntimeException("GYM not found"));
        trainer.setGym(gym);
        String password= PasswordGenerator.generateRandomPassword(10);
        trainer.setPassword(password);
        mailService.sendWelcomeEmail(trainer.getEmail(),trainer.getName(),password,"");
        Trainer newTrainer= trainerRepository.save(trainer);
        res.put("status", "success");
        res.put("id", newTrainer.getId());
        return ResponseEntity.ok(res);
    }

}

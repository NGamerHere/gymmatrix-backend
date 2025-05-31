package com.coderstack.gymmatrix.controller;

import com.coderstack.gymmatrix.dto.TrainerAssignmentRequest;
import com.coderstack.gymmatrix.dto.TrainerDTO;
import com.coderstack.gymmatrix.enums.UserType;
import com.coderstack.gymmatrix.exceptions.ResourceNotFoundException;
import com.coderstack.gymmatrix.models.Gym;
import com.coderstack.gymmatrix.models.Member;
import com.coderstack.gymmatrix.models.Trainer;
import com.coderstack.gymmatrix.models.User;
import com.coderstack.gymmatrix.repository.GymRepository;
import com.coderstack.gymmatrix.repository.MemberRepository;
import com.coderstack.gymmatrix.repository.TrainerRepository;
import com.coderstack.gymmatrix.repository.UserRepository;
import com.coderstack.gymmatrix.service.DuplicateCheckService;
import com.coderstack.gymmatrix.service.MailService;
import com.coderstack.gymmatrix.service.PasswordGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.coderstack.gymmatrix.service.RespoanceService.sendSuccessResponse;
import static com.coderstack.gymmatrix.service.RespoanceService.sendErrorResponse;

@RestController
@CrossOrigin
@RequestMapping("/api/gym/{gym_id}/admin/{admin_id}")
public class AdminTrainerController {

    @Autowired
    private GymRepository gymRepository;

    @Autowired
    private MailService mailService;

    @Autowired
    private DuplicateCheckService duplicateCheckService;

    @Autowired
    private UserRepository userRepository;


    @PostMapping("/trainer")
    public ResponseEntity<?> trainer(@PathVariable int gym_id, @RequestBody User trainer) {
        HashMap<String, Object> res = new HashMap<>();
        Gym gym = gymRepository.findById(gym_id)
                .orElseThrow(() -> new RuntimeException("GYM not found"));

        Map<String, String> duplicate = duplicateCheckService.checkDuplicate(gym.getId(), trainer.getPhone(), trainer.getEmail());

        if (!duplicate.isEmpty()) {
            if (duplicate.get("phone") != null) {
                return sendSuccessResponse("Phone number already exists", 409);
            } else if (duplicate.get("email") != null) {
                return sendErrorResponse("Email already exists", 409);
            }
        }
        trainer.setUserType(UserType.trainer);
        trainer.setGym(gym);
        String password = PasswordGenerator.generateRandomPassword(10);
        trainer.setPassword(password);
        mailService.sendWelcomeEmail(UserType.trainer,trainer.getEmail(), trainer.getName(), password, "",trainer.getGym().getName());
        User newTrainer = userRepository.save(trainer);
        res.put("status", "success");
        res.put("id", newTrainer.getId());
        return ResponseEntity.ok(res);
    }

    @GetMapping("/trainer")
    public ResponseEntity<?> getTrainers(@PathVariable int gym_id) {
        Gym gym = gymRepository.findById(gym_id)
                .orElseThrow(() -> new RuntimeException("GYM not found"));
        List<User> trainerInfo=userRepository.findByGymAndUserType(gym,UserType.trainer);
        return ResponseEntity.ok(trainerInfo);
    }
    @GetMapping("/trainer/{trainer_id}")
    public ResponseEntity<?> getTrainer(@PathVariable int gym_id, @PathVariable int trainer_id) {
         User trainer=userRepository.findUserByIdAndUserType(trainer_id,UserType.trainer);
         if(trainer == null){
             return ResponseEntity.status(404).body(Map.of("error","trainer not found"));
         }
         return ResponseEntity.ok(trainer);
    }


    @PostMapping("/assign-trainer")
    public ResponseEntity<?> assignTrainer(@RequestBody TrainerAssignmentRequest trainerAssignmentRequest, @PathVariable String gym_id) {
        User member = userRepository.findByIdAndUserType(trainerAssignmentRequest.member_id,UserType.member).orElseThrow(() -> new ResourceNotFoundException("Member not found"));
        if(member.getTrainer() != null){
            return sendErrorResponse("trainer is already exists for this user", 400);
        }
        User trainer = userRepository.findByIdAndUserType(trainerAssignmentRequest.trainer_id,UserType.trainer).orElseThrow(() -> new ResourceNotFoundException("Trainer not found"));
        member.setTrainer(trainer);
        trainer.getMembers().add(member);
        userRepository.save(member);
        userRepository.save(trainer);
        HashMap<String, Object> res = new HashMap<>();
        res.put("status", "success");
        return ResponseEntity.ok(res);
    }

}

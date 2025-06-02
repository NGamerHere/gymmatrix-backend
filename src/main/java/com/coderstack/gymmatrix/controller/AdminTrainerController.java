package com.coderstack.gymmatrix.controller;

import com.coderstack.gymmatrix.dto.NewTrainer;
import com.coderstack.gymmatrix.dto.TrainerAssignmentRequest;
import com.coderstack.gymmatrix.enums.UserType;
import com.coderstack.gymmatrix.exceptions.ResourceNotFoundException;
import com.coderstack.gymmatrix.models.Gym;
import com.coderstack.gymmatrix.models.User;
import com.coderstack.gymmatrix.repository.GymRepository;
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
    public ResponseEntity<?> trainer(@PathVariable int gym_id, @RequestBody NewTrainer trainer) {
        HashMap<String, Object> res = new HashMap<>();
        Gym gym = gymRepository.findById(gym_id)
                .orElseThrow(() -> new RuntimeException("GYM not found"));

        System.out.println(trainer.email);
        System.out.println(trainer.phone);

        Map<String, String> duplicate = duplicateCheckService.checkDuplicate(gym.getId(), trainer.phone, trainer.email);

        if (!duplicate.isEmpty()) {
            if (duplicate.get("phone") != null) {
                return sendSuccessResponse("Phone number already exists", 409);
            } else if (duplicate.get("email") != null) {
                return sendErrorResponse("Email already exists", 409);
            }
        }


        User newTrainer = new User();
        newTrainer.setName(trainer.name);
        newTrainer.setEmail(trainer.email);
        newTrainer.setPhone(trainer.phone);
        newTrainer.setGender(trainer.gender);
        newTrainer.setAge(trainer.age);
        newTrainer.setAddress(trainer.address);
        newTrainer.setCity(trainer.city);
        newTrainer.setState(trainer.state);
        newTrainer.setCountry(trainer.country);
        newTrainer.setZip(trainer.zip);
        newTrainer.setGym(gym);
        newTrainer.setUserType(UserType.trainer);
        String password = PasswordGenerator.generateRandomPassword(10);
        newTrainer.setPassword(password);
        mailService.sendWelcomeEmail(UserType.trainer,newTrainer.getEmail(), newTrainer.getName(), password, "",newTrainer.getGym().getName());
        User savedTrainer = userRepository.save(newTrainer);
        res.put("status", "success");
        res.put("id", savedTrainer.getId());
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
         User trainer=userRepository.findUserByIdAndUserType(trainer_id,UserType.trainer).orElseThrow( () -> new ResourceNotFoundException("Trainer not found with id: " + trainer_id) );
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

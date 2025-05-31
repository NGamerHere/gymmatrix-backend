package com.coderstack.gymmatrix.controller;

import com.coderstack.gymmatrix.dto.TrainerAssignmentRequest;
import com.coderstack.gymmatrix.dto.TrainerDTO;
import com.coderstack.gymmatrix.enums.UserType;
import com.coderstack.gymmatrix.exceptions.ResourceNotFoundException;
import com.coderstack.gymmatrix.models.Gym;
import com.coderstack.gymmatrix.models.Member;
import com.coderstack.gymmatrix.models.Trainer;
import com.coderstack.gymmatrix.repository.GymRepository;
import com.coderstack.gymmatrix.repository.MemberRepository;
import com.coderstack.gymmatrix.repository.TrainerRepository;
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
@RequestMapping("/api/admin/{gym_id}")
public class AdminTrainerController {

    @Autowired
    private GymRepository gymRepository;

    @Autowired
    private TrainerRepository trainerRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private MailService mailService;

    @Autowired
    private DuplicateCheckService duplicateCheckService;


//    @PostMapping("/trainer")
//    public ResponseEntity<?> trainer(@PathVariable int gym_id, @RequestBody Trainer trainer) {
//        HashMap<String, Object> res = new HashMap<>();
//        Gym gym = gymRepository.findById(gym_id)
//                .orElseThrow(() -> new RuntimeException("GYM not found"));
//
//        Map<String, String> duplicate = duplicateCheckService.checkDuplicate(
//                gym.getId(), trainer.getPhone(), trainer.getEmail(), UserType.trainer);
//
//        if (!duplicate.isEmpty()) {
//            if (duplicate.get("phone") != null) {
//                return sendSuccessResponse("Phone number already exists", 409);
//            } else if (duplicate.get("email") != null) {
//                return sendErrorResponse("Email already exists", 409);
//            }
//        }
//        trainer.setUserType(UserType.trainer);
//        trainer.setGym(gym);
//        String password = PasswordGenerator.generateRandomPassword(10);
//        trainer.setPassword(password);
//        mailService.sendWelcomeEmail(UserType.trainer,trainer.getEmail(), trainer.getName(), password, "",trainer.getGym().getName());
//        Trainer newTrainer = trainerRepository.save(trainer);
//        res.put("status", "success");
//        res.put("id", newTrainer.getId());
//        return ResponseEntity.ok(res);
//    }
//
//    @GetMapping("/trainer")
//    public ResponseEntity<?> getTrainers(@PathVariable int gym_id) {
//        List<TrainerDTO> trainerInfo=trainerRepository.findByGym(gym_id);
//        return ResponseEntity.ok(trainerInfo);
//    }
//
//    @GetMapping("/trainer/{trainer_id}")
//    public ResponseEntity<?> getTrainer(@PathVariable int gym_id, @PathVariable int trainer_id) {
//         Trainer trainer=trainerRepository.findById(trainer_id).orElseThrow(() -> new ResourceNotFoundException("Trainer not found"));
//         return ResponseEntity.ok(trainer);
//    }
//
//
//    @PostMapping("/assign-trainer")
//    public ResponseEntity<?> assignTrainer(@RequestBody TrainerAssignmentRequest trainerAssignmentRequest, @PathVariable String gym_id) {
//        Member member = memberRepository.findById(trainerAssignmentRequest.member_id).orElseThrow(() -> new ResourceNotFoundException("Member not found"));
//        if(member.getTrainer() != null){
//            return sendErrorResponse("trainer is already exists for this user", 400);
//        }
//        Trainer trainer = trainerRepository.findById(trainerAssignmentRequest.trainer_id).orElseThrow(() -> new ResourceNotFoundException("Trainer not found"));
//        trainer.addMember(member);
//        member.setTrainer(trainer);
//        memberRepository.save(member);
//        trainerRepository.save(trainer);
//        HashMap<String, Object> res = new HashMap<>();
//        res.put("status", "success");
//        return ResponseEntity.ok(res);
//    }

}

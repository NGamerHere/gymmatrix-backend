package com.coderstack.gymmatrix.controller;

import com.coderstack.gymmatrix.enums.PlanStatus;
import com.coderstack.gymmatrix.enums.UserType;
import com.coderstack.gymmatrix.models.Gym;
import com.coderstack.gymmatrix.models.Member;
import com.coderstack.gymmatrix.models.User;
import com.coderstack.gymmatrix.repository.GymRepository;
import com.coderstack.gymmatrix.repository.MemberRepository;
import com.coderstack.gymmatrix.repository.MembershipRepository;
import com.coderstack.gymmatrix.repository.UserRepository;
import com.coderstack.gymmatrix.service.AdminStatsService;
import com.coderstack.gymmatrix.service.DuplicateCheckService;
import com.coderstack.gymmatrix.service.MailService;
import com.coderstack.gymmatrix.service.PasswordGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static com.coderstack.gymmatrix.service.RespoanceService.sendErrorResponse;
import static com.coderstack.gymmatrix.service.RespoanceService.sendSuccessResponse;

@RestController
@CrossOrigin
@RequestMapping("/api/gym/{gym_id}/admin/{admin_id}")
public class MemberController {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private GymRepository gymRepository;

    @Autowired
    private DuplicateCheckService duplicateCheckService;

    @Autowired
    private MembershipRepository membershipRepository;

    @Autowired
    private MailService mailService;

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/member")
    public ResponseEntity<?> addNewUser(@PathVariable int gym_id, @RequestBody User newMember) {
        Optional<Gym> gymOpt = getGymById(gym_id);
        if (gymOpt.isEmpty()) {
            return sendErrorResponse("Gym not found", 404);
        }

        Gym gym = gymOpt.get();
        Map<String, String> duplicate = duplicateCheckService.checkDuplicate(
                gym.getId(), newMember.getPhone(), newMember.getEmail(), UserType.member);

        if (!duplicate.isEmpty()) {
            if (duplicate.get("phone") != null) {
                return sendErrorResponse("Phone number already exists", 409);
            } else if (duplicate.get("email") != null) {
                return sendErrorResponse("Email already exists", 409);
            }
        }

        newMember.setGym(gym);
        String password=PasswordGenerator.generateRandomPassword(10);
        newMember.setPassword(password);
        newMember.setUserType(UserType.member);
        User member=userRepository.save(newMember);
        mailService.sendWelcomeEmail(UserType.member,member.getEmail(), member.getName(), password, "",member.getGym().getName());
        return sendSuccessResponse("New member saved successfully", member.getId());
    }

    @GetMapping("/member")
    public ResponseEntity<?> getMember(@PathVariable int gym_id) {
        return ResponseEntity.ok(userRepository.getMembersProjection(gym_id));
    }

    @GetMapping("/member/{member_id}")
    public ResponseEntity<?> getMember(@PathVariable int gym_id, @PathVariable int member_id) {
        Optional<Gym> gymOpt = getGymById(gym_id);
        if (gymOpt.isEmpty()) {
            return sendErrorResponse("Gym not found", 404);
        }
        Map<String, Object> res = new HashMap<>();
        User user=userRepository.findUserByIdAndUserType(member_id,UserType.member);
        if (user == null) {
            return sendErrorResponse("Member not found", 404);
        }
        res.put("total_active_memberships",membershipRepository.countActiveMemberShip(gym_id, member_id, PlanStatus.ACTIVE));
        res.put("memberInfo",user);
        res.put("planHistory", memberRepository.findPaymentDetailsByGymIdAndMemberId(gym_id, member_id));
        return ResponseEntity.ok(res);
    }

    @PutMapping("/member/{member_id}")
    public ResponseEntity<?> updateMember(@PathVariable int gym_id, @PathVariable int member_id, @RequestBody User updatedMember) {
        Optional<Gym> gymOpt = getGymById(gym_id);
        if (gymOpt.isEmpty()) {
            return sendErrorResponse("Gym not found", 404);
        }

        Optional<User> existingMemberOpt = userRepository.findById(member_id);
        if (existingMemberOpt.isEmpty()) {
            return sendErrorResponse("Member not found", 404);
        }

        User existingMember = existingMemberOpt.get();
        if (existingMember.getGym().getId() != gym_id) {
            return sendErrorResponse("Member does not belong to this gym", 403);
        }

        existingMember.setName(updatedMember.getName());
        existingMember.setPhone(updatedMember.getPhone());
        existingMember.setEmail(updatedMember.getEmail());
        existingMember.setAddress(updatedMember.getAddress());
        existingMember.setCity(updatedMember.getCity());
        existingMember.setState(updatedMember.getState());
        existingMember.setCountry(updatedMember.getCountry());

        userRepository.save(existingMember);
        return sendSuccessResponse("Member updated successfully");
    }

    @DeleteMapping("/member/{member_id}")
    public ResponseEntity<?> deleteMember(@PathVariable int gym_id, @PathVariable int member_id) {
        Optional<Gym> gymOpt = getGymById(gym_id);
        if (gymOpt.isEmpty()) {
            return sendErrorResponse("Gym not found", 404);
        }

        Optional<User> memberOpt = userRepository.findById(member_id);
        if (memberOpt.isEmpty()) {
            return sendErrorResponse("Member not found", 404);
        }

        User member = memberOpt.get();
        if (member.getGym().getId() != gym_id) {
                return sendErrorResponse("Member does not belong to this gym", 403);
        }

        userRepository.delete(member);
        return sendSuccessResponse("Member deleted successfully");
    }


    private Optional<Gym> getGymById(int gymId) {
        return gymRepository.findById(gymId);
    }
}

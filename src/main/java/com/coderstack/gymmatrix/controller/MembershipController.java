package com.coderstack.gymmatrix.controller;

import com.coderstack.gymmatrix.dto.MembershipResponseDTO;
import com.coderstack.gymmatrix.enums.PaymentType;
import com.coderstack.gymmatrix.models.*;
import com.coderstack.gymmatrix.repository.*;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin
@RequestMapping("/api/admin/{gym_id}")
public class MembershipController {

    @Autowired
    private MembershipRepository membershipRepository;

    @Autowired
    private GymRepository gymRepository;

    @Autowired
    private MembershipPlanRepository membershipPlanRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private PaymentRepository paymentRepository;


    @PostMapping("/memberships")
    public ResponseEntity<?> createMembership(HttpServletRequest request, @RequestBody MembershipRequest membershipRequest , @PathVariable int gym_id) {
        Map<String, Object> res = new HashMap<>();
        Gym gym = gymRepository.findById(gym_id).orElseThrow(() -> new RuntimeException("Gym not found"));
        MembershipPlan plan = membershipPlanRepository.findById(membershipRequest.getPlanId())
                .orElseThrow(() -> new RuntimeException("Membership plan not found"));
        Member member = memberRepository.findById(membershipRequest.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Membership planMembership = membershipRepository.getByMemberAndGymAndActiveTrue(member, gym);
        if (planMembership != null) {
            res.put("error", "this user already has membership plan");
            return ResponseEntity.status(400).body(res);
        }

        Membership membership = new Membership();
        membership.setGym(gym);
        membership.setMembershipPlan(plan);
        membership.setUser(member);
        membership.setStartDate(LocalDateTime.now());
        membership.setEndDate(LocalDateTime.now().plusMonths(plan.getPlanDuration()));
        membership.setActive(true);

        Membership savedMembership = membershipRepository.save(membership);

        Claims claims = (Claims) request.getAttribute("sessionData");
        int user_id = (int) claims.get("user_id");

        Payment newpayment=new Payment();
        newpayment.setAmount(plan.getPrice());
        newpayment.setRefID(membershipRequest.refID);
        newpayment.setGym(gym);
        newpayment.setPaymentType(membershipRequest.paymentType);
        newpayment.setMembershipPlan(plan);
        newpayment.setCollectedByAdmin(adminRepository.findById(user_id).orElseThrow(() -> new RuntimeException("Admin not found")));
        newpayment.setCollectedOn(LocalDateTime.now());
        newpayment.setMember(member);
        newpayment.setMembership(membership);
        paymentRepository.save(newpayment);

        res.put("message", "Membership created successfully");
        res.put("membershipId", savedMembership.getId());
        return ResponseEntity.ok(res);
    }


    @GetMapping("/memberships")
    public ResponseEntity<?> getAllMemberships(@PathVariable int gym_id) {
        Gym gym = gymRepository.findById(gym_id).orElseThrow(() -> new RuntimeException("Gym not found"));
        List<Membership> memberships=membershipRepository.findByGym(gym);
        List<MembershipResponseDTO> response = memberships.stream().map(m -> new MembershipResponseDTO(
                m.getId(),
                m.getUser().getName(),
                m.getUser().getEmail(),
                m.getMembershipPlan().getPlanName(),
                m.getMembershipPlan().getPrice(),
                m.getStartDate(),
                m.getEndDate(),
                m.isActive(),
                m.getMembershipPlan().getId(),
                m.getMembershipPlan().getPlanDuration()
        )).toList();
        return ResponseEntity.ok(response);
    }

    @PutMapping("/membership/{membershipId}")
    public ResponseEntity<?> updateMembership(@PathVariable Integer membershipId, @RequestBody MembershipRequest membershipRequest) {
        Map<String, Object> res = new HashMap<>();

        Membership membership = membershipRepository.findById(membershipId)
                .orElseThrow(() -> new RuntimeException("Membership not found"));

        MembershipPlan plan = membershipPlanRepository.findById(membershipRequest.getPlanId())
                .orElseThrow(() -> new RuntimeException("Membership plan not found"));

        membership.setMembershipPlan(plan);
        membership.setEndDate(LocalDateTime.now().plusMonths(plan.getPlanDuration()));

        Membership updatedMembership = membershipRepository.save(membership);

        res.put("message", "Membership updated successfully");
        res.put("membershipId", updatedMembership.getId());
        return ResponseEntity.ok(res);
    }

    @PutMapping("/membership/{membershipId}/deactivate")
    public ResponseEntity<?> deactivateMembership(@PathVariable Integer membershipId) {
        Map<String, Object> res = new HashMap<>();
        Membership membership = membershipRepository.findById(membershipId)
                .orElseThrow(() -> new RuntimeException("Membership not found"));
        membership.setActive(false);
        membershipRepository.save(membership);
        res.put("message", "Membership deactivated successfully");
        return ResponseEntity.ok(res);
    }

    @DeleteMapping("/membership/{membershipId}")
    public ResponseEntity<?> deleteMembership(@PathVariable Integer membershipId) {
        Map<String, Object> res = new HashMap<>();

        Membership membership = membershipRepository.findById(membershipId)
                .orElseThrow(() -> new RuntimeException("Membership not found"));

        membershipRepository.delete(membership);

        res.put("message", "Membership deleted successfully");
        return ResponseEntity.ok(res);
    }

    public static class MembershipRequest {
        private Integer planId;
        private Integer userId;
        public PaymentType paymentType;
        public String refID;


        public Integer getPlanId() {
            return planId;
        }

        public void setPlanId(Integer planId) {
            this.planId = planId;
        }

        public Integer getUserId() {
            return userId;
        }

        public void setUserId(Integer userId) {
            this.userId = userId;
        }
    }
}
package com.coderstack.gymmatrix.controller;

import com.coderstack.gymmatrix.dto.MembershipResponseDTO;
import com.coderstack.gymmatrix.dto.MembershipRequest;
import com.coderstack.gymmatrix.enums.PlanStatus;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import com.coderstack.gymmatrix.models.*;
import com.coderstack.gymmatrix.repository.*;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin
@RequestMapping("/api/gym/{gym_id}/admin/{admin_id}")
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
    public ResponseEntity<?> createMembership(
            @PathVariable int admin_id, @RequestBody MembershipRequest membershipRequest, @PathVariable int gym_id) {

        Gym gym = gymRepository.findById(gym_id)
                .orElseThrow(() -> new RuntimeException("Gym not found"));
        MembershipPlan plan = membershipPlanRepository.findById(membershipRequest.getPlanId())
                .orElseThrow(() -> new RuntimeException("Membership plan not found"));
        Member member = memberRepository.findById(membershipRequest.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));


        LocalDate today = LocalDate.now();
        LocalDate startDate = membershipRequest.getStartDate() != null
                ? membershipRequest.getStartDate()
                : today;


        int countInfo = membershipRepository.findActiveOrUpcomingMembership(
                membershipRequest.getUserId(), gym_id, startDate);
        if (countInfo > 0) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM d, yyyy");
            String formattedDate = startDate.format(formatter);
            return ResponseEntity.status(400).body(Map.of(
                    "error", "This user already has an active or upcoming membership plan starting on " + formattedDate));
        }

        if (startDate.isBefore(today)) {
            return ResponseEntity.badRequest().body(Map.of(
                    "error", "Start date cannot be in the past"));
        }

        Membership membership = new Membership();
        membership.setGym(gym);
        membership.setMembershipPlan(plan);
        membership.setUser(member);
        membership.setStartDate(startDate);
        membership.setEndDate(startDate.plusMonths(plan.getPlanDuration()));
        membership.setStatus(startDate.equals(today) ? PlanStatus.ACTIVE : PlanStatus.UPCOMING);
        membership.setActive(true);

        Membership savedMembership = membershipRepository.save(membership);

        Admin collectedByAdmin = adminRepository.findById(admin_id)
                .orElseThrow(() -> new RuntimeException("Admin not found"));

        Payment payment = new Payment();
        payment.setAmount(plan.getPrice());
        payment.setRefID(membershipRequest.getRefID());
        payment.setGym(gym);
        payment.setMembershipPlan(plan);
        payment.setPaymentType(membershipRequest.getPaymentType());
        payment.setCollectedByAdmin(collectedByAdmin);
        payment.setCollectedOn(LocalDateTime.now());
        payment.setMember(member);
        payment.setMembership(savedMembership);
        paymentRepository.save(payment);

        return ResponseEntity.ok(Map.of(
                "message", "Membership created successfully",
                "membershipId", savedMembership.getId()
        ));
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
        membership.setEndDate(LocalDate.now().plusMonths(plan.getPlanDuration()));

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
}
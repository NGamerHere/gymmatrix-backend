package com.coderstack.gymmatrix.controller;

import com.coderstack.gymmatrix.models.Gym;
import com.coderstack.gymmatrix.models.Member;
import com.coderstack.gymmatrix.models.Membership;
import com.coderstack.gymmatrix.models.MembershipPlan;
import com.coderstack.gymmatrix.repository.GymRepository;
import com.coderstack.gymmatrix.repository.MemberRepository;
import com.coderstack.gymmatrix.repository.MembershipPlanRepository;
import com.coderstack.gymmatrix.repository.MembershipRepository;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/dashboard")
public class MembershipController {

    @Autowired
    private MembershipRepository membershipRepository;

    @Autowired
    private GymRepository gymRepository;

    @Autowired
    private MembershipPlanRepository membershipPlanRepository;

    @Autowired
    private MemberRepository memberRepository;


    @PostMapping("/membership")
    public ResponseEntity<?> createMembership(HttpServletRequest request, @RequestBody MembershipRequest membershipRequest) {
        Map<String, Object> res = new HashMap<>();

        Claims claims = (Claims) request.getAttribute("sessionData");
        Integer gymId = (Integer) claims.get("gym_id");

        Gym gym = gymRepository.findById(gymId).orElseThrow(() -> new RuntimeException("Gym not found"));
        MembershipPlan plan = membershipPlanRepository.findById(membershipRequest.getPlanId())
                .orElseThrow(() -> new RuntimeException("Membership plan not found"));
        Member member = memberRepository.findById(membershipRequest.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Membership membership = new Membership();
        membership.setGym(gym);
        membership.setMembershipPlan(plan);
        membership.setUser(member);
        membership.setStartDate(LocalDateTime.now());
        membership.setEndDate(LocalDateTime.now().plusMonths(plan.getPlanDuration()));
        membership.setActive(true);

        Membership savedMembership = membershipRepository.save(membership);

        res.put("message", "Membership created successfully");
        res.put("membershipId", savedMembership.getId());
        return ResponseEntity.ok(res);
    }


    @GetMapping("/memberships")
    public ResponseEntity<?> getAllMemberships(HttpServletRequest request) {
        Claims claims = (Claims) request.getAttribute("sessionData");
        Integer gymId = (Integer) claims.get("gym_id");

        Gym gym = gymRepository.findById(gymId).orElseThrow(() -> new RuntimeException("Gym not found"));
        return ResponseEntity.ok(membershipRepository.findByGym(gym));
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

        Membership updatedMembership = membershipRepository.save(membership);

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
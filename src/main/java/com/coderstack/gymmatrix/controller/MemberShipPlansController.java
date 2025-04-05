package com.coderstack.gymmatrix.controller;


import com.coderstack.gymmatrix.dto.NewMembershipPlan;
import com.coderstack.gymmatrix.exceptions.ResourceNotFoundException;
import com.coderstack.gymmatrix.models.Gym;
import com.coderstack.gymmatrix.models.MembershipPlan;
import com.coderstack.gymmatrix.repository.GymRepository;
import com.coderstack.gymmatrix.repository.MembershipPlanRepository;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.time.LocalDateTime;
import java.util.Optional;

@RestController
@RequestMapping("/api/dashboard")
public class MemberShipPlansController {

    @Autowired
    private MembershipPlanRepository membershipPlanRepository;

    @Autowired
    private GymRepository gymRepository;

    @GetMapping("/plan")
    public List<MembershipPlan> getAllPlans(HttpServletRequest request){
        Claims claims = (Claims) request.getAttribute("sessionData");
        Integer gymId = (Integer) claims.get("gym_id");
        Gym gym = gymRepository.findById(gymId).orElseThrow(() -> new ResourceNotFoundException("Gym not found"));
        return membershipPlanRepository.getAllByGym(gym);
    }

    @PutMapping("/plan/{planId}")
    public ResponseEntity<MembershipPlan> updatePlan(HttpServletRequest request,@PathVariable Integer planId, @RequestBody NewMembershipPlan newMembershipPlan){
        Claims claims = (Claims) request.getAttribute("sessionData");
        Integer gymId = (Integer) claims.get("gym_id");
        Gym gym = gymRepository.findById(gymId).orElseThrow(() -> new ResourceNotFoundException("Gym not found"));

        MembershipPlan existingPlan = membershipPlanRepository.findById(planId).orElseThrow(() -> new ResourceNotFoundException("Membership plan not found"));
        existingPlan.setPlanDuration(newMembershipPlan.plan_duration);
        existingPlan.setPrice(newMembershipPlan.price);
        existingPlan.setPlanName(newMembershipPlan.plan_name);
        existingPlan.setUpdatedAt(LocalDateTime.now());
        membershipPlanRepository.save(existingPlan);
        return ResponseEntity.ok(existingPlan);
    }

    @DeleteMapping("/plan/{planId}")
    public ResponseEntity<?> deletePlan(HttpServletRequest request, @PathVariable Integer planId) {
        Map<String, Object> res = new HashMap<>();
        Claims claims = (Claims) request.getAttribute("sessionData");
        Integer gymId = (Integer) claims.get("gym_id");
        Gym gym = gymRepository.findById(gymId).orElseThrow(() -> new ResourceNotFoundException("Gym not found"));
        MembershipPlan planToDelete = membershipPlanRepository.findById(planId)
                .orElseThrow(() -> new ResourceNotFoundException("Membership plan not found"));

        if (planToDelete.getGym().getId() != gym.getId()) {
            res.put("error", "You cannot delete a plan that doesn't belong to your gym");
            return ResponseEntity.status(403).body(res);
        }
        membershipPlanRepository.delete(planToDelete);
        res.put("message", "Plan deleted successfully");
        return ResponseEntity.ok(res);
    }


    @PostMapping("/plan")
    public ResponseEntity<?> addingNewPlan(HttpServletRequest request, @RequestBody NewMembershipPlan newMembershipPlan){
        Map<String,Object> res=new HashMap<>();
        Claims claims = (Claims) request.getAttribute("sessionData");
        Optional<Gym> optionalGYM=gymRepository.findById((Integer) claims.get("gym_id"));
        if(optionalGYM.isEmpty()){
            res.put("error","gym not found");
            return ResponseEntity.status(404).body(res);
        }
        Gym gym=optionalGYM.get();
        MembershipPlan membershipPlan=new MembershipPlan();
        membershipPlan.setPlanName(newMembershipPlan.plan_name);
        membershipPlan.setPlanDuration(newMembershipPlan.plan_duration);
        membershipPlan.setPrice(newMembershipPlan.price);
        membershipPlan.setGym(gym);
        membershipPlan.setCreatedAt(LocalDateTime.now());
        membershipPlan.setUpdatedAt(LocalDateTime.now());
        MembershipPlan savedPlan=membershipPlanRepository.save(membershipPlan);
        res.put("message","plan saved successfully");
        res.put("plan_id",savedPlan.getId());
        return ResponseEntity.ok(res);
    }
}

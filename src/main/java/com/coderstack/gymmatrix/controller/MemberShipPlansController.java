package com.coderstack.gymmatrix.controller;


import com.coderstack.gymmatrix.dto.NewMembershipPlan;
import com.coderstack.gymmatrix.exceptions.ResourceNotFoundException;
import com.coderstack.gymmatrix.models.Gym;
import com.coderstack.gymmatrix.models.MembershipPlan;
import com.coderstack.gymmatrix.repository.GymRepository;
import com.coderstack.gymmatrix.repository.MembershipPlanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.time.LocalDateTime;
import java.util.Optional;

@RestController
@CrossOrigin
@RequestMapping("/api/gym/{gym_id}/admin/{admin_id}")
public class MemberShipPlansController {

    @Autowired
    private MembershipPlanRepository membershipPlanRepository;

    @Autowired
    private GymRepository gymRepository;

    @GetMapping("/plan")
    public List<MembershipPlan> getAllPlans(@PathVariable int gym_id) {
        Gym gym = gymRepository.findById(gym_id).orElseThrow(() -> new ResourceNotFoundException("Gym not found"));
        return membershipPlanRepository.getAllByGym(gym);
    }

    @PutMapping("/plan/{planId}")
    public ResponseEntity<MembershipPlan> updatePlan(@PathVariable int gym_id,@PathVariable Integer planId, @RequestBody NewMembershipPlan newMembershipPlan){
        gymRepository.findById(gym_id).orElseThrow(() -> new ResourceNotFoundException("Gym not found"));
        MembershipPlan existingPlan = membershipPlanRepository.findById(planId).orElseThrow(() -> new ResourceNotFoundException("Membership plan not found"));
        existingPlan.setPlanDuration(newMembershipPlan.plan_duration);
        existingPlan.setPrice(newMembershipPlan.price);
        existingPlan.setPlanName(newMembershipPlan.plan_name);
        existingPlan.setUpdatedAt(LocalDateTime.now());
        membershipPlanRepository.save(existingPlan);
        return ResponseEntity.ok(existingPlan);
    }

    @DeleteMapping("/plan/{planId}")
    public ResponseEntity<?> deletePlan(@PathVariable int gym_id, @PathVariable Integer planId) {
        Map<String, Object> res = new HashMap<>();
        Gym gym = gymRepository.findById(gym_id).orElseThrow(() -> new ResourceNotFoundException("Gym not found"));
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
    public ResponseEntity<?> addingNewPlan(@PathVariable int gym_id, @RequestBody NewMembershipPlan newMembershipPlan){
        Map<String, Object> res = new HashMap<>();
        Optional<Gym> optionalGYM=gymRepository.findById(gym_id);
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

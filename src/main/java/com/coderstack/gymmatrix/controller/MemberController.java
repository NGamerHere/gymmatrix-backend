package com.coderstack.gymmatrix.controller;

import com.coderstack.gymmatrix.models.Gym;
import com.coderstack.gymmatrix.models.Member;
import com.coderstack.gymmatrix.repository.GymRepository;
import com.coderstack.gymmatrix.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/admin/{gym_id}")
public class MemberController {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private GymRepository gymRepository;

    @PostMapping("/member")
    public ResponseEntity<?> addNewUser(@PathVariable int gym_id, @RequestBody Member newMember){
        Map<String,String> res=new HashMap<>();
        Optional<Gym> gym=gymRepository.findById(gym_id);
        if (gym.isEmpty()){
            res.put("error","gym not found");
            return ResponseEntity.status(404).body(res);
        }
        Optional<Member> existing = memberRepository.findByPhoneOrEmail(newMember.getPhone(), newMember.getEmail());
        if (existing.isPresent()) {
            Member existingMember = existing.get();
            if (existingMember.getPhone().equals(newMember.getPhone())) {
                res.put("error", "Phone number already exists");
            } else {
                res.put("error", "Email already exists");
            }
            return ResponseEntity.status(409).body(res);
        }
        newMember.setGym(gym.get());
        memberRepository.save(newMember);
        res.put("message","new member saved successfully");
        return ResponseEntity.ok(res);
    }
}

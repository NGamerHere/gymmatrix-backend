package com.coderstack.gymmatrix.service;


import com.coderstack.gymmatrix.enums.UserType;
import com.coderstack.gymmatrix.models.Admin;
import com.coderstack.gymmatrix.models.Member;
import com.coderstack.gymmatrix.models.Trainer;
import com.coderstack.gymmatrix.repository.MemberRepository;
import com.coderstack.gymmatrix.repository.TrainerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class DuplicateCheckService {
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private TrainerRepository trainerRepository;

    public Map<String, String> checkDuplicate(int gymId, String phone, String email, UserType userType) {
        Map<String, String> errors = new HashMap<>();
        if(userType == UserType.member){
            List<Member> members = memberRepository.findDuplicates(gymId, phone, email);
            if (members.stream().anyMatch(m -> m.getPhone().equals(phone))) {
                errors.put("phone", "Phone number already exists");
            }
            if (members.stream().anyMatch(m -> m.getEmail().equals(email))) {
                errors.put("email", "Email already exists");
            }
            return errors;
        }else if(userType == UserType.trainer) {
            List<Trainer> trainers = trainerRepository.findDuplicates(gymId, phone, email);
            if (trainers.stream().anyMatch(m -> m.getPhone().equals(phone))) {
                errors.put("phone", "Phone number already exists");
            }
            if (trainers.stream().anyMatch(m -> m.getEmail().equals(email))) {
                errors.put("email", "Email already exists");
            }
            return errors;
        }else {
            return null;
        }
    }
}


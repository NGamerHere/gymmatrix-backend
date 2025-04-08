package com.coderstack.gymmatrix.service;


import com.coderstack.gymmatrix.models.Member;
import com.coderstack.gymmatrix.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class DuplicateCheckService {
    @Autowired
    private MemberRepository memberRepository;

    public Map<String, String> checkDuplicate(int gymId, String phone, String email) {
        List<Member> members = memberRepository.findDuplicates(gymId, phone, email);
        Map<String, String> errors = new HashMap<>();
        if (members.stream().anyMatch(m -> m.getPhone().equals(phone))) {
            errors.put("phone", "Phone number already exists");
        }
        if (members.stream().anyMatch(m -> m.getEmail().equals(email))) {
            errors.put("email", "Email already exists");
        }
        return errors;
    }
}


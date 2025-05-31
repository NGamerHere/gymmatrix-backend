package com.coderstack.gymmatrix.service;


import com.coderstack.gymmatrix.enums.UserType;
import com.coderstack.gymmatrix.models.User;
import com.coderstack.gymmatrix.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class DuplicateCheckService {

    @Autowired
    private UserRepository userRepository;

    public Map<String, String> checkDuplicate(int gymId, String phone, String email, UserType userType) {
        Map<String, String> errors = new HashMap<>();

        List<User> users=userRepository.findDuplicates(gymId, phone, email);
        if (users.stream().anyMatch(m -> m.getPhone().equals(phone))) {
            errors.put("phone", "Phone number already exists");
        }
        if (users.stream().anyMatch(m -> m.getEmail().equals(email))) {
            errors.put("email", "Email already exists");
        }
        return errors;
    }
}


package com.coderstack.gymmatrix.controller;

import com.coderstack.gymmatrix.dto.NewGym;
import com.coderstack.gymmatrix.models.Admin;
import com.coderstack.gymmatrix.models.Gym;
import com.coderstack.gymmatrix.enums.UserType;
import com.coderstack.gymmatrix.models.User;
import com.coderstack.gymmatrix.repository.AdminRepository;
import com.coderstack.gymmatrix.repository.GymRepository;
import com.coderstack.gymmatrix.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@CrossOrigin
@RequestMapping("/api")
public class GymController {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private GymRepository gymRepository;

    @PostMapping("/new-gym-add")
    public ResponseEntity<?> add(@RequestBody NewGym newGym) {
        Gym gym = new Gym();
        gym.setName(newGym.gym_name);
        gym.setDescription(newGym.gym_description);
        gym.setAddress(newGym.address);
        gym.setPhone(newGym.phone);
        gym.setCity(newGym.city);
        gym.setState(newGym.state);
        gym.setCountry(newGym.country);
        gym.setZip(newGym.zip);
        gym.setEmail(newGym.email);
        Gym savedGym=gymRepository.save(gym);

        User user = getUser(newGym, savedGym);
        userRepository.save(user);
        return ResponseEntity.ok("Gym and user added successfully");
    }
    private static User getUser(NewGym newGym, Gym savedGym) {
        User user = new User();
        user.setName(newGym.name);
        user.setPassword(newGym.password);
        user.setEmail(newGym.email);
        user.setAge(newGym.age);
        user.setGender(newGym.gender);
        user.setAddress(newGym.address);
        user.setPhone(newGym.phone);
        user.setCity(newGym.city);
        user.setState(newGym.state);
        user.setCountry(newGym.country);
        user.setZip(newGym.zip);
        user.setUserType(UserType.admin);
        user.setGym(savedGym);
        return user;
    }
}

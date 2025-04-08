package com.coderstack.gymmatrix.controller;

import com.coderstack.gymmatrix.dto.NewGym;
import com.coderstack.gymmatrix.models.Admin;
import com.coderstack.gymmatrix.models.Gym;
import com.coderstack.gymmatrix.enums.UserType;
import com.coderstack.gymmatrix.repository.AdminRepository;
import com.coderstack.gymmatrix.repository.GymRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/api/gym")
public class GymController {

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private GymRepository gymRepository;

    @PostMapping("/add")
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

        Admin admin = getAdmin(newGym, savedGym);
        adminRepository.save(admin);
        return ResponseEntity.ok("Gym and admin added successfully");
    }
    private static Admin getAdmin(NewGym newGym, Gym savedGym) {
        Admin admin = new Admin();
        admin.setName(newGym.name);
        admin.setPassword(newGym.password);
        admin.setEmail(newGym.email);
        admin.setAge(newGym.age);
        admin.setGender(newGym.gender);
        admin.setAddress(newGym.address);
        admin.setPhone(newGym.phone);
        admin.setCity(newGym.city);
        admin.setState(newGym.state);
        admin.setCountry(newGym.country);
        admin.setZip(newGym.zip);
        admin.setUserType(UserType.admin);
        admin.setGym(savedGym);
        return admin;
    }
}

package com.coderstack.gymmatrix.controller;

import com.coderstack.gymmatrix.dto.NewTrainer;
import com.coderstack.gymmatrix.enums.UserType;
import com.coderstack.gymmatrix.exceptions.ResourceNotFoundException;
import com.coderstack.gymmatrix.models.User;
import com.coderstack.gymmatrix.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("/api/gym/{gym_id}/trainer/{trainer_id}")
public class TrainerController {
    @Autowired
    private UserRepository userRepository;

    @GetMapping("/info")
    public User getTrainer(@PathVariable("trainer_id") int trainer_id, @PathVariable("gym_id") long gym_id) {
        return userRepository.findById(trainer_id).orElseThrow( () -> new ResourceNotFoundException("Trainer not found with id: " + trainer_id));
    }

    @PutMapping("/info")
    public User editTrainer(@PathVariable("trainer_id") int trainer_id, @RequestBody NewTrainer trainer, @PathVariable String gym_id) {
        User dbTrainer=userRepository.findUserByIdAndUserType(trainer_id, UserType.trainer).orElseThrow( () -> new ResourceNotFoundException("Trainer not found with id: " + trainer_id) );
        dbTrainer.setName(trainer.name);
        dbTrainer.setEmail(trainer.email);
        dbTrainer.setAge(trainer.age);
        dbTrainer.setGender(trainer.gender);
        dbTrainer.setAddress(trainer.address);
        dbTrainer.setPhone(trainer.phone);
        dbTrainer.setState(trainer.state);
        dbTrainer.setCountry(trainer.country);
        dbTrainer.setCity(trainer.city);
        dbTrainer.setZip(trainer.zip);
        return userRepository.save(dbTrainer);
    }

}

package com.coderstack.gymmatrix.controller;

import com.coderstack.gymmatrix.exceptions.ResourceNotFoundException;
import com.coderstack.gymmatrix.models.Trainer;
import com.coderstack.gymmatrix.repository.TrainerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("/api/gym/{gym_id}/trainer/{trainer_id}")
public class TrainerController {

    @Autowired
    private TrainerRepository trainerRepository;

    @GetMapping("/info")
    public Trainer getTrainer(@PathVariable("trainer_id") int trainer_id, @PathVariable("gym_id") long gym_id) {
        return trainerRepository.findById(trainer_id).orElseThrow( () -> new ResourceNotFoundException("Trainer not found with id: " + trainer_id) );
    }

    @PutMapping("/info")
    public Trainer editTrainer(@PathVariable("trainer_id") int trainer_id, @RequestBody Trainer trainer, @PathVariable String gym_id) {
        Trainer dbTrainer=trainerRepository.findById(trainer_id).orElseThrow( () -> new ResourceNotFoundException("Trainer not found with id: " + trainer_id) );
        dbTrainer.setName(trainer.getName());
        dbTrainer.setEmail(trainer.getEmail());
        dbTrainer.setAge(trainer.getAge());
        dbTrainer.setGender(trainer.getGender());
        dbTrainer.setAddress(trainer.getAddress());
        dbTrainer.setPhone(trainer.getPhone());
        dbTrainer.setState(trainer.getState());
        dbTrainer.setCountry(trainer.getCountry());
        dbTrainer.setCity(trainer.getCity());
        dbTrainer.setZip(trainer.getZip());
        return trainerRepository.save(dbTrainer);
    }

}

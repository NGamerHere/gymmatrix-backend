package com.coderstack.gymmatrix.controller;

import com.coderstack.gymmatrix.dto.Note;
import com.coderstack.gymmatrix.enums.UserType;
import com.coderstack.gymmatrix.exceptions.ResourceNotFoundException;
import com.coderstack.gymmatrix.models.DeviceToken;
import com.coderstack.gymmatrix.models.Gym;
import com.coderstack.gymmatrix.models.User;
import com.coderstack.gymmatrix.repository.DeviceTokenRepository;
import com.coderstack.gymmatrix.repository.GymRepository;
import com.coderstack.gymmatrix.repository.UserRepository;
import com.coderstack.gymmatrix.service.FirebaseMessagingService;
import com.google.firebase.messaging.FirebaseMessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Map;

@RestController
@CrossOrigin
@RequestMapping("/api/gym/{gym_id}/{role}/{role_id}")
public class NotificationController {

    @Autowired
    private FirebaseMessagingService firebaseMessagingService;

    @Autowired
    private GymRepository gymRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private DeviceTokenRepository deviceTokenRepository;

    // Register device token
    @PostMapping("/notification")
    public ResponseEntity<?> registerPhone(
            @PathVariable int gym_id,
            @PathVariable UserType role,
            @PathVariable int role_id,
            @RequestBody Map<String, String> body
    ) {
        Gym gym = gymRepository.findById(gym_id)
                .orElseThrow(() -> new ResourceNotFoundException("Gym not found"));

        User user = userRepository.findByIdAndUserType(role_id, role)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        String token = body.get("token");

        DeviceToken deviceToken = new DeviceToken();
        deviceToken.setToken(token);
        deviceToken.setUser(user);
        deviceToken.setGym(gym);
        LocalDateTime now = LocalDateTime.now();
        deviceToken.setAddedOn(now);
        deviceToken.setLastAccessedOn(now);

        deviceTokenRepository.save(deviceToken);
        return ResponseEntity.ok(Map.of("status", "token saved"));
    }

    @PostMapping("/send-notification")
    public ResponseEntity<?> sendNotification(
            @PathVariable int gym_id,
            @PathVariable UserType role,
            @PathVariable int role_id,
            @RequestBody Map<String, String> body
    ) throws FirebaseMessagingException {

        int userId = Integer.parseInt(body.get("user_id"));

        Gym gym = gymRepository.findById(gym_id)
                .orElseThrow(() -> new ResourceNotFoundException("Gym not found"));

        User user = userRepository.findByIdAndUserType(userId, role)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));


        DeviceToken deviceToken = deviceTokenRepository.findTopByUserOrderByAddedOnDesc(user)
                .orElseThrow(() -> new ResourceNotFoundException("Device token not found"));


        Note note = new Note();
        note.setSubject(body.get("subject"));
        note.setContent(body.get("content"));
        note.setData(Map.of("user", user.getName()));

        firebaseMessagingService.sendNotification(note, deviceToken.getToken());

        deviceToken.setLastAccessedOn(LocalDateTime.now());
        deviceTokenRepository.save(deviceToken);

        return ResponseEntity.ok(Map.of("status", "notification sent"));
    }

    @DeleteMapping("/notification")
    public ResponseEntity<?> deleteDeviceToken(
            @PathVariable int gym_id,
            @PathVariable UserType role,
            @PathVariable int role_id,
            @RequestBody Map<String, String> body
    ) {
        String token = body.get("token");

        deviceTokenRepository
                .findByToken(token)
                .ifPresent(deviceTokenRepository::delete);

        return ResponseEntity.ok(Map.of("status", "token deleted"));
    }




}

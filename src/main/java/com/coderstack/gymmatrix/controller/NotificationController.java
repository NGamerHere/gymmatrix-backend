package com.coderstack.gymmatrix.controller;

import com.coderstack.gymmatrix.enums.UserType;
import com.coderstack.gymmatrix.service.FcmPushService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@CrossOrigin
@RequestMapping("/api/gym/{gym_id}/{role}/{role_id}")
public class NotificationController {

    @Autowired
    FcmPushService fcmPushService;


    @PostMapping("/notification")
    public ResponseEntity<?> registorPhone(
            @PathVariable int gym_id,
            @PathVariable UserType role,
            @PathVariable int role_id,
            @RequestBody String token
    ) {
        fcmPushService.sendPush(token, "hello there", "this notification is working");
        System.out.println("sending the notification to "+token);
        return ResponseEntity.ok(Map.of("status", "ok"));
    }

}

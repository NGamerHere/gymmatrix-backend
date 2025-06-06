package com.coderstack.gymmatrix.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
public class FcmPushService {

    private final String FCM_URL = "https://fcm.googleapis.com/fcm/send";

    private final String SERVER_KEY ;

    private RestTemplate restTemplate;

    public FcmPushService( @Value("${firebase.comm.key}") String serverKey) {
        restTemplate=new RestTemplate();
        SERVER_KEY = serverKey;
    }

    public void sendPush(String token, String title, String body) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(SERVER_KEY);

        Map<String, Object> notification = new HashMap<>();
        notification.put("title", title);
        notification.put("body", body);

        Map<String, Object> message = new HashMap<>();
        message.put("to", token);
        message.put("notification", notification);

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(message, headers);

        ResponseEntity<String> response = restTemplate.postForEntity(FCM_URL, entity, String.class);
        System.out.println("FCM response: " + response.getBody());
    }
}

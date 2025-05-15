package com.coderstack.gymmatrix.service;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class RespoanceService {
    public static ResponseEntity<Map<String, String>> sendSuccessResponse(String message) {
        Map<String, String> res = new HashMap<>();
        res.put("message", message);
        return ResponseEntity.ok(res);
    }
    public static ResponseEntity<Map<String, String>> sendSuccessResponse(String message, int id) {
        Map<String, String> res = new HashMap<>();
        res.put("message", message);
        res.put("id", String.valueOf(id));

        return ResponseEntity.ok(res);
    }
    public static ResponseEntity<Map<String, String>> sendErrorResponse(String error, int status) {
        Map<String, String> res = new HashMap<>();
        res.put("error", error);
        return ResponseEntity.status(status).body(res);
    }
}

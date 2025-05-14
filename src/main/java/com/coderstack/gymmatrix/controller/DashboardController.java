package com.coderstack.gymmatrix.controller;

import com.coderstack.gymmatrix.service.AdminStatsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.HashMap;
import java.util.Map;

@Controller
@CrossOrigin
public class DashboardController {

    @Autowired
    AdminStatsService adminStatsService;

    @GetMapping("/api/admin/{gym_id}/dashboard")
    public ResponseEntity<Map<String, Object>> getStats(@PathVariable Long gym_id) {
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalMembers", adminStatsService.getTotalMembers(gym_id));
        stats.put("totalActiveMembers", adminStatsService.getTotalActiveMembers(gym_id));
        stats.put("totalRevenue", adminStatsService.getTotalRevenue(gym_id));
        stats.put("memberInfo", adminStatsService.getMembers(gym_id));
        return ResponseEntity.ok(stats);
    }
}

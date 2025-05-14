package com.coderstack.gymmatrix.service;

import com.coderstack.gymmatrix.dto.MemberProjection;
import com.coderstack.gymmatrix.repository.AdminStatsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminStatsService {

    @Autowired
    private AdminStatsRepository adminStatsRepository;

    public int getTotalMembers(Long gymId) {
        return adminStatsRepository.getTotalMemberCount(gymId);
    }

    public int getTotalActiveMembers(Long gymId) {
        return adminStatsRepository.getTotalActiveMembers(gymId);
    }

    public double getTotalRevenue(Long gymId) {
        Double revenue = adminStatsRepository.getTotalRevenue(gymId);
        return revenue != null ? revenue : 0.0;
    }

    public List<MemberProjection> getMembers(Long gymId) {
        return adminStatsRepository.getMember(gymId);
    }


}

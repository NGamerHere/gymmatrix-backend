package com.coderstack.gymmatrix.schedulingtasks;

import com.coderstack.gymmatrix.models.Membership;
import com.coderstack.gymmatrix.repository.MembershipRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UpdatingStatusApplication {

    @Autowired
    private MembershipRepository membershipRepository;

    @Scheduled(fixedRate = 50000)
    public void reportCurrentTime() {
        System.out.println("Schedular was started");
    }
}

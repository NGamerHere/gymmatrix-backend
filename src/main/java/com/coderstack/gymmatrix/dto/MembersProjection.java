package com.coderstack.gymmatrix.dto;

import com.coderstack.gymmatrix.enums.PlanStatus;

import java.time.LocalDate;

public interface MembersProjection {
    int getId();
    String getEmail();
    String getName();
    String getPhone();
    Integer getMembershipId();
    PlanStatus getStatus();
    LocalDate getStartDate();
    LocalDate getEndDate();
    Integer getPlanId();
    String getPlanName();
    Integer getPlanDuration();
    Integer getPrice();
}

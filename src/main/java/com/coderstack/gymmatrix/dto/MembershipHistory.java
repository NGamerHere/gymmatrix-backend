package com.coderstack.gymmatrix.dto;

import com.coderstack.gymmatrix.enums.PlanStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;

public interface MembershipHistory {
    Double getAmount();
    String getPaymentType();
    LocalDateTime getPaymentDoneOn();
    String getPlanName();
    Boolean getActive();
    LocalDate getStartDate();
    LocalDate getEndDate();
    String getCollectedBy();
    PlanStatus getStatus();
}
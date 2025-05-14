package com.coderstack.gymmatrix.dto;

import java.time.LocalDateTime;

public interface MembershipHistory {
    Double getAmount();
    String getPaymentType();
    LocalDateTime getPaymentDoneOn();
    String getPlanName();
    Boolean getActive();
    LocalDateTime getStartDate();
    LocalDateTime getEndDate();
    String getCollectedBy();
}
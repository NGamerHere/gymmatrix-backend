package com.coderstack.gymmatrix.dto;

import com.coderstack.gymmatrix.enums.PlanStatus;

public interface MemberProjection {
    String getName();
    String getPlanName();
    Integer getPlanDuration();
    Integer getMemberId();
    String getEmail();
    PlanStatus getStatus();
}

package com.coderstack.gymmatrix.dto;

import com.coderstack.gymmatrix.enums.PlanStatus;

public interface MembersInfo {
    String getName();
    String getPhone();
    String getEmail();
    PlanStatus  getStatus();
    String getPlanName();
}

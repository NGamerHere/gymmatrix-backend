package com.coderstack.gymmatrix.dto;

import java.time.LocalDate;

public class MembershipResponseDTO {
    private int id;
    private String userName;
    private String userEmail;
    private String planName;
    private int planPrice;
    private LocalDate startDate;
    private LocalDate endDate;
    private Boolean active;
    private int planId;
    private int planDuration;

    public MembershipResponseDTO(int id, String userName, String userEmail,
                                 String planName, int planPrice,
                                 LocalDate startDate, LocalDate endDate,
                                 Boolean active, int planId, int planDuration) {
        this.id = id;
        this.userName = userName;
        this.userEmail = userEmail;
        this.planName = planName;
        this.planPrice = planPrice;
        this.startDate = startDate;
        this.endDate = endDate;
        this.active = active;
        this.planId = planId;
        this.planDuration = planDuration;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getPlanName() {
        return planName;
    }

    public void setPlanName(String planName) {
        this.planName = planName;
    }

    public int getPlanPrice() {
        return planPrice;
    }

    public void setPlanPrice(int planPrice) {
        this.planPrice = planPrice;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public int getPlanId() {
        return planId;
    }

    public void setPlanId(int planId) {
        this.planId = planId;
    }

    public int getPlanDuration() {
        return planDuration;
    }

    public void setPlanDuration(int planDuration) {
        this.planDuration = planDuration;
    }
}
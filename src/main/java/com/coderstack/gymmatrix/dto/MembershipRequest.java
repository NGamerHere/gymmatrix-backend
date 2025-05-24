package com.coderstack.gymmatrix.dto;

import com.coderstack.gymmatrix.enums.PaymentType;

import java.time.LocalDate;

public  class MembershipRequest {
    private Integer planId;
    private Integer userId;
    public PaymentType paymentType;
    public String refID;
    public LocalDate startDate;

    public LocalDate getStartDate(){
        return startDate;
    }

    public void setStartDate(LocalDate startDate){
        this.startDate = startDate;
    }

    public String getRefID() {
        return refID;
    }
    public void setRefID(String refID) {
        this.refID = refID;
    }

    public PaymentType getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(PaymentType paymentType) {
        this.paymentType = paymentType;
    }

    public Integer getPlanId() {
        return planId;
    }

    public void setPlanId(Integer planId) {
        this.planId = planId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }
}

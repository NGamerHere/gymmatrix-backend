package com.coderstack.gymmatrix.models;

import com.coderstack.gymmatrix.enums.PaymentType;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "payments")
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private int amount;

    @ManyToOne
    @JoinColumn(name = "gym_id")
    private Gym gym;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne
    @JoinColumn(name = "collected_by_admin_id")
    private Admin collectedByAdmin;

    @Enumerated(EnumType.STRING)
    private PaymentType paymentType;

    private String refID;
    private LocalDateTime collectedOn;

    @ManyToOne
    @JoinColumn(name = "membership_plan_id")
    private MembershipPlan membershipPlan;

    @ManyToOne
    @JoinColumn(name = "membership_id")
    private  Membership membership;

    public Membership getMembership() {
        return membership;
    }

    public void setMembership(Membership membership) {
        this.membership = membership;
    }

    public MembershipPlan getMembershipPlan() {
        return membershipPlan;
    }

    public void setMembershipPlan(MembershipPlan membershipPlan) {
        this.membershipPlan = membershipPlan;
    }

    public String getRefID(){
        return refID;
    }

    public void setRefID(String refID){
        this.refID=refID;
    }

    public LocalDateTime getCollectedOn(){
        return collectedOn;
    }

    public void setCollectedOn(LocalDateTime collectedOn){
        this.collectedOn=collectedOn;
    }

    public PaymentType getPaymentType(){
        return paymentType;
    }

    public void setPaymentType(PaymentType paymentType){
        this.paymentType=paymentType;
    }

    public Admin getCollectedByAdmin() {
        return collectedByAdmin;
    }

    public void setCollectedByAdmin(Admin collectedByAdmin) {
        this.collectedByAdmin = collectedByAdmin;
    }

    public int getId(){
        return id;
    }

    public void setId(int id){
        this.id=id;
    }

    public int getAmount(){
        return amount;
    }

    public void setAmount(int amount){
        this.amount=amount;
    }

    public Member getMember() {
        return member;
    }

    public void setMember(Member member) {
        this.member = member;
    }

    public Gym getGym() {
        return gym;
    }

    public void setGym(Gym gym) {
        this.gym = gym;
    }
}

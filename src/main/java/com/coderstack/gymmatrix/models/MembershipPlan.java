package com.coderstack.gymmatrix.models;

import jakarta.persistence.*;

@Entity
@Table(name = "membership_plans")
public class MembershipPlan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public int id;
    public String plan_name;
    public int plan_duration;
    @ManyToOne
    @JoinColumn(name = "gym_id",nullable = false)
    private Gym gym;

}

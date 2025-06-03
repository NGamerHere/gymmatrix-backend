package com.coderstack.gymmatrix.models;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "member_workout_assignments")
public class MemberWorkoutAssignment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private LocalDate assignedDate;

    @ManyToOne
    @JoinColumn(name = "member_id", nullable = false)
    private User member;

    @ManyToOne
    @JoinColumn(name = "routine_id", nullable = false)
    private WorkoutRoutine routine;

    private String customNotes;

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LocalDate getAssignedDate() {
        return assignedDate;
    }

    public void setAssignedDate(LocalDate assignedDate) {
        this.assignedDate = assignedDate;
    }

    public User getMember() {
        return member;
    }

    public void setMember(User member) {
        this.member = member;
    }

    public WorkoutRoutine getRoutine() {
        return routine;
    }

    public void setRoutine(WorkoutRoutine routine) {
        this.routine = routine;
    }

    public String getCustomNotes() {
        return customNotes;
    }

    public void setCustomNotes(String customNotes) {
        this.customNotes = customNotes;
    }
}
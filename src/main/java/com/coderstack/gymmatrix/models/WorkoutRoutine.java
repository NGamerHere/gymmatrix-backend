package com.coderstack.gymmatrix.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "workout_routines")
public class WorkoutRoutine {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String routineName;
    private String description;

    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "added_by",nullable = false)
    private User addedBy;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "gym_id", nullable = false)
    private Gym gym;

    @OneToMany(mappedBy = "routine", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<WorkoutExercise> exercises;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    public User getAddedBy(){
        return addedBy;
    }

    public void setAddedBy(User addedBy){
        this.addedBy=addedBy;
    }


    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRoutineName() {
        return routineName;
    }

    public void setRoutineName(String routineName) {
        this.routineName = routineName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public Gym getGym() {
        return gym;
    }

    public void setGym(Gym gym) {
        this.gym = gym;
    }

    public List<WorkoutExercise> getExercises() {
        return exercises;
    }

    public void setExercises(List<WorkoutExercise> exercises) {
        this.exercises = exercises;
    }
}
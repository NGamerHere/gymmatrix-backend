package com.coderstack.gymmatrix.dto;


import java.util.List;
import java.time.DayOfWeek;

public class NewWorkoutRoutines {
    private String routineName;
    private String description;

    private boolean isTemplate;
    private Integer memberId;
    private Integer routineId;
    private DayOfWeek dayOfWeek;

    public Integer getRoutineId() {
        return routineId;
    }

    public void setRoutineId(Integer routineId) {
        this.routineId = routineId;
    }

    public java.time.DayOfWeek getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(DayOfWeek dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public Integer getGetRoutineId() {
        return routineId;
    }

    public void setGetRoutineId(Integer routineId) {
        this.routineId = routineId;
    }

    public List<NewWorkoutExercises> getNewWorkoutRoutines() {
        return newWorkoutRoutines;
    }

    public void setNewWorkoutRoutines(List<NewWorkoutExercises> newWorkoutRoutines) {
        this.newWorkoutRoutines = newWorkoutRoutines;
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

    public boolean isTemplate() {
        return isTemplate;
    }

    public void setTemplate(boolean template) {
        isTemplate = template;
    }

    private List<NewWorkoutExercises> newWorkoutRoutines;

    public void setMemberId(Integer memberId) {
        this.memberId = memberId;
    }

    public Integer getMemberId(){
        return memberId;
    }

    public String getRoutineName() {
        return routineName;
    }
}
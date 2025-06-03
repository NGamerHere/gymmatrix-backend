package com.coderstack.gymmatrix.repository;

import com.coderstack.gymmatrix.models.WorkoutRoutine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WorkoutRoutineRepository extends JpaRepository<WorkoutRoutine,Integer> {
}

package com.coderstack.gymmatrix.repository;

import com.coderstack.gymmatrix.models.Gym;
import com.coderstack.gymmatrix.models.WorkoutRoutine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WorkoutRoutineRepository extends JpaRepository<WorkoutRoutine,Integer> {
    List<WorkoutRoutine> findByGym(Gym gym);

    List<WorkoutRoutine> findByGymAndIsTemplate(Gym gym, boolean isTemplate);
}

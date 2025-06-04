package com.coderstack.gymmatrix.controller;

import com.coderstack.gymmatrix.dto.NewWorkoutExercises;
import com.coderstack.gymmatrix.dto.NewWorkoutRoutines;
import com.coderstack.gymmatrix.enums.UserType;
import com.coderstack.gymmatrix.exceptions.ResourceNotFoundException;
import com.coderstack.gymmatrix.models.*;
import com.coderstack.gymmatrix.repository.EquipmentRepository;
import com.coderstack.gymmatrix.repository.GymRepository;
import com.coderstack.gymmatrix.repository.UserRepository;
import com.coderstack.gymmatrix.repository.WorkoutRoutineRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@CrossOrigin
@RequestMapping("/api")
public class WorkoutController {

    @Autowired
    private GymRepository gymRepository;

    @Autowired
    private WorkoutRoutineRepository workoutRoutineRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EquipmentRepository equipmentRepository;

    @PostMapping(value = "/gym/{gym_id}/{role}/{role_id}/workout-routine")
    public ResponseEntity<?> addNewWorkoutRoutine(@PathVariable int gym_id, @PathVariable UserType role, @PathVariable int role_id, @RequestBody NewWorkoutRoutines newWorkoutroutines){
        if(role == UserType.admin || role == UserType.trainer ){
            Gym gym=gymRepository.findById(gym_id).orElseThrow(()-> new ResourceNotFoundException("gym not found"));
            User user=userRepository.findUserByIdAndUserType(role_id,role).orElseThrow(()-> new ResourceNotFoundException("User not found"));
            WorkoutRoutine workoutRoutine=new WorkoutRoutine();
            workoutRoutine.setRoutineName(newWorkoutroutines.routineName);
            workoutRoutine.setDescription(newWorkoutroutines.description);
            workoutRoutine.setGym(gym);
            workoutRoutine.setAddedBy(user);
            WorkoutRoutine newworkoutRoutine=workoutRoutineRepository.save(workoutRoutine);
            return ResponseEntity.ok(Map.of("message","routine saved successfully","id",newworkoutRoutine.getId()));
        }else {
            return ResponseEntity.status(403).body(Map.of("error","you cant access this resource"));
        }

    }

    @PostMapping(value = "/gym/{gym_id}/{role}/{role_id}/workout-exercise/{workout_routine_id}")
    public ResponseEntity<?> addNewWorkoutExercise(@PathVariable int gym_id,
                                                   @PathVariable UserType role,
                                                   @PathVariable int role_id,
                                                   @PathVariable int workout_routine_id,
                                                   @RequestBody List<NewWorkoutExercises> newWorkoutRoutines) {
        if (role == UserType.admin || role == UserType.trainer) {
            Gym gym = gymRepository.findById(gym_id)
                    .orElseThrow(() -> new ResourceNotFoundException("gym not found"));
            User user = userRepository.findUserByIdAndUserType(role_id, role)
                    .orElseThrow(() -> new ResourceNotFoundException("User not found"));
            WorkoutRoutine workoutRoutine = workoutRoutineRepository.findById(workout_routine_id)
                    .orElseThrow(() -> new ResourceNotFoundException("workout routine not found"));

            List<WorkoutExercise> existingExercises = workoutRoutine.getExercises();
            existingExercises.clear();


            for (NewWorkoutExercises dto : newWorkoutRoutines) {
                WorkoutExercise exercise = new WorkoutExercise();
                exercise.setExerciseName(dto.exerciseName);
                exercise.setSets(dto.sets);
                exercise.setReps(dto.reps);
                exercise.setRestSeconds(dto.restSeconds);
                exercise.setOrderIndex(dto.orderIndex);
                exercise.setRoutine(workoutRoutine);

                Equipment equipment = equipmentRepository.findById(dto.equipmentId)
                        .orElseThrow(() -> new ResourceNotFoundException("Equipment not found"));
                exercise.setEquipment(equipment);

                existingExercises.add(exercise);
            }
            workoutRoutineRepository.save(workoutRoutine);

            return ResponseEntity.ok(Map.of("message", "routine saved successfully"));
        } else {
            return ResponseEntity.status(403).body(Map.of("error", "you can't access this resource"));
        }
    }

    @GetMapping(value = "/gym/{gym_id}/{role}/{role_id}/workout-exercise/{workout_routine_id}")
    public ResponseEntity<?> getWorkoutExercises(@PathVariable int gym_id,
                                                 @PathVariable UserType role,
                                                 @PathVariable int role_id,
                                                 @PathVariable int workout_routine_id) {
        if (role == UserType.admin || role == UserType.trainer) {
            // Validate gym
            Gym gym = gymRepository.findById(gym_id)
                    .orElseThrow(() -> new ResourceNotFoundException("Gym not found"));

            // Validate user
            User user = userRepository.findUserByIdAndUserType(role_id, role)
                    .orElseThrow(() -> new ResourceNotFoundException("User not found"));

            // Fetch workout routine
            WorkoutRoutine workoutRoutine = workoutRoutineRepository.findById(workout_routine_id)
                    .orElseThrow(() -> new ResourceNotFoundException("Workout routine not found"));


            List<Map<String, Object>> exerciseList = workoutRoutine.getExercises().stream().map(exercise -> {
                Map<String, Object> map = new HashMap<>();
                map.put("exerciseName", exercise.getExerciseName());
                map.put("sets", exercise.getSets());
                map.put("reps", exercise.getReps());
                map.put("restSeconds", exercise.getRestSeconds());
                map.put("orderIndex", exercise.getOrderIndex());
                map.put("equipmentId", exercise.getEquipment().getId());
                map.put("equipmentName", exercise.getEquipment().getEquipmentName());
                return map;
            }).collect(Collectors.toList());


            return ResponseEntity.ok(exerciseList);
        } else {
            return ResponseEntity.status(403).body(Map.of("error", "you can't access this resource"));
        }
    }

    @GetMapping("/gym/{gym_id}/{role}/{role_id}/workout-routines")
    public ResponseEntity<?> getAllWorkoutRoutinesByGym(@PathVariable int gym_id,
                                                        @PathVariable UserType role,
                                                        @PathVariable int role_id) {
        if (role == UserType.admin || role == UserType.trainer) {
            Gym gym = gymRepository.findById(gym_id)
                    .orElseThrow(() -> new ResourceNotFoundException("Gym not found"));
            User user = userRepository.findUserByIdAndUserType(role_id, role)
                    .orElseThrow(() -> new ResourceNotFoundException("User not found"));

            List<WorkoutRoutine> routines = workoutRoutineRepository.findByGym(gym);


            List<Map<String, Object>> result = routines.stream().map(routine -> Map.of(
                    "routineId", routine.getId(),
                    "routineName", routine.getRoutineName(),
                    "exercises", routine.getExercises().stream().map(exercise -> Map.of(
                            "exerciseName", exercise.getExerciseName(),
                            "sets", exercise.getSets(),
                            "reps", exercise.getReps(),
                            "restSeconds", exercise.getRestSeconds(),
                            "orderIndex", exercise.getOrderIndex(),
                            "equipmentId", exercise.getEquipment().getId(),
                            "equipmentName", exercise.getEquipment().getEquipmentName()
                    )).toList()
            )).toList();

            return ResponseEntity.ok(result);
        } else {
            return ResponseEntity.status(403).body(Map.of("error", "you can't access this resource"));
        }
    }





}

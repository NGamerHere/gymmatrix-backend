package com.coderstack.gymmatrix.controller;

import com.coderstack.gymmatrix.dto.NewMemberAssignment;
import com.coderstack.gymmatrix.dto.NewWorkoutExercises;
import com.coderstack.gymmatrix.dto.NewWorkoutRoutines;
import com.coderstack.gymmatrix.enums.UserType;
import com.coderstack.gymmatrix.exceptions.ResourceNotFoundException;
import com.coderstack.gymmatrix.models.*;
import com.coderstack.gymmatrix.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
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

    @Autowired
    private MemberWorkoutAssignmentRepository memberWorkoutAssignmentRepository;

    @PostMapping(value = "/gym/{gym_id}/{role}/{role_id}/workout-routine")
    public ResponseEntity<?> addNewWorkoutRoutine(@PathVariable int gym_id,
                                                  @PathVariable UserType role,
                                                  @PathVariable int role_id,
                                                  @RequestBody NewWorkoutRoutines newWorkoutroutines) {
        if (role == UserType.admin || role == UserType.trainer) {
            Gym gym = gymRepository.findById(gym_id)
                    .orElseThrow(() -> new ResourceNotFoundException("gym not found"));

            User user = userRepository.findUserByIdAndUserType(role_id, role)
                    .orElseThrow(() -> new ResourceNotFoundException("User not found"));

            if (!newWorkoutroutines.isTemplate()) {
                if (newWorkoutroutines.getMemberId() == 0 || newWorkoutroutines.getRoutineId() == null) {
                    return ResponseEntity.status(400).body(Map.of("error", "memberId and routineId are required"));
                }

                User member = userRepository.findUserByIdAndUserType(newWorkoutroutines.getMemberId(), UserType.member)
                        .orElseThrow(() -> new ResourceNotFoundException("Member not found"));

                if (role == UserType.admin && member.getTrainer().getId() != role_id) {
                    return ResponseEntity.status(401).body(Map.of("error", "you can't have access to assign the template to this user"));
                }

                Optional<WorkoutRoutine> opWorkoutRoutine = workoutRoutineRepository.findById(newWorkoutroutines.getRoutineId());
                if (opWorkoutRoutine.isEmpty()) {
                    return ResponseEntity.status(404).body(Map.of("error", "template routine not found"));
                }

                WorkoutRoutine templateRoutine = opWorkoutRoutine.get();
                WorkoutRoutine memberRoutine = new WorkoutRoutine();
                memberRoutine.setRoutineName(templateRoutine.getRoutineName());
                memberRoutine.setDescription(templateRoutine.getDescription());
                memberRoutine.setGym(gym);
                memberRoutine.setAddedBy(user);


                memberRoutine.setIsTemplate(false);

                WorkoutRoutine savedRoutine = workoutRoutineRepository.save(memberRoutine);
                MemberWorkoutAssignment newAssignment = new MemberWorkoutAssignment();
                newAssignment.setGym(gym);
                newAssignment.setMember(member);
                newAssignment.setRoutine(savedRoutine);
                newAssignment.setDayOfWeek(newWorkoutroutines.getDayOfWeek());
                memberWorkoutAssignmentRepository.save(newAssignment);

                return ResponseEntity.ok(Map.of("message", "routine assigned to member", "id", savedRoutine.getId()));
            } else {
                WorkoutRoutine workoutRoutine = new WorkoutRoutine();
                workoutRoutine.setRoutineName(newWorkoutroutines.getRoutineName());
                workoutRoutine.setDescription(newWorkoutroutines.getDescription());
                workoutRoutine.setGym(gym);
                workoutRoutine.setAddedBy(user);
                workoutRoutine.setIsTemplate(true);

                WorkoutRoutine savedRoutine = workoutRoutineRepository.save(workoutRoutine);
                return ResponseEntity.ok(Map.of("message", "routine template saved successfully", "id", savedRoutine.getId()));
            }
        } else {
            return ResponseEntity.status(403).body(Map.of("error", "you can't access this resource"));
        }
    }


    @PostMapping(value = "/gym/{gym_id}/{role}/{role_id}/workout-routine/{workout_routine_id}/workout-exercises")
    public ResponseEntity<?> addNewWorkoutExercise(@PathVariable int gym_id,
                                                   @PathVariable UserType role,
                                                   @PathVariable int role_id,
                                                   @PathVariable int workout_routine_id,
                                                   @RequestBody List<NewWorkoutExercises> newWorkoutExercises) {
        if (role == UserType.admin || role == UserType.trainer) {
            Gym gym = gymRepository.findById(gym_id)
                    .orElseThrow(() -> new ResourceNotFoundException("gym not found"));
            User user = userRepository.findUserByIdAndUserType(role_id, role)
                    .orElseThrow(() -> new ResourceNotFoundException("User not found"));
            WorkoutRoutine workoutRoutine = workoutRoutineRepository.findById(workout_routine_id)
                    .orElseThrow(() -> new ResourceNotFoundException("workout routine not found"));

            List<WorkoutExercise> existingExercises = workoutRoutine.getExercises();
            existingExercises.clear();


            for (NewWorkoutExercises dto : newWorkoutExercises) {
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


    @GetMapping(value = "/gym/{gym_id}/{role}/{role_id}/workout-routine/{workout_routine_id}/workout-exercises")
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

            List<WorkoutRoutine> routines = workoutRoutineRepository.findByGymAndIsTemplate(gym, true);


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

    @PostMapping("/gym/{gym_id}/{role}/{role_id}/workout-member-assignment")
    public ResponseEntity<?> newMemberAssignment(@PathVariable int gym_id, @RequestBody NewMemberAssignment newMemberAssignment) {
        Gym gym = gymRepository.findById(gym_id).orElseThrow(() -> new ResourceNotFoundException("Gym not found"));
        User member = userRepository.findUserByIdAndUserType(newMemberAssignment.memberId, UserType.member).orElseThrow(() -> new ResourceNotFoundException("Member not found"));
        WorkoutRoutine workoutRoutine = workoutRoutineRepository.findById(newMemberAssignment.routineId).orElseThrow(() -> new ResourceNotFoundException("Workout Routine not found"));
        MemberWorkoutAssignment check = memberWorkoutAssignmentRepository.findByMemberAndDayOfWeek(member, newMemberAssignment.dayOfWeek);
        if (check != null) {
            return ResponseEntity.status(400).body(Map.of("error", "Workout routine for that day already do exists for that user"));
        }
        MemberWorkoutAssignment newAssignment = new MemberWorkoutAssignment();
        newAssignment.setGym(gym);
        newAssignment.setMember(member);
        newAssignment.setRoutine(workoutRoutine);
        newAssignment.setDayOfWeek(newMemberAssignment.dayOfWeek);
        memberWorkoutAssignmentRepository.save(newAssignment);
        return ResponseEntity.ok(Map.of("message", "workout assigned successfully"));
    }

    @GetMapping("/gym/{gym_id}/{role}/{role_id}/workout/member")
    public ResponseEntity<?> getWorkoutAssignment(@PathVariable int gym_id, @PathVariable UserType role, @PathVariable int role_id) {
        Gym gym = gymRepository.findById(gym_id).orElseThrow(() -> new ResourceNotFoundException("Gym not found"));
        User member = userRepository.findUserByIdAndUserType(role_id, UserType.member).orElseThrow(() -> new ResourceNotFoundException("Member not found"));
        return ResponseEntity.ok(memberWorkoutAssignmentRepository.findByGymAndMember(gym, member));
    }


}

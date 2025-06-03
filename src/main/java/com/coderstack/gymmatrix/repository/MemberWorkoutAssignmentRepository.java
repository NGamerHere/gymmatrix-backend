package com.coderstack.gymmatrix.repository;

import com.coderstack.gymmatrix.models.MemberWorkoutAssignment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberWorkoutAssignmentRepository extends JpaRepository<MemberWorkoutAssignment, Integer> {
}

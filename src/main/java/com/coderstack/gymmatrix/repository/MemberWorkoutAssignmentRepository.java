package com.coderstack.gymmatrix.repository;

import com.coderstack.gymmatrix.models.Gym;
import com.coderstack.gymmatrix.models.MemberWorkoutAssignment;
import com.coderstack.gymmatrix.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.DayOfWeek;
import java.util.List;

@Repository
public interface MemberWorkoutAssignmentRepository extends JpaRepository<MemberWorkoutAssignment, Integer> {
    List<MemberWorkoutAssignment> findByGymAndMember(Gym gym, User member);

    MemberWorkoutAssignment findByMemberAndDayOfWeek(User member, DayOfWeek dayOfWeek);
}

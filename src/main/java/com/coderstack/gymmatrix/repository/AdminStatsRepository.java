package com.coderstack.gymmatrix.repository;

import com.coderstack.gymmatrix.dto.MemberProjection;
import com.coderstack.gymmatrix.enums.UserType;
import com.coderstack.gymmatrix.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AdminStatsRepository extends JpaRepository<User, Integer> {

    @Query("SELECT COUNT(u) FROM User u WHERE u.gym.id = :gymId AND u.userType = :userType")
    int getTotalMemberCount(@Param("gymId") Long gymId, @Param("userType") UserType userType);

    @Query("SELECT COUNT(m) FROM Membership m WHERE m.gym.id = :gymId AND m.status = com.coderstack.gymmatrix.enums.PlanStatus.ACTIVE")
    int getTotalActiveMembers(@Param("gymId") Long gymId);

    @Query("SELECT SUM(p.amount) FROM Payment p WHERE p.gym.id = :gymId")
    Double getTotalRevenue(@Param("gymId") Long gymId);

    @Query(value = """
            SELECT
                m.id AS memberId,
                m.email AS email,
                m.name AS name,
                p.plan_name AS planName,
                p.plan_duration AS planDuration,
                membership.status AS status
            FROM
                membership
            INNER JOIN membership_plans p ON p.id = membership.membership_plan_id
            INNER JOIN users m ON membership.user_id = m.id
            WHERE
                membership.gym_id = :gym_id
              AND (membership.status = 'ACTIVE' OR membership.status = 'upcoming')
              AND m.user_type = 'member'
            ORDER BY membership.status ASC
            """, nativeQuery = true)
    List<MemberProjection> getMember(@Param("gym_id") Long gym_id);
}
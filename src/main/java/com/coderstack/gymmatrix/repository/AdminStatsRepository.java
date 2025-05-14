package com.coderstack.gymmatrix.repository;

import com.coderstack.gymmatrix.dto.MemberProjection;
import com.coderstack.gymmatrix.models.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AdminStatsRepository extends JpaRepository<Member, Integer> {

    @Query(value = "SELECT COUNT(*) FROM members WHERE gym_id = :gymId", nativeQuery = true)
    int getTotalMemberCount(@Param("gymId") Long gymId);

    @Query(value = "SELECT COUNT(*) FROM membership WHERE gym_id = :gymId AND active = 1", nativeQuery = true)
    int getTotalActiveMembers(@Param("gymId") Long gymId);

    @Query(value = "SELECT SUM(amount) FROM payments WHERE gym_id = :gymId", nativeQuery = true)
    Double getTotalRevenue(@Param("gymId") Long gymId);

    @Query(value = """
            SELECT 
                    m.id AS memberId,
                    m.email AS email,
                m.name AS name,
                p.plan_name AS planName,
                p.plan_duration AS planDuration,
                active
            FROM 
                membership 
                INNER JOIN membership_plans p ON p.id = membership.membership_plan_id 
                INNER JOIN members m ON membership.user_id = m.id 
            WHERE 
                membership.gym_id = :gym_id 
                AND membership.active = 1
            """, nativeQuery = true)
    List<MemberProjection> getMember(@Param("gym_id") Long gym_id);
}




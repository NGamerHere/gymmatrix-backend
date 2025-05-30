package com.coderstack.gymmatrix.repository;

import com.coderstack.gymmatrix.enums.PlanStatus;
import com.coderstack.gymmatrix.models.Gym;
import com.coderstack.gymmatrix.models.Member;
import com.coderstack.gymmatrix.models.Membership;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface MembershipRepository extends JpaRepository<Membership, Integer> {

    List<Membership> findByGym(Gym gym);

    Membership getByMemberAndGymAndActiveTrue(Member member, Gym gym);

    List<Membership> findByStatus(PlanStatus status);

    @Query("""
        SELECT COUNT(m) FROM Membership m
        WHERE m.member.id = :userId
          AND m.gym.id = :gymId
          AND (
            (
              m.status = 'ACTIVE' AND 
              :checkDate BETWEEN m.startDate AND m.endDate
            ) OR 
            (
              m.status = 'UPCOMING' AND
              :checkDate BETWEEN m.startDate AND m.endDate
            )
          )
        """)
    int findActiveOrUpcomingMembership(
            @Param("userId") int userId,
            @Param("gymId") int gymId,
            @Param("checkDate") LocalDate checkDate
    );


    @Query("SELECT COUNT(m) FROM Membership m WHERE m.gym.id = ?1 AND m.member.id = ?2 AND m.status = ?3")
    Long countActiveMemberShip(Integer gymId,Integer userId, PlanStatus status);
}
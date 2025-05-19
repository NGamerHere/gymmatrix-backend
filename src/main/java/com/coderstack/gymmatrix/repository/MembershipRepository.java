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
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface MembershipRepository extends JpaRepository<Membership, Integer> {
    public List<Membership> findByGym(Gym gym);

    Membership getByMemberAndGymAndActiveTrue(Member member, Gym gym);

    @Query("""
    SELECT count(*) FROM Membership m
    WHERE m.member.id = :userId
      AND m.gym.id = :gymId
      AND (
        (
          m.status = 'ACTIVE' AND 
          :checkDate BETWEEN FUNCTION('DATE', m.startDate) AND FUNCTION('DATE', m.endDate)
        ) or 
        (
          m.status = 'upcoming' AND
          :checkDate BETWEEN FUNCTION('DATE', m.startDate) AND FUNCTION('DATE', m.endDate)
        )
      )
""")
    int findActiveOrUpcomingMembership(
            @Param("userId") int userId,
            @Param("gymId") int gymId,
            @Param("checkDate") LocalDateTime checkDate
    );


    @Query(value="""
           select count(*) from membership where gym_id=:gym_id and user_id=:user_id and status=:status
      """,nativeQuery = true)
    public Integer countActiveMemberShip(@Param("gym_id") int gymId,@Param("user_id") int userId,@Param("status") PlanStatus planStatus);




}

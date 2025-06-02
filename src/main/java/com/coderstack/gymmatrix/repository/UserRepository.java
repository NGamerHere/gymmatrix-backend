package com.coderstack.gymmatrix.repository;

import com.coderstack.gymmatrix.dto.MembersProjection;
import com.coderstack.gymmatrix.dto.MembershipHistory;
import com.coderstack.gymmatrix.enums.UserType;
import com.coderstack.gymmatrix.models.Gym;
import com.coderstack.gymmatrix.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User , Integer> {
    User findUserByEmailAndUserType(String email, UserType userType);

    @Query("SELECT u FROM User u WHERE u.email = :email AND u.phone = :phone")
    List<User> findDuplicates(@Param("email") String email, @Param("phone") String phone);

    List<User> findByGym(Gym gym);

    Optional<User> findByIdAndUserType(Integer integer,UserType userType);

    List<User> findByGymAndUserType(Gym gym, UserType userType);


    @Query(value = """
        SELECT users.id,
               users.name,
               users.email,
               users.phone,
               m.membership_id AS membershipId,
               m.status,
               m.start_date,
               m.end_date,
               m.plan_id,
               m.plan_name,
               m.plan_duration,
               m.price
        FROM users
        LEFT JOIN LATERAL (
            SELECT mem.id                 AS membership_id,
                   mem.status,
                   mem.start_date,
                   mem.end_date,
                   mem.membership_plan_id AS plan_id,
                   mp.plan_name,
                   mp.plan_duration,
                   mp.price
            FROM membership mem
            JOIN membership_plans mp ON mem.membership_plan_id = mp.id
            WHERE mem.user_id = users.id
              AND (
                mem.status = 'ACTIVE'
                OR (mem.status = 'UPCOMING' AND mem.start_date > CURRENT_DATE)
              )
            ORDER BY CASE WHEN mem.status = 'ACTIVE' THEN 1 ELSE 2 END,
                     mem.start_date ASC
            LIMIT 1
        ) m ON true
        WHERE users.gym_id = :gym_id and users.user_type='member'
        ORDER BY m.status DESC
        """, nativeQuery = true)
    List<MembersProjection> getMembersProjection(@Param("gym_id") int gymId);

    User findUserByIdAndUserType(int id, UserType userType);


    @Query("""
        SELECT 
            p.amount AS amount,
            p.paymentType AS paymentType,
            p.collectedOn AS paymentDoneOn,
            mp.plan_name AS planName,
            m.active AS active,
            m.status AS status,
            m.startDate AS startDate,
            m.endDate AS endDate,
            a.name AS collectedBy
        FROM Payment p
        INNER JOIN p.membershipPlan mp
        INNER JOIN p.membership m
        INNER JOIN p.collectedByAdmin a
        WHERE p.gym.id = :gymId AND p.member.id = :memberId
        ORDER BY 
            CASE m.status
                WHEN 'active' THEN 0
                WHEN 'expired' THEN 1
                WHEN 'upcoming' THEN 2
                ELSE 3
            END
        """)
    List<MembershipHistory> findPaymentDetailsByGymIdAndMemberId(@Param("gymId") Integer gymId, @Param("memberId") Integer memberId);

    @Query("""
        SELECT COUNT(m) FROM Membership m
        WHERE m.user.id = :userId
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
}

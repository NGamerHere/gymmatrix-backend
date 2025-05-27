package com.coderstack.gymmatrix.repository;

import com.coderstack.gymmatrix.dto.MembersInfo;
import com.coderstack.gymmatrix.dto.MembersProjection;
import com.coderstack.gymmatrix.dto.MembershipHistory;
import com.coderstack.gymmatrix.models.Gym;
import com.coderstack.gymmatrix.models.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Integer> {
    Optional<Member> findByPhoneOrEmail(String phone, String email);

    List<Member> findByGym(Gym gym);

    Member findByEmail(String email);

    @Query(value = """
            SELECT members.id,
                   members.name,
                   members.email,
                   members.phone,
            
                   m.membership_id as membershipId,
                   m.status,
                   m.start_date,
                   m.end_date,
            
                   m.plan_id,
                   m.plan_name,
                   m.plan_duration,
                   m.price
            FROM members
                     LEFT JOIN LATERAL (
                SELECT mem.id                 AS membership_id,
                       mem.status,
                       mem.start_date,
                       mem.end_date,
                       mem.membership_plan_id AS plan_id,
                       mp.plan_name,
                       mp.plan_duration,
                       mp.price
                FROM gymmatrix.membership mem
                         JOIN gymmatrix.membership_plans mp ON mem.membership_plan_id = mp.id
                WHERE mem.user_id = members.id
                  AND (
                    mem.status = 'ACTIVE'
                        OR (mem.status = 'UPCOMING' AND mem.start_date > CURRENT_DATE)
                    )
                ORDER BY CASE WHEN mem.status = 'ACTIVE' THEN 1 ELSE 2 END,
                         mem.start_date ASC
                LIMIT 1
                ) m ON true
            WHERE members.gym_id = :gym_id
                        order by m.status desc
            """, nativeQuery = true)
    List<MembersProjection> getMembersProjection(@Param("gym_id") int gymId);

    @Query(value = """
                  select m.id,m.email,m.name,m.phone,ms.status,mp.plan_name as planName from members m
                left join membership ms on m.id = ms.user_id
                              left join membership_plans mp on ms.membership_plan_id = mp.id
                              where m.gym_id=:gym_id and ms.status='ACTIVE'
            """, nativeQuery = true)
    List<MembersInfo> getMemberInfo(@Param("gym_id") int gymId);

    @Query("SELECT m FROM Member m WHERE m.gym.id = :gymId AND (m.phone = :phone OR m.email = :email)")
    List<Member> findDuplicates(@Param("gymId") int gymId, @Param("phone") String phone, @Param("email") String email);

    @Query("SELECT " +
            "p.amount AS amount, " +
            "p.paymentType AS paymentType, " +
            "p.collectedOn AS paymentDoneOn, " +
            "mp.plan_name AS planName, " +
            "m.active AS active, " +
            "m.status AS status," +
            "m.startDate AS startDate, " +
            "m.endDate AS endDate, " +
            "a.name AS collectedBy " +
            "FROM Payment p " +
            "INNER JOIN p.membershipPlan mp " +
            "INNER JOIN p.membership m " +
            "INNER JOIN p.collectedByAdmin a " +
            "WHERE p.gym.id = :gymId AND p.member.id = :memberId " +
            "ORDER BY  CASE status" +
            "    WHEN 'active' THEN 0" +
            "    WHEN 'expired' THEN 1" +
            "    WHEN 'upcoming' THEN 2" +
            "    ELSE 3 END")
    List<MembershipHistory> findPaymentDetailsByGymIdAndMemberId(@Param("gymId") Integer gymId, @Param("memberId") Integer memberId);

}
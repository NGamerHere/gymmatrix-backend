package com.coderstack.gymmatrix.repository;

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

    @Query("SELECT m FROM Member m WHERE m.gym.id = :gymId AND (m.phone = :phone OR m.email = :email)")
    List<Member> findDuplicates(@Param("gymId") int gymId, @Param("phone") String phone, @Param("email") String email);

    @Query("SELECT " +
            "p.amount AS amount, " +
            "p.paymentType AS paymentType, " +
            "p.collectedOn AS paymentDoneOn, " +
            "mp.plan_name AS planName, " +
            "m.active AS active, " +
            "m.startDate AS startDate, " +
            "m.endDate AS endDate, " +
            "a.name AS collectedBy " +
            "FROM Payment p " +
            "INNER JOIN p.membershipPlan mp " +
            "INNER JOIN p.membership m " +
            "INNER JOIN p.collectedByAdmin a " +
            "WHERE p.gym.id = :gymId AND p.member.id = :memberId order by active desc")
    List<MembershipHistory> findPaymentDetailsByGymIdAndMemberId(@Param("gymId") Integer gymId, @Param("memberId") Integer memberId);

}
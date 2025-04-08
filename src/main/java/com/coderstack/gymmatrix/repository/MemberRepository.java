package com.coderstack.gymmatrix.repository;

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
}
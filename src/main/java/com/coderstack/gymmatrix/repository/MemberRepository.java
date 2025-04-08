package com.coderstack.gymmatrix.repository;

import com.coderstack.gymmatrix.models.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Integer> {
    Optional<Member> findByPhone(String phone);
    Optional<Member> findByPhoneOrEmail(String phone, String email);
}
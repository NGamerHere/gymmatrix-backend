package com.coderstack.gymmatrix.repository;

import com.coderstack.gymmatrix.models.Gym;
import com.coderstack.gymmatrix.models.Member;
import com.coderstack.gymmatrix.models.Membership;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MembershipRepository extends JpaRepository<Membership, Integer> {
    public List<Membership> findByGym(Gym gym);

    Membership getByMemberAndGymAndActiveTrue(Member member, Gym gym);
}

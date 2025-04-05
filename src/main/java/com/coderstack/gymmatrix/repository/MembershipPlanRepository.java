package com.coderstack.gymmatrix.repository;

import com.coderstack.gymmatrix.models.Gym;
import com.coderstack.gymmatrix.models.MembershipPlan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface MembershipPlanRepository extends JpaRepository<MembershipPlan, Integer> {
    List<MembershipPlan> getAllByGym(Gym gym);
}

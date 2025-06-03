package com.coderstack.gymmatrix.repository;

import com.coderstack.gymmatrix.models.Equipment;
import com.coderstack.gymmatrix.models.Gym;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EquipmentRepository extends JpaRepository<Equipment, Integer> {

    List<Equipment> findByGym(Gym gym);
}

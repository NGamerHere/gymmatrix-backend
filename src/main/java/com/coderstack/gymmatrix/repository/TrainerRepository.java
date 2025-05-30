package com.coderstack.gymmatrix.repository;

import com.coderstack.gymmatrix.dto.TrainerDTO;
import com.coderstack.gymmatrix.models.Trainer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TrainerRepository extends JpaRepository<Trainer, Integer> {

    Trainer findByEmail(String email);

    @Query(value = """
    SELECT t.id, t.name, t.email, t.phone, t.gender
    FROM trainer t
    WHERE t.gym_id = :gymId
    """, nativeQuery = true)
    List<TrainerDTO> findByGym(@Param("gymId") int gymId);

    @Query("SELECT t FROM Trainer t WHERE t.gym.id = :gymId AND (t.phone = :phone OR t.email = :email)")
    List<Trainer> findDuplicates(@Param("gymId") int gymId, @Param("phone") String phone, @Param("email") String email);
}

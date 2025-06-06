package com.coderstack.gymmatrix.repository;

import com.coderstack.gymmatrix.models.DeviceToken;
import com.coderstack.gymmatrix.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DeviceTokenRepository extends JpaRepository<DeviceToken, Long> {
    List<DeviceToken> findByUser(User user);
    Optional<DeviceToken> findTopByUserOrderByAddedOnDesc(User user);
    Optional<DeviceToken> findByToken(String token);
}

 package com.coderstack.gymmatrix.repository;

import com.coderstack.gymmatrix.models.Gym;
import com.coderstack.gymmatrix.models.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PaymentRepository extends JpaRepository<Payment,Integer> {
    List<Payment> getPaymentsByGym(Gym gym);
}

package com.coderstack.gymmatrix.repository;

import com.coderstack.gymmatrix.enums.UserType;
import com.coderstack.gymmatrix.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User , Integer> {
    User findUserByEmailAndUser_type(String Email, UserType userType);
}

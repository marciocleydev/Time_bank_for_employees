package com.marciocleydev.Time_bank_for_employees.repositories;

import com.marciocleydev.Time_bank_for_employees.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    @Query("select u from User u where u.username=:userName")
    User findByUsername(@Param(value = "userName") String userName);
}

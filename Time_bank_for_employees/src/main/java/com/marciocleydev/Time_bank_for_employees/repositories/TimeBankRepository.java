package com.marciocleydev.Time_bank_for_employees.repositories;

import com.marciocleydev.Time_bank_for_employees.entities.Employee;
import com.marciocleydev.Time_bank_for_employees.entities.TimeBank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TimeBankRepository extends JpaRepository<TimeBank, Long> {
}

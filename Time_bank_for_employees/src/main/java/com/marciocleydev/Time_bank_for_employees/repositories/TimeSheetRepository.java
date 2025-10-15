package com.marciocleydev.Time_bank_for_employees.repositories;

import com.marciocleydev.Time_bank_for_employees.entities.TimeBank;
import com.marciocleydev.Time_bank_for_employees.entities.TimeSheet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TimeSheetRepository extends JpaRepository<TimeSheet, Long> {
}

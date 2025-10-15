package com.marciocleydev.Time_bank_for_employees.services;

import com.marciocleydev.Time_bank_for_employees.entities.Employee;
import com.marciocleydev.Time_bank_for_employees.repositories.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;

@Service
public class EmployeeService  {

    @Autowired
    private EmployeeRepository repository;

}


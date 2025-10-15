package com.marciocleydev.Time_bank_for_employees.controllers;

import com.marciocleydev.Time_bank_for_employees.entities.Employee;
import com.marciocleydev.Time_bank_for_employees.services.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/employees")
public class EmployeeController {

    @Autowired
    private EmployeeService service;

    @GetMapping(value = "/{id}")
    private ResponseEntity<Employee> findById(@PathVariable Long id) {
        Employee employee = service.findById(id);
        return ResponseEntity.ok().body(employee);
    }
}

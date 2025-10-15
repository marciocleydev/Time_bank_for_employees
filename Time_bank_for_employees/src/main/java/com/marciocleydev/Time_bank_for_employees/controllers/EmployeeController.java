package com.marciocleydev.Time_bank_for_employees.controllers;

import com.marciocleydev.Time_bank_for_employees.entities.Employee;
import com.marciocleydev.Time_bank_for_employees.services.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/employees")
public class EmployeeController {

    @Autowired
    private EmployeeService service;

    @GetMapping
    private ResponseEntity<List<Employee>> findAll() {
        List<Employee> employees = service.findAll();
        return ResponseEntity.ok().body(employees);
    }

    @GetMapping(value = "/{id}")
    private ResponseEntity<Employee> findById(@PathVariable Long id) {
        Employee employee = service.findById(id);
        return ResponseEntity.ok().body(employee);
    }

    @PostMapping()
    private ResponseEntity<Employee> create(@RequestBody Employee employee) {
        Employee persistedEmployee = service.create(employee);
        //Cria uma URI (endereço) para o novo recurso, Usa a URL da requisição atual e adiciona o id do novo funcionário no final
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(persistedEmployee.getId()).toUri();
        return ResponseEntity.created(uri).body(persistedEmployee);
    }

    @PutMapping(value = "/{id}")
    private ResponseEntity<Employee> update(@PathVariable Long id, @RequestBody Employee employee) {
        Employee updatedEmployee = service.update(employee, id);
        return ResponseEntity.ok().body(updatedEmployee);
    }

    @DeleteMapping(value = "/{id}")
    private ResponseEntity<Void> deleteById(@PathVariable Long id) {
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}

package com.marciocleydev.Time_bank_for_employees.controllers;

import com.marciocleydev.Time_bank_for_employees.DTO.EmployeeDTO;
import com.marciocleydev.Time_bank_for_employees.controllers.docs.EmployeeControllerDocs;
import com.marciocleydev.Time_bank_for_employees.services.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/employees")
public class EmployeeController implements EmployeeControllerDocs {

    @Autowired
    private EmployeeService service;


    @GetMapping
    @Override
    public ResponseEntity<PagedModel<EntityModel<EmployeeDTO>>> findAll(
            @RequestParam(value = "page") Integer page,
            @RequestParam(value = "size") Integer size,
            @RequestParam(value = "direction") String direction
    ) {
        var sortDirection = "desc".equalsIgnoreCase(direction) ? Sort.Direction.DESC : Sort.Direction.ASC;

        Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, "name"));
        var  employees = service.findAll(pageable);
        return ResponseEntity.ok().body(employees);
    }

    @GetMapping(value = "/{id}")
    @Override
    public ResponseEntity<EmployeeDTO> findById(@PathVariable Long id) {
        EmployeeDTO employee = service.findById(id);
        return ResponseEntity.ok().body(employee);
    }

    @PostMapping()
    @Override
    public ResponseEntity<EmployeeDTO> create(@RequestBody EmployeeDTO employee) {
        EmployeeDTO persistedEmployee = service.create(employee);
        //Cria uma URI (endereço) para o novo recurso, Usa a URL da requisição atual e adiciona o id do novo funcionário no final
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(persistedEmployee.getId()).toUri();
        return ResponseEntity.created(uri).body(persistedEmployee);
    }

    @PutMapping(value = "/{id}")
    @Override
    public ResponseEntity<EmployeeDTO> update(@PathVariable Long id, @RequestBody EmployeeDTO employee) {
        EmployeeDTO updatedEmployee = service.update(employee, id);
        return ResponseEntity.ok().body(updatedEmployee);
    }

    @DeleteMapping(value = "/{id}")
    @Override
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }

}

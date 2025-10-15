package com.marciocleydev.Time_bank_for_employees.controllers;

import com.marciocleydev.Time_bank_for_employees.entities.TimeSheet;
import com.marciocleydev.Time_bank_for_employees.services.TimeSheetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/timeSheets")
public class TimeSheetController {

    @Autowired
    private TimeSheetService service;

    @GetMapping
    private ResponseEntity<List<TimeSheet>> findAll() {
        List<TimeSheet> timeSheets = service.findAll();
        return ResponseEntity.ok().body(timeSheets);
    }

    @GetMapping(value = "/{id}")
    private ResponseEntity<TimeSheet> findById(@PathVariable Long id) {
        TimeSheet timeSheet = service.findById(id);
        return ResponseEntity.ok().body(timeSheet);
    }

    @PostMapping()
    private ResponseEntity<TimeSheet> create(@RequestBody TimeSheet timeSheet) {
        TimeSheet persistedTimeSheet = service.create(timeSheet);
        //Cria uma URI (endereço) para o novo recurso, Usa a URL da requisição atual e adiciona o id do novo funcionário no final
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(persistedTimeSheet.getId()).toUri();
        return ResponseEntity.created(uri).body(persistedTimeSheet);
    }

    @PutMapping(value = "/{id}")
    private ResponseEntity<TimeSheet> update(@PathVariable Long id, @RequestBody TimeSheet timeSheet) {
        TimeSheet updatedTimeSheet = service.update(timeSheet, id);
        return ResponseEntity.ok().body(updatedTimeSheet);
    }

    @DeleteMapping(value = "/{id}")
    private ResponseEntity<Void> deleteById(@PathVariable Long id) {
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}

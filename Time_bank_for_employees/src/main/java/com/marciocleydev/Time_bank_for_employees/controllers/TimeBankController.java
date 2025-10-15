package com.marciocleydev.Time_bank_for_employees.controllers;

import com.marciocleydev.Time_bank_for_employees.entities.TimeBank;
import com.marciocleydev.Time_bank_for_employees.services.TimeBankService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/timeBanks")
public class TimeBankController {

    @Autowired
    private TimeBankService service;

    @GetMapping
    private ResponseEntity<List<TimeBank>> findAll() {
        List<TimeBank> timeBanks = service.findAll();
        return ResponseEntity.ok().body(timeBanks);
    }

    @GetMapping(value = "/{id}")
    private ResponseEntity<TimeBank> findById(@PathVariable Long id) {
        TimeBank timeBank = service.findById(id);
        return ResponseEntity.ok().body(timeBank);
    }

    @PostMapping()
    private ResponseEntity<TimeBank> create(@RequestBody TimeBank timeBank) {
        TimeBank persistedTimeBank = service.create(timeBank);
        //Cria uma URI (endereço) para o novo recurso, Usa a URL da requisição atual e adiciona o id do novo funcionário no final
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(persistedTimeBank.getId()).toUri();
        return ResponseEntity.created(uri).body(persistedTimeBank);
    }

    @PutMapping(value = "/{id}")
    private ResponseEntity<TimeBank> update(@PathVariable Long id, @RequestBody TimeBank timeBank) {
        TimeBank updatedTimeBank = service.update(timeBank, id);
        return ResponseEntity.ok().body(updatedTimeBank);
    }

    @DeleteMapping(value = "/{id}")
    private ResponseEntity<Void> deleteById(@PathVariable Long id) {
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}

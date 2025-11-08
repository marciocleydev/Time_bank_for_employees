package com.marciocleydev.Time_bank_for_employees.controllers;

import com.marciocleydev.Time_bank_for_employees.DTO.TimeSheetDTO;
import com.marciocleydev.Time_bank_for_employees.controllers.docs.TimeSheetControllerDocs;
import com.marciocleydev.Time_bank_for_employees.services.TimeSheetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/timeSheets")
public class TimeSheetController implements TimeSheetControllerDocs {

    @Autowired
    private TimeSheetService service;


    @GetMapping
    @Override
    public ResponseEntity<PagedModel<EntityModel<TimeSheetDTO>>> findAll(
            @RequestParam(value = "page") Integer page,
            @RequestParam(value = "size") Integer size,
            @RequestParam(value = "direction") String direction
    ) {
        var sortDirection = "desc".equalsIgnoreCase(direction) ? Sort.Direction.DESC : Sort.Direction.ASC;

        Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, "id"));
        var timeSheets = service.findAll(pageable);
        return ResponseEntity.ok().body(timeSheets);
    }

    @GetMapping(value = "/{id}")
    @Override
    public ResponseEntity<TimeSheetDTO> findById(@PathVariable Long id) {
        TimeSheetDTO timeSheet = service.findById(id);
        return ResponseEntity.ok().body(timeSheet);
    }

    @PostMapping()
    @Override
    public ResponseEntity<TimeSheetDTO> create(@RequestBody TimeSheetDTO timeSheet) {
        TimeSheetDTO persistedTimeSheet = service.create(timeSheet);
        //Cria uma URI (endereço) para o novo recurso, Usa a URL da requisição atual e adiciona o id do novo funcionário no final
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(persistedTimeSheet.getId()).toUri();
        return ResponseEntity.created(uri).body(persistedTimeSheet);
    }

    @PutMapping(value = "/{id}")
    @Override
    public ResponseEntity<TimeSheetDTO> update(@PathVariable Long id, @RequestBody TimeSheetDTO timeSheet) {
        TimeSheetDTO updatedTimeSheet = service.update(timeSheet, id);
        return ResponseEntity.ok().body(updatedTimeSheet);
    }

    @DeleteMapping(value = "/{id}")
    @Override
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }

}

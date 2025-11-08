package com.marciocleydev.Time_bank_for_employees.controllers;

import com.marciocleydev.Time_bank_for_employees.DTO.TimeBankDTO;
import com.marciocleydev.Time_bank_for_employees.controllers.docs.TimeBankControllerDocs;
import com.marciocleydev.Time_bank_for_employees.services.TimeBankService;
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
@RequestMapping("/timeBanks")
public class TimeBankController implements TimeBankControllerDocs {

    @Autowired
    private TimeBankService service;


    @GetMapping
    @Override
    public ResponseEntity<PagedModel<EntityModel<TimeBankDTO>>> findAll(
            @RequestParam(value = "page") Integer page,
            @RequestParam(value = "size") Integer size,
            @RequestParam(value = "direction") String direction
    ) {
        var sortDirection = "desc".equalsIgnoreCase(direction) ? Sort.Direction.DESC : Sort.Direction.ASC;

        Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, "id"));
        var timeBanks = service.findAll(pageable);
        return ResponseEntity.ok().body(timeBanks);
    }

    @GetMapping(value = "/{id}")
    @Override
    public ResponseEntity<TimeBankDTO> findById(@PathVariable Long id) {
        TimeBankDTO timeBank = service.findById(id);
        return ResponseEntity.ok().body(timeBank);
    }

    @PostMapping()
    @Override
    public ResponseEntity<TimeBankDTO> create(@RequestBody TimeBankDTO timeBank) {
        TimeBankDTO persistedTimeBank = service.create(timeBank);
        //Cria uma URI (endereço) para o novo recurso, Usa a URL da requisição atual e adiciona o id do novo funcionário no final
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(persistedTimeBank.getId()).toUri();
        return ResponseEntity.created(uri).body(persistedTimeBank);
    }

    @PutMapping(value = "/{id}")
    @Override
    public ResponseEntity<TimeBankDTO> update(@PathVariable Long id, @RequestBody TimeBankDTO timeBank) {
        TimeBankDTO updatedTimeBank = service.update(timeBank, id);
        return ResponseEntity.ok().body(updatedTimeBank);
    }

    @DeleteMapping(value = "/{id}")
    @Override
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }

}

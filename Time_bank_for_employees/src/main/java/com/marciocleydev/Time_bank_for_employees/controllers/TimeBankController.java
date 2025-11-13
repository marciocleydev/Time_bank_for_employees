package com.marciocleydev.Time_bank_for_employees.controllers;

import com.marciocleydev.Time_bank_for_employees.DTO.TimeBankDTO;
import com.marciocleydev.Time_bank_for_employees.controllers.docs.TimeBankControllerDocs;
import com.marciocleydev.Time_bank_for_employees.services.TimeBankService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/employees/{employeeId}/timeBank")
public class TimeBankController implements TimeBankControllerDocs {

    @Autowired
    private TimeBankService service;


    @GetMapping( "/balance")
    public ResponseEntity<TimeBankDTO> getBalance(@PathVariable Long employeeId){
        return ResponseEntity.ok().body(service.getBalance(employeeId));
    }

    @PostMapping("/add")                                                              //@RequestParam vem via query param ?minutes=10
    public ResponseEntity<TimeBankDTO> addHours(@PathVariable Long employeeId, @RequestParam Integer minutes){
        return ResponseEntity.ok().body(service.addHours(employeeId, minutes));
    }

    @PostMapping("/remove")
    public ResponseEntity<TimeBankDTO> removeHours(@PathVariable Long employeeId, @RequestParam Integer minutes){
        return ResponseEntity.ok().body(service.removeHours(employeeId, minutes));
    }


}

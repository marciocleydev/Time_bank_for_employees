package com.marciocleydev.Time_bank_for_employees.controllers;

import com.marciocleydev.Time_bank_for_employees.DTO.TimeSheetDTO;
import com.marciocleydev.Time_bank_for_employees.services.TimeSheetService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/employees/{employeeId}/timeSheet")
public class TimeSheetController {
    private static TimeSheetService service;

    public TimeSheetController(TimeSheetService service) {
        this.service = service;
    }

    @PostMapping("/add")
    public ResponseEntity<TimeSheetDTO> add(@PathVariable Long employeeId, @RequestBody TimeSheetDTO timeSheetDTO) {
        var persistedTimeSheetDTO = service.create(timeSheetDTO, employeeId);
        return ResponseEntity.ok().body(persistedTimeSheetDTO);
    }
}

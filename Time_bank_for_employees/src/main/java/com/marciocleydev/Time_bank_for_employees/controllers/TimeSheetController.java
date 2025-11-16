package com.marciocleydev.Time_bank_for_employees.controllers;

import com.marciocleydev.Time_bank_for_employees.DTO.TimeSheetDTO;
import com.marciocleydev.Time_bank_for_employees.services.TimeSheetService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/employees/{employeeId}/timeSheet")
public class TimeSheetController {
    private static TimeSheetService service;

    public TimeSheetController(TimeSheetService service) {
        this.service = service;
    }

    @PostMapping("/add")
    public ResponseEntity<TimeSheetDTO> addTimeSheet(@PathVariable Long employeeId, @RequestParam MultipartFile file) {
        var persistedTimeSheetDTO = service.create(timeSheetDTO, employeeId);
        return ResponseEntity.ok().body(persistedTimeSheetDTO);
    }
}

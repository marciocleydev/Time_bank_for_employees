package com.marciocleydev.Time_bank_for_employees.services;

import com.marciocleydev.Time_bank_for_employees.DTO.TimeSheetDTO;
import com.marciocleydev.Time_bank_for_employees.entities.Employee;
import com.marciocleydev.Time_bank_for_employees.entities.TimeSheet;
import com.marciocleydev.Time_bank_for_employees.exceptions.ResourceNotFoundException;
import com.marciocleydev.Time_bank_for_employees.mapper.TimeSheetMapper;
import com.marciocleydev.Time_bank_for_employees.repositories.EmployeeRepository;
import com.marciocleydev.Time_bank_for_employees.repositories.TimeSheetRepository;
import org.springframework.stereotype.Service;

@Service
public class TimeSheetService {
    private static EmployeeRepository employeeRepository;
    private static TimeSheetRepository repository;
    private TimeSheetMapper mapper;

    public TimeSheetService(EmployeeRepository employeeRepository, TimeSheetRepository repository, TimeSheetMapper mapper) {
        this.employeeRepository = employeeRepository;
        this.repository = repository;
        this.mapper = mapper;
    }

    public TimeSheetDTO create(TimeSheetDTO timeSheetDTO, Long employeeId) {
        Employee employee = employeeRepository.findById(employeeId).orElseThrow(()-> new ResourceNotFoundException("Employee not found! ID: ", employeeId));
        if (timeSheetDTO.getId() != null) {
            throw new ResourceNotFoundException("TimeSheet ID must be null to create a new TimeSheet");
        }
        TimeSheet timeSheet = mapper.toEntity(timeSheetDTO);
        timeSheet.setEmployee(employee);
        timeSheet = repository.save(timeSheet);

        return mapper.toDTO(timeSheet);
    }


}

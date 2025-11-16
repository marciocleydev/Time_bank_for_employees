package com.marciocleydev.Time_bank_for_employees.services;

import com.marciocleydev.Time_bank_for_employees.DTO.TimeSheetDTO;
import com.marciocleydev.Time_bank_for_employees.entities.Employee;
import com.marciocleydev.Time_bank_for_employees.entities.TimeSheet;
import com.marciocleydev.Time_bank_for_employees.mapper.TimeSheetMapper;
import com.marciocleydev.Time_bank_for_employees.repositories.TimeSheetRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;

@Service
public class TimeSheetService {

    private FileStorageService fileStorageService;
    private static EmployeeService employeeService;
    private static TimeSheetRepository timeSheetRepository;
    private TimeSheetMapper mapper;

    public TimeSheetService(FileStorageService fileStorageService, EmployeeService employeeService, TimeSheetRepository repository, TimeSheetMapper mapper) {
        this.fileStorageService = fileStorageService;
        this.employeeService = employeeService;
        this.timeSheetRepository = repository;
        this.mapper = mapper;
    }

    public TimeSheetDTO processTimeSheet(Long employeeId, MultipartFile file) {

        String savedFileName = fileStorageService.storeFile(file);

        Employee employee = employeeService.findEntityById(employeeId);

       LocalTime expectedExit = null; //employee.getExpectedExitTime();
        LocalTime actualExit = LocalTime.now(); // futuramente pode ser OCR

        long diffInMinutes = Duration
                .between(expectedExit, actualExit)
                .toMinutes();

        TimeSheet timeSheet = new TimeSheet();
        timeSheet.setEmployee(employee);
        timeSheet.setExpectedWorkTime(expectedExit);
        timeSheet.setActualSheetTime(actualExit);
        timeSheet.setDifferenceInMinutes((int) diffInMinutes);
        timeSheet.setPhotoUrl(savedFileName);
        timeSheet.setDate(LocalDate.now());

        timeSheetRepository.save(timeSheet);

        return new TimeSheetDTO(timeSheet);
    }


}

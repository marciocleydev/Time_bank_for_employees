package com.marciocleydev.Time_bank_for_employees.services;

import com.marciocleydev.Time_bank_for_employees.DTO.TimeBankDTO;
import com.marciocleydev.Time_bank_for_employees.controllers.TimeBankController;
import com.marciocleydev.Time_bank_for_employees.exceptions.ResourceNotFoundException;
import com.marciocleydev.Time_bank_for_employees.mapper.TimeBankMapper;
import com.marciocleydev.Time_bank_for_employees.repositories.EmployeeRepository;
import com.marciocleydev.Time_bank_for_employees.repositories.TimeBankRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
public class TimeBankService {

    @Autowired
    EmployeeRepository employeeRepository;
    @Autowired
    private TimeBankRepository TimeBankRepository;
    @Autowired
    private TimeBankMapper mapper;

    private static final Logger logger = LoggerFactory.getLogger(TimeBankService.class);


    public TimeBankDTO getBalance(Long employeeId){
        logger.info("Getting balance for employee id: {}", employeeId);
        var employee = employeeRepository.findById(employeeId).orElseThrow(()->new ResourceNotFoundException("Employee not found! ID: ", employeeId));
        var timeBankDTO = mapper.toDTO(employee.getTimeBank());
        addHateoasLinks(timeBankDTO);
        return timeBankDTO;
    }

    public TimeBankDTO addHours(Long employeeId, Integer minutes){
        logger.info("Adding {} minutes to employeeId id: {}", minutes, employeeId);
        var employeePersisted = employeeRepository.findById(employeeId).orElseThrow(()->new ResourceNotFoundException("Employee not found! ID: ", employeeId));

        var timeBank = employeePersisted.getTimeBank();
        timeBank.setTotalValue(timeBank.getTotalValue() + minutes);
        timeBank.setLastUpdate(Instant.now());

        employeeRepository.save(employeePersisted);
        var timeBankDTO = mapper.toDTO(timeBank);
        addHateoasLinks(timeBankDTO);
        return timeBankDTO;
    }

   public TimeBankDTO removeHours(Long employeeId, Integer minutes){
        logger.info("Removing {} minutes to employeeId id: {}", minutes, employeeId);
        var persistedEmployee = employeeRepository.findById(employeeId).orElseThrow(()->new ResourceNotFoundException("Employee not found! ID: ", employeeId));

        var timeBank = persistedEmployee.getTimeBank();
        timeBank.setTotalValue(timeBank.getTotalValue() - minutes);
        timeBank.setLastUpdate(Instant.now());
        logger.info("Removing {} minutes to employeeId id: {}", minutes, employeeId);

       employeeRepository.save(persistedEmployee);
       var timeBankDTO = mapper.toDTO(timeBank);
       addHateoasLinks(timeBankDTO);
       return timeBankDTO;
   }

    public TimeBankDTO create(TimeBankDTO timeBank) {
        logger.info(" Creating timeBank !  ");

        timeBank.setTotalValue(0.0);
        var persistedTimeBank= TimeBankRepository.save(mapper.toEntity(timeBank));
        logger.info("TimeBank created! ID: {}", persistedTimeBank.getId());

        var timeBankDTO = mapper.toDTO(persistedTimeBank);
        addHateoasLinks(timeBankDTO);
        return timeBankDTO;
    }

    private void addHateoasLinks(TimeBankDTO dto) {
        dto.add(linkTo(methodOn(TimeBankController.class).addHours(dto.getEmployeeId(),null)).withRel("addHours").withType("POST"));
        dto.add(linkTo(methodOn(TimeBankController.class).removeHours(dto.getEmployeeId(),null)).withRel("removeHours").withType("POST"));
        dto.add(linkTo(methodOn(TimeBankController.class).getBalance(dto.getEmployeeId())).withRel("getBalance").withType("GET"));
    }
}


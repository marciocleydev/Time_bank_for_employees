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
    private TimeBankRepository timeBankRepository;
    @Autowired
    private TimeBankMapper mapper;

    private static final Logger LOGGER = LoggerFactory.getLogger(TimeBankService.class);


    public TimeBankDTO getBalance(Long employeeId){
        LOGGER.info("Getting balance for employee id: {}", employeeId);
        var employee = employeeRepository.findById(employeeId).orElseThrow(()->new ResourceNotFoundException("Employee not found! ID: ", employeeId));
        var timeBankDTO = mapper.toDTO(employee.getTimeBank());
        addHateoasLinks(timeBankDTO);
        return timeBankDTO;
    }

    public TimeBankDTO addHours(Long employeeId, Integer minutes){
        LOGGER.info("Adding {} minutes to employee id: {}", minutes, employeeId);
        return updateTimeBank(employeeId, minutes, 1);
    }

   public TimeBankDTO removeHours(Long employeeId, Integer minutes){
        LOGGER.info("Removing {} minutes to employee id: {}", minutes, employeeId);
       return updateTimeBank(employeeId, minutes, -1);
   }
   private TimeBankDTO updateTimeBank(Long employeeId, Integer minutes, Integer operation){
       var persistedEmployee = employeeRepository.findById(employeeId).orElseThrow(()->new ResourceNotFoundException("Employee not found! ID: ", employeeId));
       var timeBank = persistedEmployee.getTimeBank();

       if(operation == -1){
           timeBank.setTotalValue(timeBank.getTotalValue() - minutes);
       }else {
           timeBank.setTotalValue(timeBank.getTotalValue() + minutes);
       }
       timeBank.setLastUpdate(Instant.now());

       employeeRepository.save(persistedEmployee);
       var timeBankDTO = mapper.toDTO(timeBank);
       addHateoasLinks(timeBankDTO);
       return timeBankDTO;
   }

    public TimeBankDTO create(TimeBankDTO timeBankDTO) {
        LOGGER.info(" Creating timeBankDTO !  ");

        var entity = mapper.toEntity(timeBankDTO);
        entity.setTotalValue(0);
        entity.setLastUpdate(Instant.now());

        var persisted = timeBankRepository.save(entity);

        LOGGER.info("TimeBank created! ID: {}", persisted.getId());

        var dto = mapper.toDTO(persisted);
        addHateoasLinks(dto);
        return dto;
    }

    private void addHateoasLinks(TimeBankDTO dto) {
        dto.add(linkTo(methodOn(TimeBankController.class).addHours(dto.getEmployeeId(),null)).withRel("addHours").withType("POST"));
        dto.add(linkTo(methodOn(TimeBankController.class).removeHours(dto.getEmployeeId(),null)).withRel("removeHours").withType("POST"));
        dto.add(linkTo(methodOn(TimeBankController.class).getBalance(dto.getEmployeeId())).withRel("getBalance").withType("GET"));
    }
}


package com.marciocleydev.Time_bank_for_employees.services;

import com.marciocleydev.Time_bank_for_employees.DTO.EmployeeDTO;
import com.marciocleydev.Time_bank_for_employees.entities.Employee;
import com.marciocleydev.Time_bank_for_employees.exceptions.DataIntegrityException;
import com.marciocleydev.Time_bank_for_employees.exceptions.ResourceNotFoundException;
import com.marciocleydev.Time_bank_for_employees.mapper.EmployeeMapper;
import com.marciocleydev.Time_bank_for_employees.repositories.EmployeeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmployeeService  {

    @Autowired
    private EmployeeRepository repository;
    @Autowired
    private EmployeeMapper mapper;

    private static final Logger logger = LoggerFactory.getLogger(EmployeeService.class);

    public List<EmployeeDTO> findAll() {
        logger.info("Finding all employees");
        return mapper.toDTOList(repository.findAll());
    }

    public EmployeeDTO findById(Long id) {
        logger.info("Finding employee by id {}", id);
        Employee employee =  repository.findById(id).orElseThrow(()->new ResourceNotFoundException("Employee not found! ID: ", id));
        return mapper.toDTO(employee);
    }

    public EmployeeDTO create(EmployeeDTO employee) {
        logger.info(" Trying to create an employee !  ");
        if (employee.getId() != null) {
            throw new DataIntegrityException("Employee ID must be null to create a new employee");
        }
        var persistedEmployee= repository.save(mapper.toEntity(employee));
        logger.info("Employee created! ID: {}", persistedEmployee.getId());
        return mapper.toDTO(persistedEmployee);
    }

    public EmployeeDTO update(EmployeeDTO newEmployee, Long id) {
        logger.info("Updating employee! ID: {}", id);

        Employee oldEmployee = repository.findById(id).orElseThrow(()->new ResourceNotFoundException("Employee not found! ID: ", id));
        updateFactory(newEmployee, oldEmployee);
        return mapper.toDTO(repository.save(oldEmployee));
    }

    private void updateFactory(EmployeeDTO newEmployee, Employee oldEmployee){
        oldEmployee.setName(newEmployee.getName());
        oldEmployee.setPis(newEmployee.getPis());
    }

    public void deleteById(Long id) {
        logger.info(" Trying to delete employee! ID: {}", id);
        try {
            repository.findById(id).orElseThrow(()->new ResourceNotFoundException("Employee not found! ID: ", id));
            repository.deleteById(id);
            logger.info("Employee deleted! ID: {}", id);
        }
        catch (DataIntegrityViolationException e) {
            throw new DataIntegrityException(e.getMessage());
        }
    }
}


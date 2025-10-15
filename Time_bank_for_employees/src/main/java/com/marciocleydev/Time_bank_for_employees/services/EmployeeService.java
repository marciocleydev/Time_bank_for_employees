package com.marciocleydev.Time_bank_for_employees.services;

import com.marciocleydev.Time_bank_for_employees.entities.Employee;
import com.marciocleydev.Time_bank_for_employees.exceptions.DataIntegrityException;
import com.marciocleydev.Time_bank_for_employees.exceptions.ResourceNotFoundException;
import com.marciocleydev.Time_bank_for_employees.repositories.EmployeeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmployeeService  {

    @Autowired
    private EmployeeRepository repository;

    private static final Logger logger = LoggerFactory.getLogger(EmployeeService.class);

    public List<Employee> findAll() {
        logger.info("Finding all employees");
        return repository.findAll();
    }

    public Employee findById(Long id) {
        logger.info("Finding employee by id {}", id);
        return repository.findById(id).orElseThrow(()->new ResourceNotFoundException("Employee not found", id));
    }

    public Employee create(Employee employee) {
        logger.info("Creating employee! ID: {}", employee.getId());
        if (employee.getId() != null) {
            throw new DataIntegrityException("Employee ID must be null to create a new employee");
        }
        return repository.save(employee);
    }
    public Employee update(Employee employee, Long id) {
        logger.info("Updating employee! ID: {}", employee.getId());
        Employee persistedEmployee = findById(id);
        persistedEmployee.setName(employee.getName());
        persistedEmployee.setPis(employee.getPis());
        return repository.save(persistedEmployee);
    }
    public void deleteById(Long id) {
        logger.info(" Trying to delete employee! ID: {}", id);
        try {
            repository.findById(id).orElseThrow(()->new ResourceNotFoundException("Employee not found", id));
            repository.deleteById(id);
            logger.info("Employee deleted! ID: {}", id);
        }
        catch (DataIntegrityViolationException e) {
            throw new DataIntegrityException(e.getMessage());
        }
    }
}


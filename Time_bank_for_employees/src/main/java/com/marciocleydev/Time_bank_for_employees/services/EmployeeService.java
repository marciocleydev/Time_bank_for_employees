package com.marciocleydev.Time_bank_for_employees.services;

import com.marciocleydev.Time_bank_for_employees.DTO.EmployeeDTO;
import com.marciocleydev.Time_bank_for_employees.DTO.TimeBankDTO;
import com.marciocleydev.Time_bank_for_employees.controllers.EmployeeController;
import com.marciocleydev.Time_bank_for_employees.entities.Employee;
import com.marciocleydev.Time_bank_for_employees.exceptions.DataIntegrityException;
import com.marciocleydev.Time_bank_for_employees.exceptions.ResourceNotFoundException;
import com.marciocleydev.Time_bank_for_employees.mapper.EmployeeMapper;
import com.marciocleydev.Time_bank_for_employees.repositories.EmployeeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Service;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
public class EmployeeService  {

    @Autowired
    private EmployeeRepository repository;
    @Autowired
    private EmployeeMapper mapper;

    @Autowired
    private TimeBankService timeBankService;

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    PagedResourcesAssembler<EmployeeDTO> assembler;

    private static final Logger logger = LoggerFactory.getLogger(EmployeeService.class);

    public Employee findEntityById(Long id) {
        logger.info("Finding an employee by id {}", id);
        return repository.findById(id).orElseThrow(()->new ResourceNotFoundException("Employee not found! ID: ", id));
    }

    public PagedModel<EntityModel<EmployeeDTO>> findAll(Pageable pageable) {
        logger.info("Finding all employees");

        var employees = repository.findAll(pageable);

        var employeesWithLinks = employees.map(employee -> {
            var employeeDTO = mapper.toDTO(employee);
            addHateoasLinks(employeeDTO);
            return employeeDTO;
        });

        Link findAllLink = WebMvcLinkBuilder.linkTo(
                WebMvcLinkBuilder.methodOn(EmployeeController.class)
                        .findAll(
                                pageable.getPageNumber(),
                                pageable.getPageSize(),
                                pageable.getSort().toString()
                        )
        ).withSelfRel();
        return assembler.toModel(employeesWithLinks, findAllLink);
    }

    public EmployeeDTO findById(Long id) {
        logger.info("Finding employee by id {}", id);
        Employee employee =  repository.findById(id).orElseThrow(()->new ResourceNotFoundException("Employee not found! ID: ", id));

        var employeeDTO = mapper.toDTO(employee);
        addHateoasLinks(employeeDTO);
        return employeeDTO;
    }

    public EmployeeDTO create(EmployeeDTO employeeDTO) {
        logger.info(" Trying to create an employeeDTO !  ");
        if (employeeDTO.getId() != null) {
            throw new DataIntegrityException("Employee ID must be null to create a new employeeDTO");
        }

        var persistedTimeBank = timeBankService.create(new TimeBankDTO());
        employeeDTO.setTimeBankId(persistedTimeBank.getId());

        var persistedEmployee= repository.save(mapper.toEntity(employeeDTO));
        logger.info("Employee created! ID: {}", persistedEmployee.getId());

        var persistedEmployeeDTO = mapper.toDTO(persistedEmployee);
        addHateoasLinks(persistedEmployeeDTO);
        return persistedEmployeeDTO;
    }

    public EmployeeDTO update(EmployeeDTO newEmployee, Long id) {
        logger.info("Updating employee! ID: {}", id);

        Employee oldEmployee = repository.findById(id).orElseThrow(()->new ResourceNotFoundException("Employee not found! ID: ", id));
        updateFactory(newEmployee, oldEmployee);
        repository.save(oldEmployee);
        logger.info("Employee updated! ID: {}", id);

        var employeeDTO = mapper.toDTO(oldEmployee);
        addHateoasLinks(employeeDTO);
        return employeeDTO;
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

    private void addHateoasLinks(EmployeeDTO dto) {
        dto.add(linkTo(methodOn(EmployeeController.class).findAll(1, 12, "asc")).withRel("findAll").withType("GET"));
        dto.add(linkTo(methodOn(EmployeeController.class).findById(dto.getId())).withSelfRel().withType("GET"));
        dto.add(linkTo(methodOn(EmployeeController.class).create(dto)).withRel("create").withType("POST"));
        dto.add(linkTo(methodOn(EmployeeController.class).update(dto.getId(),dto)).withRel("update").withType("PUT"));
        dto.add(linkTo(methodOn(EmployeeController.class).deleteById(dto.getId())).withRel("delete").withType("DELETE"));
    }
}


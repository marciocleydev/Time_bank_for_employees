package com.marciocleydev.Time_bank_for_employees.services;

import com.marciocleydev.Time_bank_for_employees.DTO.TimeSheetDTO;
import com.marciocleydev.Time_bank_for_employees.controllers.TimeSheetController;
import com.marciocleydev.Time_bank_for_employees.entities.TimeSheet;
import com.marciocleydev.Time_bank_for_employees.exceptions.DataIntegrityException;
import com.marciocleydev.Time_bank_for_employees.exceptions.ResourceNotFoundException;
import com.marciocleydev.Time_bank_for_employees.mapper.TimeSheetMapper;
import com.marciocleydev.Time_bank_for_employees.repositories.TimeSheetRepository;
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
public class TimeSheetService {

    @Autowired
    private TimeSheetRepository repository;
    @Autowired
    private TimeSheetMapper mapper;

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    PagedResourcesAssembler<TimeSheetDTO> assembler;

    private static final Logger logger = LoggerFactory.getLogger(TimeSheetService.class);

    public PagedModel<EntityModel<TimeSheetDTO>> findAll(Pageable pageable) {
        logger.info("Finding all timeSheetList");

        var timeSheetList = repository.findAll(pageable);

        var timeSheetListWithLinks = timeSheetList.map(timeSheet -> {
            var timeSheetDTO = mapper.toDTO(timeSheet);
            addHateoasLinks(timeSheetDTO);
            return timeSheetDTO;
        });

        Link findAllLink = WebMvcLinkBuilder.linkTo(
                WebMvcLinkBuilder.methodOn(TimeSheetController.class)
                        .findAll(
                                pageable.getPageNumber(),
                                pageable.getPageSize(),
                                pageable.getSort().toString()
                        )
        ).withSelfRel();
        return assembler.toModel(timeSheetListWithLinks, findAllLink);
    }

    public TimeSheetDTO findById(Long id) {
        logger.info("Finding timeSheet by id {}", id);
        TimeSheet timeSheet =  repository.findById(id).orElseThrow(()->new ResourceNotFoundException("TimeSheet not found! ID: ", id));

        var timeSheetDTO = mapper.toDTO(timeSheet);
        addHateoasLinks(timeSheetDTO);
        return timeSheetDTO;
    }

    public TimeSheetDTO create(TimeSheetDTO timeSheet) {
        logger.info(" Trying to create an timeSheet !  ");
        if (timeSheet.getId() != null) {
            throw new DataIntegrityException("TimeSheet ID must be null to create a new timeSheet");
        }
        var persistedTimeSheet= repository.save(mapper.toEntity(timeSheet));
        logger.info("TimeSheet created! ID: {}", persistedTimeSheet.getId());

        var timeSheetDTO = mapper.toDTO(persistedTimeSheet);
        addHateoasLinks(timeSheetDTO);
        return timeSheetDTO;
    }

    public TimeSheetDTO update(TimeSheetDTO newTimeSheet, Long id) {
        logger.info("Updating timeSheet! ID: {}", id);

        TimeSheet oldTimeSheet = repository.findById(id).orElseThrow(()->new ResourceNotFoundException("TimeSheet not found! ID: ", id));
        updateFactory(newTimeSheet, oldTimeSheet);
        repository.save(oldTimeSheet);
        logger.info("TimeSheet updated! ID: {}", id);

        var timeSheetDTO = mapper.toDTO(oldTimeSheet);
        addHateoasLinks(timeSheetDTO);
        return timeSheetDTO;
    }

    private void updateFactory(TimeSheetDTO newTimeSheet, TimeSheet oldTimeSheet){
        oldTimeSheet.setDateTime(newTimeSheet.getDateTime());
        oldTimeSheet.setOverTime(newTimeSheet.getOverTime());
        oldTimeSheet.setImageStorage(newTimeSheet.getImageStorage());
    }

    public void deleteById(Long id) {
        logger.info(" Trying to delete timeSheet! ID: {}", id);
        try {
            repository.findById(id).orElseThrow(()->new ResourceNotFoundException("TimeSheet not found! ID: ", id));
            repository.deleteById(id);
            logger.info("TimeSheet deleted! ID: {}", id);
        }
        catch (DataIntegrityViolationException e) {
            throw new DataIntegrityException(e.getMessage());
        }
    }

    private void addHateoasLinks(TimeSheetDTO dto) {
        dto.add(linkTo(methodOn(TimeSheetController.class).findAll(1, 12, "asc")).withRel("findAll").withType("GET"));
        dto.add(linkTo(methodOn(TimeSheetController.class).findById(dto.getId())).withSelfRel().withType("GET"));
        dto.add(linkTo(methodOn(TimeSheetController.class).create(dto)).withRel("create").withType("POST"));
        dto.add(linkTo(methodOn(TimeSheetController.class).update(dto.getId(),dto)).withRel("update").withType("PUT"));
        dto.add(linkTo(methodOn(TimeSheetController.class).deleteById(dto.getId())).withRel("delete").withType("DELETE"));
    }
}


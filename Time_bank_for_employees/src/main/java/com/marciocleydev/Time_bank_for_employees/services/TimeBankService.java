package com.marciocleydev.Time_bank_for_employees.services;

import com.marciocleydev.Time_bank_for_employees.DTO.TimeBankDTO;
import com.marciocleydev.Time_bank_for_employees.controllers.TimeBankController;
import com.marciocleydev.Time_bank_for_employees.entities.TimeBank;
import com.marciocleydev.Time_bank_for_employees.exceptions.DataIntegrityException;
import com.marciocleydev.Time_bank_for_employees.exceptions.ResourceNotFoundException;
import com.marciocleydev.Time_bank_for_employees.mapper.TimeBankMapper;
import com.marciocleydev.Time_bank_for_employees.repositories.TimeBankRepository;
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
public class TimeBankService {

    @Autowired
    private TimeBankRepository repository;
    @Autowired
    private TimeBankMapper mapper;

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    PagedResourcesAssembler<TimeBankDTO> assembler;

    private static final Logger logger = LoggerFactory.getLogger(TimeBankService.class);

    public PagedModel<EntityModel<TimeBankDTO>> findAll(Pageable pageable) {
        logger.info("Finding all timeBankList");

        var timeBankList = repository.findAll(pageable);

        var timeBankListWithLinks = timeBankList.map(timeBank -> {
            var timeBankDTO = mapper.toDTO(timeBank);
            addHateoasLinks(timeBankDTO);
            return timeBankDTO;
        });

        Link findAllLink = WebMvcLinkBuilder.linkTo(
                WebMvcLinkBuilder.methodOn(TimeBankController.class)
                        .findAll(
                                pageable.getPageNumber(),
                                pageable.getPageSize(),
                                pageable.getSort().toString()
                        )
        ).withSelfRel();
        return assembler.toModel(timeBankListWithLinks, findAllLink);
    }

    public TimeBankDTO findById(Long id) {
        logger.info("Finding timeBank by id {}", id);
        TimeBank timeBank =  repository.findById(id).orElseThrow(()->new ResourceNotFoundException("TimeBank not found! ID: ", id));

        var timeBankDTO = mapper.toDTO(timeBank);
        addHateoasLinks(timeBankDTO);
        return timeBankDTO;
    }

    public TimeBankDTO create(TimeBankDTO timeBank) {
        logger.info(" Trying to create an timeBank !  ");
        if (timeBank.getId() != null) {
            throw new DataIntegrityException("TimeBank ID must be null to create a new timeBank");
        }
        var persistedTimeBank= repository.save(mapper.toEntity(timeBank));
        logger.info("TimeBank created! ID: {}", persistedTimeBank.getId());

        var timeBankDTO = mapper.toDTO(persistedTimeBank);
        addHateoasLinks(timeBankDTO);
        return timeBankDTO;
    }

    public TimeBankDTO update(TimeBankDTO newTimeBank, Long id) {
        logger.info("Updating timeBank! ID: {}", id);

        TimeBank oldTimeBank = repository.findById(id).orElseThrow(()->new ResourceNotFoundException("TimeBank not found! ID: ", id));
        updateFactory(newTimeBank, oldTimeBank);
        repository.save(oldTimeBank);
        logger.info("TimeBank updated! ID: {}", id);

        var timeBankDTO = mapper.toDTO(oldTimeBank);
        addHateoasLinks(timeBankDTO);
        return timeBankDTO;
    }

    private void updateFactory(TimeBankDTO newTimeBank, TimeBank oldTimeBank){
        oldTimeBank.setTotalValue(newTimeBank.getTotalValue());
        oldTimeBank.setLastUpdate(newTimeBank.getLastUpdate());
    }

    public void deleteById(Long id) {
        logger.info(" Trying to delete timeBank! ID: {}", id);
        try {
            repository.findById(id).orElseThrow(()->new ResourceNotFoundException("TimeBank not found! ID: ", id));
            repository.deleteById(id);
            logger.info("TimeBank deleted! ID: {}", id);
        }
        catch (DataIntegrityViolationException e) {
            throw new DataIntegrityException(e.getMessage());
        }
    }

    private void addHateoasLinks(TimeBankDTO dto) {
        dto.add(linkTo(methodOn(TimeBankController.class).findAll(1, 12, "asc")).withRel("findAll").withType("GET"));
        dto.add(linkTo(methodOn(TimeBankController.class).findById(dto.getId())).withSelfRel().withType("GET"));
        dto.add(linkTo(methodOn(TimeBankController.class).create(dto)).withRel("create").withType("POST"));
        dto.add(linkTo(methodOn(TimeBankController.class).update(dto.getId(),dto)).withRel("update").withType("PUT"));
        dto.add(linkTo(methodOn(TimeBankController.class).deleteById(dto.getId())).withRel("delete").withType("DELETE"));
    }
}


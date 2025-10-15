package com.marciocleydev.Time_bank_for_employees.services;

import com.marciocleydev.Time_bank_for_employees.entities.TimeSheet;
import com.marciocleydev.Time_bank_for_employees.exceptions.DataIntegrityException;
import com.marciocleydev.Time_bank_for_employees.exceptions.ResourceNotFoundException;
import com.marciocleydev.Time_bank_for_employees.repositories.TimeSheetRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TimeSheetService {

    @Autowired
    private TimeSheetRepository repository;

    private static final Logger logger = LoggerFactory.getLogger(TimeSheetService.class);

    public List<TimeSheet> findAll() {
        logger.info("Finding all timeSheets");
        return repository.findAll();
    }

    public TimeSheet findById(Long id) {
        logger.info("Finding timeSheet by id {}", id);
        return repository.findById(id).orElseThrow(()->new ResourceNotFoundException("TimeSheet not found", id));
    }

    public TimeSheet create(TimeSheet timeSheet) {
        logger.info("Creating timeSheet! ID: {}", timeSheet.getId());
        if (timeSheet.getId() != null) {
            throw new DataIntegrityException("TimeSheet ID must be null to create a new timeSheet");
        }
        return repository.save(timeSheet);
    }
    public TimeSheet update(TimeSheet timeSheet, Long id) {
        logger.info("Updating timeSheet! ID: {}", timeSheet.getId());
        TimeSheet persistedTimeSheet = findById(id);
        persistedTimeSheet.setDateTime(timeSheet.getDateTime());
        persistedTimeSheet.setOverTime(timeSheet.getOverTime());
        persistedTimeSheet.setImageStorage(timeSheet.getImageStorage());
        return repository.save(persistedTimeSheet);
    }
    public void deleteById(Long id) {
        logger.info(" Trying to delete timeSheet! ID: {}", id);
        try {
            repository.findById(id).orElseThrow(()->new ResourceNotFoundException("TimeSheet not found", id));
            repository.deleteById(id);
            logger.info("TimeSheet deleted! ID: {}", id);
        }
        catch (DataIntegrityViolationException e) {
            throw new DataIntegrityException(e.getMessage());
        }
    }
}


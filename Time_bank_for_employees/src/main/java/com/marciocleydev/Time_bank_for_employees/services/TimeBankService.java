package com.marciocleydev.Time_bank_for_employees.services;

import com.marciocleydev.Time_bank_for_employees.entities.TimeBank;
import com.marciocleydev.Time_bank_for_employees.exceptions.DataIntegrityException;
import com.marciocleydev.Time_bank_for_employees.exceptions.ResourceNotFoundException;
import com.marciocleydev.Time_bank_for_employees.repositories.TimeBankRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TimeBankService {

    @Autowired
    private TimeBankRepository repository;

    private static final Logger logger = LoggerFactory.getLogger(TimeBankService.class);

    public List<TimeBank> findAll() {
        logger.info("Finding all timeBanks");
        return repository.findAll();
    }

    public TimeBank findById(Long id) {
        logger.info("Finding timeBank by id {}", id);
        return repository.findById(id).orElseThrow(()->new ResourceNotFoundException("TimeBank not found", id));
    }

    public TimeBank create(TimeBank timeBank) {
        logger.info("Creating timeBank! ID: {}", timeBank.getId());
        if (timeBank.getId() != null) {
            throw new DataIntegrityException("TimeBank ID must be null to create a new timeBank");
        }
        return repository.save(timeBank);
    }
    public TimeBank update(TimeBank timeBank, Long id) {
        logger.info("Updating timeBank! ID: {}", timeBank.getId());

        TimeBank persistedTimeBank = findById(id);
        persistedTimeBank.setTotalValue(timeBank.getTotalValue());
        persistedTimeBank.setLastUpdate(timeBank.getLastUpdate());
        return repository.save(persistedTimeBank);
    }
    public void deleteById(Long id) {
        logger.info(" Trying to delete timeBank! ID: {}", id);
        try {
            repository.findById(id).orElseThrow(()->new ResourceNotFoundException("TimeBank not found", id));
            repository.deleteById(id);
            logger.info("TimeBank deleted! ID: {}", id);
        }
        catch (DataIntegrityViolationException e) {
            throw new DataIntegrityException(e.getMessage());
        }
    }
}


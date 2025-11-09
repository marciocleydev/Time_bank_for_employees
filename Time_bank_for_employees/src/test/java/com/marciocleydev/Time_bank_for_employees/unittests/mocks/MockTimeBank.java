package com.marciocleydev.Time_bank_for_employees.unittests.mocks;

import com.marciocleydev.Time_bank_for_employees.DTO.TimeBankDTO;
import com.marciocleydev.Time_bank_for_employees.entities.TimeBank;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class MockTimeBank {
    private static MockEmployee mockEmployee = new MockEmployee();

    public TimeBank mockEntity(Integer number){
        TimeBank timeBank = new TimeBank();
        timeBank.setId(number.longValue());
        timeBank.setTotalValue(number * 100);
        timeBank.setLastUpdate(Instant.now());
        timeBank.setEmployee(mockEmployee.mockEntity(number));
        return timeBank;
    }

    public TimeBankDTO mockDTO(Integer number){
        TimeBankDTO timeBankDTO = new TimeBankDTO();
        timeBankDTO.setId(number.longValue());
        timeBankDTO.setTotalValue(number * 100);
        timeBankDTO.setLastUpdate(Instant.now());
        timeBankDTO.setEmployeeId(2L);
        return timeBankDTO;
    }

    public List<TimeBank> mockEntityList(){
        List<TimeBank> timeBankList = new ArrayList<>();
        for(int i = 0; i < 20; i++){
            timeBankList.add(mockEntity(i));
        }
        return timeBankList;
    }

    public List<TimeBankDTO> mockDTOList(){
        List<TimeBankDTO> timeBankDTOList = new ArrayList<>();
        for(int i = 0; i < 20; i++){
            timeBankDTOList.add(mockDTO(i));
        }
        return timeBankDTOList;
    }
}

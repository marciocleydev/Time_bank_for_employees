package com.marciocleydev.Time_bank_for_employees.mapper;

import com.marciocleydev.Time_bank_for_employees.DTO.TimeBankDTO;
import com.marciocleydev.Time_bank_for_employees.entities.Employee;
import com.marciocleydev.Time_bank_for_employees.entities.TimeBank;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")//Ela diz para o MapStruct gerar um mapper que é reconhecido como um bean do Spring, ou seja, você pode injetar esse mapper usando @Autowired
public interface TimeBankMapper {

    @Mapping(source = "employee.id", target = "employeeId")
    TimeBankDTO toDTO(TimeBank timeBank);

    @Mapping(source = "employeeId", target = "employee")
    TimeBank toEntity(TimeBankDTO timeBankDTO);

    List<TimeBankDTO> toDTOList(List<TimeBank> timeBankList);

    List<TimeBank> toEntityList(List<TimeBankDTO> timeBankDTOList);

    // méthod auxiliar usado pelo MapStruct
    default Employee mapEmployee(Long employeeId) {
        if (employeeId == null) return null;
        Employee employee = new Employee();
        employee.setId(employeeId);
        return employee;
    }
}

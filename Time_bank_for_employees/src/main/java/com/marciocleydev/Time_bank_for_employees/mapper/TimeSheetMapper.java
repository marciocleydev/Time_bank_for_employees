package com.marciocleydev.Time_bank_for_employees.mapper;

import com.marciocleydev.Time_bank_for_employees.DTO.TimeSheetDTO;
import com.marciocleydev.Time_bank_for_employees.entities.Employee;
import com.marciocleydev.Time_bank_for_employees.entities.TimeSheet;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")//Ela diz para o MapStruct gerar um mapper que é reconhecido como um bean do Spring, ou seja, você pode injetar esse mapper usando @Autowired
public interface TimeSheetMapper {


    @Mapping(source = "employee.id", target = "employeeId")
    TimeSheetDTO toDTO(TimeSheet timeSheet);

    @Mapping(source = "employeeId", target = "employee")
    TimeSheet toEntity(TimeSheetDTO timeSheetDTO);

    List<TimeSheetDTO> toDTOList(List<TimeSheet> timeSheetList);

    List<TimeSheet> toEntityList(List<TimeSheetDTO> timeSheetDTOList);

    // méthod auxiliar usado pelo MapStruct
    default Employee mapEmployee(Long employeeId) {
        if (employeeId == null) return null;
        Employee employee = new Employee();
        employee.setId(employeeId);
        return employee;
    }
}

package com.marciocleydev.Time_bank_for_employees.mapper;

import com.marciocleydev.Time_bank_for_employees.DTO.EmployeeDTO;
import com.marciocleydev.Time_bank_for_employees.entities.Employee;
import com.marciocleydev.Time_bank_for_employees.entities.TimeBank;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")//Ela diz para o MapStruct gerar um mapper que é reconhecido como um bean do Spring, ou seja, você pode injetar esse mapper usando @Autowired
public interface EmployeeMapper {

    @Mapping(source = "timeBank.id", target = "timeBankId")
    EmployeeDTO toDTO(Employee employee);

    @Mapping(source = "timeBankId", target = "timeBank")
    Employee toEntity(EmployeeDTO employeeDTO);

    List<EmployeeDTO> toDTOList(List<Employee> employeeList);

    List<Employee> toEntityList(List<EmployeeDTO> employeeDTOList);

    // méthod auxiliar usado pelo MapStruct
    default TimeBank mapEmployee(Long timeBankId) {
        if (timeBankId == null) return null;
        TimeBank timeBank = new TimeBank();
        timeBank.setId(timeBankId);
        return timeBank;
    }
}

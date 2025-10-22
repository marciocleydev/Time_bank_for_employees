package com.marciocleydev.Time_bank_for_employees.unittests.mocks;

import com.marciocleydev.Time_bank_for_employees.DTO.EmployeeDTO;
import com.marciocleydev.Time_bank_for_employees.entities.Employee;

import java.util.ArrayList;
import java.util.List;

public class MockEmployee {

    public Employee mockEntity(Integer number){
        Employee employee = new Employee();
        employee.setId(number.longValue());
        employee.setName("Employee " + number);
        employee.setPis("1234567890" + number);
        return employee;
    }

    public EmployeeDTO mockDTO(Integer number){
        EmployeeDTO employeeDTO = new EmployeeDTO();
        employeeDTO.setId(number.longValue());
        employeeDTO.setName("Employee " + number);
        employeeDTO.setPis("1234567890" + number);
        return employeeDTO;
    }

    public List<Employee> mockEntityList(){
        List<Employee> employeeList = new ArrayList<>();
        for(int i = 0; i < 20; i++){
            employeeList.add(mockEntity(i));
        }
        return employeeList;
    }

    public List<EmployeeDTO> mockDTOList(){
        List<EmployeeDTO> employeeDTOList = new ArrayList<>();
        for(int i = 0; i < 20; i++){
            employeeDTOList.add(mockDTO(i));
        }
        return employeeDTOList;
    }
}

package com.marciocleydev.Time_bank_for_employees.integrationtests.dto.wrappers.json;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.marciocleydev.Time_bank_for_employees.integrationtests.dto.EmployeeDTO;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;


public class EmployeeEmbeddedDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @JsonProperty("employees")
    private List<EmployeeDTO> employees;

    public EmployeeEmbeddedDTO() {
    }
    public List<EmployeeDTO> getEmployees() {
        return employees;
    }
    public void setEmployees(List<EmployeeDTO> employees) {
        this.employees = employees;
    }
}

package com.marciocleydev.Time_bank_for_employees.integrationtests.dto.wrappers;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.marciocleydev.Time_bank_for_employees.integrationtests.dto.wrappers.json.EmployeeEmbeddedDTO;

import java.io.Serial;
import java.io.Serializable;

public class WrapperEmployeeDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @JsonProperty("_embedded")
    private EmployeeEmbeddedDTO embedded;

    public WrapperEmployeeDTO() {
    }

    public EmployeeEmbeddedDTO getEmbedded() {
        return embedded;
    }

    public void setEmbedded(EmployeeEmbeddedDTO embedded) {
        this.embedded = embedded;
    }
}

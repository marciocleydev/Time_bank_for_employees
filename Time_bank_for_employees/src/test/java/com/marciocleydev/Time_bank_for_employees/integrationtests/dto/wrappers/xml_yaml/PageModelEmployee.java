package com.marciocleydev.Time_bank_for_employees.integrationtests.dto.wrappers.xml_yaml;

import com.marciocleydev.Time_bank_for_employees.integrationtests.dto.EmployeeDTO;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@XmlRootElement
public class PageModelEmployee implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    public List<EmployeeDTO> content;

    public PageModelEmployee() {
    }
    public List<EmployeeDTO> getContent() {
        return content;
    }
    public void setContent(List<EmployeeDTO> content) {
        this.content = content;
    }
}

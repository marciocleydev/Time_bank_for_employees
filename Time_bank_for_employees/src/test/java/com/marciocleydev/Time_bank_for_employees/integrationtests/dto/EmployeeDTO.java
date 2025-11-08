package com.marciocleydev.Time_bank_for_employees.integrationtests.dto;

import jakarta.xml.bind.annotation.XmlRootElement;

import java.io.Serial;
import java.io.Serializable;

@XmlRootElement
public class EmployeeDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private Long id;
    private String name;
    private String pis;
    private Long timeBankId;

    public EmployeeDTO() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPis() {
        return pis;
    }

    public void setPis(String pis) {
        this.pis = pis;
    }

    public Long getTimeBankId() {
        return timeBankId;
    }
    public void setTimeBankId(Long timeBankId) {
        this.timeBankId = timeBankId;
    }

}

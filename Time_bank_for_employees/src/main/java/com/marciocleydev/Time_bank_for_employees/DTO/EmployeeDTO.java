package com.marciocleydev.Time_bank_for_employees.DTO;

import jakarta.persistence.Column;
import org.springframework.hateoas.RepresentationModel;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;


public class EmployeeDTO extends RepresentationModel<EmployeeDTO> implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private Long id;
    private String name;
    private String pis;

    public EmployeeDTO() {
    }
    public EmployeeDTO(Long id, String name, String pis) {
        this.id = id;
        this.name = name;
        this.pis = pis;
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

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        EmployeeDTO that = (EmployeeDTO) o;
        return Objects.equals(id, that.id) && Objects.equals(name, that.name) && Objects.equals(pis, that.pis);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, pis);
    }
}

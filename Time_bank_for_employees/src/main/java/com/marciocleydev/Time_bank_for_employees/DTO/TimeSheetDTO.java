package com.marciocleydev.Time_bank_for_employees.DTO;

import com.marciocleydev.Time_bank_for_employees.entities.Employee;
import org.springframework.hateoas.RepresentationModel;

import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

public class TimeSheetDTO extends RepresentationModel<EmployeeDTO> implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private Long id;
    private Instant dateTime;
    private Double overTime;
    private String imageStorage;
    private Employee employee;

    public TimeSheetDTO() {
    }

    public TimeSheetDTO(Instant dateTime, Employee employee, Long id, String imageStorage, Double overTime) {
        this.dateTime = dateTime;
        this.employee = employee;
        this.id = id;
        this.imageStorage = imageStorage;
        this.overTime = overTime;
    }

    public Instant getDateTime() {
        return dateTime;
    }

    public void setDateTime(Instant dateTime) {
        this.dateTime = dateTime;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getImageStorage() {
        return imageStorage;
    }

    public void setImageStorage(String imageStorage) {
        this.imageStorage = imageStorage;
    }

    public Double getOverTime() {
        return overTime;
    }

    public void setOverTime(Double overTime) {
        this.overTime = overTime;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        TimeSheetDTO that = (TimeSheetDTO) o;
        return Objects.equals(id, that.id) && Objects.equals(dateTime, that.dateTime) && Objects.equals(overTime, that.overTime) && Objects.equals(imageStorage, that.imageStorage) && Objects.equals(employee, that.employee);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), id, dateTime, overTime, imageStorage, employee);
    }
}

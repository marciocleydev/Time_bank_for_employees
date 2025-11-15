package com.marciocleydev.Time_bank_for_employees.DTO;

import com.marciocleydev.Time_bank_for_employees.entities.Employee;
import org.springframework.hateoas.RepresentationModel;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Objects;

public class TimeSheetDTO extends RepresentationModel<EmployeeDTO> implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private Long id;
    private LocalDateTime createdAt;
    private LocalDate date;
    private LocalTime expectedWorkTime;
    private LocalTime actualSheetTime;
    private Integer differenceInMinutes;
    private String photoUrl;
    private Long employeeId;

    public TimeSheetDTO() {
    }

    public LocalTime getActualSheetTime() {
        return actualSheetTime;
    }

    public void setActualSheetTime(LocalTime actualSheetTime) {
        this.actualSheetTime = actualSheetTime;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Integer getDifferenceInMinutes() {
        return differenceInMinutes;
    }

    public void setDifferenceInMinutes(Integer differenceInMinutes) {
        this.differenceInMinutes = differenceInMinutes;
    }

    public Long getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Long employeeId) {
        this.employeeId = employeeId;
    }

    public LocalTime getExpectedWorkTime() {
        return expectedWorkTime;
    }

    public void setExpectedWorkTime(LocalTime expectedWorkTime) {
        this.expectedWorkTime = expectedWorkTime;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        TimeSheetDTO that = (TimeSheetDTO) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), id);
    }
}

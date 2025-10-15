package com.marciocleydev.Time_bank_for_employees.entities;

import jakarta.persistence.*;

import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

@Entity
@Table(name = "time_sheet")
public class TimeSheet implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Instant dateTime;
    private Double overTime;
    private String imageStorage;
    @ManyToOne
    @JoinColumn(name = "employee_id")
    private Employee employee;

    public TimeSheet() {
    }

    public TimeSheet(Instant dateTime, Employee employee, Long id, String imageStorage, Double overTime) {
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
        TimeSheet timeSheet = (TimeSheet) o;
        return Objects.equals(id, timeSheet.id) && Objects.equals(dateTime, timeSheet.dateTime) && Objects.equals(overTime, timeSheet.overTime) && Objects.equals(imageStorage, timeSheet.imageStorage) && Objects.equals(employee, timeSheet.employee);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, dateTime, overTime, imageStorage, employee);
    }
}

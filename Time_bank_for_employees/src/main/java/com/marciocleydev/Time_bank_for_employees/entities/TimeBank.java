package com.marciocleydev.Time_bank_for_employees.entities;

import jakarta.persistence.*;

import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

@Entity
@Table(name = "time_bank")
public class TimeBank implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Double totalValue;
    private Instant lastUpdate;
    @OneToOne
    @JoinColumn(name = "employee_id")
    private Employee employee;

    public TimeBank() {
    }

    public TimeBank( Long id, Double totalValue, Instant lastUpdate, Employee employee) {
        this.employee = employee;
        this.id = id;
        this.lastUpdate = lastUpdate;
        this.totalValue = totalValue;
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

    public Instant getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(Instant lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public Double getTotalValue() {
        return totalValue;
    }

    public void setTotalValue(Double totalValue) {
        this.totalValue = totalValue;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        TimeBank timeBank = (TimeBank) o;
        return Objects.equals(id, timeBank.id) && Objects.equals(totalValue, timeBank.totalValue) && Objects.equals(lastUpdate, timeBank.lastUpdate) && Objects.equals(employee, timeBank.employee);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, totalValue, lastUpdate, employee);
    }
}

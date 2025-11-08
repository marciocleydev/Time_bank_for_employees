package com.marciocleydev.Time_bank_for_employees.DTO;
import com.marciocleydev.Time_bank_for_employees.entities.Employee;
import org.springframework.hateoas.RepresentationModel;

import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

public class TimeBankDTO extends RepresentationModel<TimeBankDTO> implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

        private Long id;
        private Double totalValue;
        private Instant lastUpdate;
        private Employee employee;

        public TimeBankDTO() {
        }

        public TimeBankDTO( Long id, Double totalValue, Instant lastUpdate, Employee employee) {
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
        TimeBankDTO that = (TimeBankDTO) o;
        return Objects.equals(id, that.id) && Objects.equals(totalValue, that.totalValue) && Objects.equals(lastUpdate, that.lastUpdate) && Objects.equals(employee, that.employee);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, totalValue, lastUpdate, employee);
    }
}

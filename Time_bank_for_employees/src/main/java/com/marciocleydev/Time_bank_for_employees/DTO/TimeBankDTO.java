package com.marciocleydev.Time_bank_for_employees.DTO;
import org.springframework.hateoas.RepresentationModel;

import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

public class TimeBankDTO extends RepresentationModel<TimeBankDTO> implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

        private Long id;
        private Integer totalValue;
        private Instant lastUpdate;
        private Long employeeId;

        public TimeBankDTO() {
        }

        public TimeBankDTO( Long id, Integer totalValue, Instant lastUpdate, Long employeeId) {
            this.id = id;
            this.employeeId = employeeId;
            this.lastUpdate = lastUpdate;
            this.totalValue = totalValue;
        }

    public Long getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Long employeeId) {
        this.employeeId = employeeId;
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

    public Integer getTotalValue() {
        return totalValue;
    }

    public void setTotalValue(Integer totalValue) {
        this.totalValue = totalValue;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        TimeBankDTO that = (TimeBankDTO) o;
        return Objects.equals(id, that.id) && Objects.equals(totalValue, that.totalValue) && Objects.equals(lastUpdate, that.lastUpdate) && Objects.equals(employeeId, that.employeeId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, totalValue, lastUpdate, employeeId);
    }
}

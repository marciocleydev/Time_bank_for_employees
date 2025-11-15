package com.marciocleydev.Time_bank_for_employees.entities;

import jakarta.persistence.*;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Objects;

@Entity
@Table(name = "time_sheet")
public class TimeSheet implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
    @Column(name = "date")
    private LocalDate date;
    @Column(name = "expected_work_time")
    private LocalTime expectedWorkTime;
    @Column(name = "actual_sheet_time")
    private LocalTime actualSheetTime;
    @Column(name = "difference_in_minutes")
    private Integer differenceInMinutes;
    @Column(name = "photo_url")
    private String photoUrl;
    @ManyToOne
    @JoinColumn(name = "employee_id")
    private Employee employee;

    public TimeSheet() {
    }

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
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

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
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
        TimeSheet timeSheet = (TimeSheet) o;
        return Objects.equals(id, timeSheet.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}

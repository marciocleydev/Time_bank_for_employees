package com.marciocleydev.Time_bank_for_employees.entities;

import jakarta.persistence.*;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "employee")
public class Employee implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @Column(unique = true, nullable = false)
    private String pis;
    @OneToOne(mappedBy = "employee")
    private TimeBank timeBank;
    @OneToMany(mappedBy = "employee")
    private List<TimeSheet> timeSheetList;

    public Employee() {
    }
    public Employee(String name, String pis) {
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

    public TimeBank getTimeBank() {
        return timeBank;
    }

    public void setTimeBank(TimeBank timeBank) {
        this.timeBank = timeBank;
    }
    public List<TimeSheet> getTimeSheetList() {
        return timeSheetList;
    }
    public void setTimeSheetList(List<TimeSheet> timeSheetList) {
        this.timeSheetList = timeSheetList;
    }
    public void addTimeSheet(TimeSheet timeSheet) {
        this.timeSheetList.add(timeSheet);
    }
    public void removeTimeSheet(TimeSheet timeSheet) {
        this.timeSheetList.remove(timeSheet);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Employee employee = (Employee) o;
        return Objects.equals(id, employee.id) && Objects.equals(name, employee.name) && Objects.equals(pis, employee.pis) && Objects.equals(timeBank, employee.timeBank);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, pis, timeBank);
    }
}

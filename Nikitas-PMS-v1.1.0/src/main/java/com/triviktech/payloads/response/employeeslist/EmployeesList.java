package com.triviktech.payloads.response.employeeslist;

import com.triviktech.payloads.response.employee.EmployeeInfo;

import com.triviktech.payloads.response.manager.ManagerInfo;

import java.util.List;

/**
 * Represents a response structure containing lists of employees and managers.
 * <p>
 * This DTO (Data Transfer Object) is typically used in API responses where
 * both employees and managers need to be returned together.
 * </p>
 *
 * <h2>Fields:</h2>
 * <ul>
 *   <li>{@link #employees} - List of employee details, each represented by {@link EmployeeInfo}.</li>
 *   <li>{@link #managers} - List of manager details, each represented by {@link ManagerInfo}.</li>
 * </ul>
 *
 */

public class EmployeesList {

    private List<EmployeeInfo> employees;
    private List<ManagerInfo> managers;

    public List<EmployeeInfo> getEmployees() {
        return employees;
    }

    public void setEmployees(List<EmployeeInfo> employees) {
        this.employees = employees;
    }

    public List<ManagerInfo> getManagers() {
        return managers;
    }

    public void setManagers(List<ManagerInfo> managers) {
        this.managers = managers;
    }
}

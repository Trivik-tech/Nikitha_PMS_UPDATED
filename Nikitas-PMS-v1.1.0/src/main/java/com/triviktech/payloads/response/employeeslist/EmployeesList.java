package com.triviktech.payloads.response.employeeslist;

import com.triviktech.payloads.response.employee.EmployeeInfo;

import com.triviktech.payloads.response.manager.ManagerInfo;

import java.util.List;

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

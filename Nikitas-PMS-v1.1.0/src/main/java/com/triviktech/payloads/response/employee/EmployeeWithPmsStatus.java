package com.triviktech.payloads.response.employee;

import com.triviktech.entities.department.Department;
import com.triviktech.payloads.response.department.DepartmentResponseDto;

public class EmployeeWithPmsStatus {

    private String employeeId;

    private String Name;
    private String Position;

    public String getPosition() {
        return Position;
    }

    public void setPosition(String position) {
        Position = position;
    }

    private Department department;

    private String selfCompleted;
    private String managerCompleted;
    private String pmsInitiated;

    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    public String getName() {
        return Name;
    }

    public void setName(String Name) {
        this.Name = Name;
    }

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    public String isSelfCompleted() {
        return selfCompleted;
    }

    public void setSelfCompleted(String selfCompleted) {
        this.selfCompleted = selfCompleted;
    }

    public String isManagerCompleted() {
        return managerCompleted;
    }

    public void setManagerCompleted(String managerCompleted) {
        this.managerCompleted = managerCompleted;
    }

    public String isPmsInitiated() {
        return pmsInitiated;
    }

    public void setPmsInitiated(String pmsInitiated) {
        this.pmsInitiated = pmsInitiated;
    }
}

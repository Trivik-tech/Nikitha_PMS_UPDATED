package com.triviktech.payloads.request.department;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public class DepartmentRequestDto {
    private Long departmentId;


    @NotEmpty(message = "Department name should not be empty")
    @Size(min = 2 , message = "Department name should  at least contains 3 characters")
    private String name;

    private String hrId;

    public String getHrId() {
        return hrId;
    }

    public void setHrId(String hrId) {
        this.hrId = hrId;
    }

    public Long getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(Long departmentId) {
        this.departmentId = departmentId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
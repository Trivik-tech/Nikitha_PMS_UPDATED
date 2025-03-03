package com.triviktech.payloads.request.project;


import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public class ProjectRequestDto {

    private Long projectId;


    @NotEmpty(message = "Project name should not be empty")
    @Size(min = 3, message = "Project name should at least contains 3 characters")
    private String name;


    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


}
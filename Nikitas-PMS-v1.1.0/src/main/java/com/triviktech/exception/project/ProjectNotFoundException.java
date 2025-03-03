package com.triviktech.exception.project;

public class ProjectNotFoundException extends RuntimeException{
    public ProjectNotFoundException(long projectId) {
        super("Project not found with id : "+projectId);
    }
}

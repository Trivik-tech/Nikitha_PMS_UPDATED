package com.triviktech.controllers.project;

import com.triviktech.payloads.request.project.ProjectRequestDto;
import com.triviktech.payloads.response.project.ProjectResponseDto;
import com.triviktech.services.project.ProjectService;
import com.triviktech.utilities.validation.ValidationMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;
import java.util.Set;

@RestController
public class ProjectControllerImpl implements ProjectController {

    private final ProjectService projectService;

    public ProjectControllerImpl(ProjectService projectService) {
        this.projectService = projectService;
    }


    @Override
    public ResponseEntity<?> registerProject(ProjectRequestDto projectRequestDto, BindingResult bindingResult) {

        if (bindingResult.hasErrors()){
            ValidationMessage validationMessage = new ValidationMessage();
            validationMessage.setMessage(Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage());
            validationMessage.setStatus(HttpStatus.NOT_ACCEPTABLE);
            return new ResponseEntity<>(validationMessage, HttpStatus.NOT_ACCEPTABLE);
        }

        return new ResponseEntity<>(projectService.registerProject(projectRequestDto), HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<Set<ProjectResponseDto>> listOfProjects() {
        return ResponseEntity.ok(projectService.listOfProjects());
    }

    @Override
    public ResponseEntity<?> updateProject(ProjectRequestDto projectRequestDto, long projectId, BindingResult bindingResult) {

        if (bindingResult.hasErrors()){
            ValidationMessage validationMessage = new ValidationMessage();
            validationMessage.setMessage(Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage());
            validationMessage.setStatus(HttpStatus.NOT_ACCEPTABLE);
            return new ResponseEntity<>(validationMessage, HttpStatus.NOT_ACCEPTABLE);
        }

        return ResponseEntity.ok(projectService.updateProject(projectRequestDto,projectId));
    }

    @Override
    public ResponseEntity<ProjectResponseDto> getProject(long projectId) {
        return ResponseEntity.ok(projectService.getProject(projectId));
    }
}

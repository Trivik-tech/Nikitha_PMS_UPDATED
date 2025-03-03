package com.triviktech.controllers.project;

import com.triviktech.payloads.request.project.ProjectRequestDto;
import com.triviktech.payloads.response.project.ProjectResponseDto;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RequestMapping("/api/v1/pms/project")
public interface ProjectController {

    @PostMapping("/register-project")
    ResponseEntity<?> registerProject(@Valid @RequestBody ProjectRequestDto projectRequestDto, BindingResult bindingResult);

    @GetMapping("/projects-list")
    ResponseEntity<Set<ProjectResponseDto>> listOfProjects();

    @PutMapping("/update-project/{projectId}")
    ResponseEntity<?> updateProject(@Valid @RequestBody ProjectRequestDto projectRequestDto, @PathVariable long projectId, BindingResult bindingResult);

    @GetMapping("/{projectId}")
    ResponseEntity<ProjectResponseDto> getProject(@PathVariable long projectId);

}

package com.triviktech.services.project;

import com.triviktech.entities.project.Project;
import com.triviktech.exception.project.ProjectNotFoundException;
import com.triviktech.payloads.request.project.ProjectRequestDto;
import com.triviktech.payloads.response.project.ProjectResponseDto;
import com.triviktech.repositories.project.ProjectRepository;
import com.triviktech.utilities.entitydtoconversion.EntityDtoConversion;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ProjectServiceImpl implements ProjectService {

    private final ProjectRepository projectRepository;
    private final EntityDtoConversion entityDtoConversion;
    private static final Logger logger = LoggerFactory.getLogger(ProjectServiceImpl.class);

    public ProjectServiceImpl(ProjectRepository projectRepository, EntityDtoConversion entityDtoConversion) {
        this.projectRepository = projectRepository;
        this.entityDtoConversion = entityDtoConversion;
    }

    @Override
    public ProjectResponseDto registerProject(ProjectRequestDto projectRequestDto) {
        Project project = entityDtoConversion.dtoToEntityConversion(projectRequestDto, Project.class);
        Project saved = projectRepository.save(project);
        logger.info("Project Registered: {}", saved.getProjectId());
        return entityDtoConversion.entityToDtoConversion(saved, ProjectResponseDto.class);
    }

    @Override
    public Set<ProjectResponseDto> listOfProjects() {
        return projectRepository.findAll().stream()
                .map(project -> entityDtoConversion.entityToDtoConversion(project, ProjectResponseDto.class))
                .collect(Collectors.toSet());
    }

    @Override
    public ProjectResponseDto updateProject(ProjectRequestDto projectRequestDto, long projectId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> {
                    logger.error("Project Not Found: {}", projectId);
                    return new ProjectNotFoundException(projectId);
                });
        project.setName(projectRequestDto.getName());
        Project updatedProject = projectRepository.save(project);
        logger.info("Project Updated: {}", projectId);
        return entityDtoConversion.entityToDtoConversion(updatedProject, ProjectResponseDto.class);
    }

    @Override
    public ProjectResponseDto getProject(long projectId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> {
                    logger.error("Project Not Found: {}", projectId);
                    return new ProjectNotFoundException(projectId);
                });
        return entityDtoConversion.entityToDtoConversion(project, ProjectResponseDto.class);
    }
}

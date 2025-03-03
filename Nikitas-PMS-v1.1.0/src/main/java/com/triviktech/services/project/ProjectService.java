package com.triviktech.services.project;

import com.triviktech.payloads.request.project.ProjectRequestDto;
import com.triviktech.payloads.response.project.ProjectResponseDto;

import java.util.Set;

public interface ProjectService {

    ProjectResponseDto registerProject(ProjectRequestDto projectRequestDto);

    Set<ProjectResponseDto> listOfProjects();

    ProjectResponseDto updateProject(ProjectRequestDto projectRequestDto,long projectId);

    ProjectResponseDto getProject(long projectId);
}

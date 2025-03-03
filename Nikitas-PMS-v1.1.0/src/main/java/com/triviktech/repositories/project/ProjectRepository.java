package com.triviktech.repositories.project;

import com.triviktech.entities.project.Project;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectRepository extends JpaRepository<Project, Long> {
}
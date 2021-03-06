package ru.itmo.blps.controllers;

import lombok.AllArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;
import ru.itmo.blps.DAO.entities.Project;
import ru.itmo.blps.DAO.entities.User;
import ru.itmo.blps.DAO.mappers.ProjectMapper;
import ru.itmo.blps.DAO.mappers.UserMapper;
import ru.itmo.blps.services.ProjectService;

import java.util.List;

@RestController
@RequestMapping("/projects")
@AllArgsConstructor
public class ProjectController {
    private final ProjectMapper projectMapper;
    private final ProjectService projectService;
    private final UserMapper userMapper;

    @GetMapping("/")
    @Secured({"ROLE_ANONYMOUS", "ROLE_REGULAR"})
    List<Project> allProducts() {
        return projectService.getAllProjects();
    }

    @GetMapping("/{id}")
    @Secured({"ROLE_ANONYMOUS", "ROLE_REGULAR"})
    Project getProjectById(@PathVariable Integer id) {
        return projectMapper.findProjectById(id);
    }

    @PutMapping("/")
    @Secured({"ROLE_REGULAR"})
    Project createProject(@RequestBody Project project, Authentication authentication) throws AuthenticationException {
        User user = userMapper.findUserByLogin(authentication.getName());
        projectService.createProject(user.getId(), project);
        return project;
    }

    @GetMapping("/my")
    @Secured({"ROLE_REGULAR"})
    List<Project> myProjects(Authentication authentication) {
        User user = userMapper.findUserByLogin(authentication.getName());
        return projectMapper.getInitializedProjects(user.getId());
    }
}

package com.app.authjwt.Service;

import com.app.authjwt.Model.Team;
import com.app.authjwt.Model.Workspace;
import com.app.authjwt.Repository.TeamRepository;
import com.app.authjwt.Repository.WorkspaceRepository;
import com.app.authjwt.dto.ServiceResult;
import com.app.authjwt.dto.WorkspaceDto;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WorkspaceService {

    private final WorkspaceRepository workspaceRepository;
    private final TeamRepository teamRepository;
    private static final Logger logger = LoggerFactory.getLogger(WorkspaceService.class);

    public ServiceResult<List<WorkspaceDto>> listarWorkspacesByTeam(Long idTeam) {
        List<String> errors = new ArrayList<>();
        try {
            List<Workspace> workspaces = workspaceRepository.findAllByTeamId(idTeam);
//            logger.infoworkspaces));
            if (workspaces.isEmpty()) {
                return new ServiceResult<>(Collections.emptyList());
            }

            return new ServiceResult<>(workspaces.stream()
                    .map(this::convertToDto)
                    .collect(Collectors.toList()));

        } catch (DataAccessException e) {
            logger.error("Error de base de datos al buscar workspaces", e);
            errors.add("Error de acceso a datos");
            return new ServiceResult<>(errors);
        } catch (Exception e) {
            logger.error("Error inesperado", e);
            errors.add("Error interno del servidor");
            return new ServiceResult<>(errors);
        }
    }
    private WorkspaceDto convertToDto(Workspace workspace) {
        return WorkspaceDto.builder()
                .id(workspace.getId())
                .name(workspace.getName())
                .build();
    }



    @Transactional
    public ServiceResult<WorkspaceDto> createWorkspace(WorkspaceDto request) {
        try {
            List<Long> teamIds=request.getTeamsId();
            List<Team> teams =teamRepository.findAllById(teamIds);

            if (teams.size() != teamIds.size()) {
                Set<Long> foundIds = teams.stream().map(Team::getId).collect(Collectors.toSet());
                List<Long> missingIds = teamIds.stream()
                        .filter(id -> !foundIds.contains(id))
                        .collect(Collectors.toList());
                String errorMessage = "Invalid team IDs: " + missingIds;
                return new ServiceResult<>(List.of(errorMessage));
            }

            Workspace workspace = new Workspace();
            workspace.setName(request.getName());
            workspace.setTeams(new HashSet<>(teams));

            teams.forEach(team -> team.getWorkspaces().add(workspace));
            Workspace savedWorkspace = workspaceRepository.save(workspace);
            return new ServiceResult<>(mapToDto(savedWorkspace));
        } catch (Exception e) {
            logger.error("Error creating workspace: {}", e.getMessage());
            return new ServiceResult<>(List.of("Error creating workspace: " + e.getMessage()));
        }
    }

    @Transactional(readOnly = true)
    public ServiceResult<List<WorkspaceDto>> getAllWorkspaces() {
        try {
            List<WorkspaceDto> workspaces = workspaceRepository.findAll()
                    .stream()
                    .map(this::mapToDto)
                    .collect(Collectors.toList());
            return new ServiceResult<>(workspaces);
        } catch (Exception e) {
            logger.error("Error fetching workspaces: {}", e.getMessage());
            return new ServiceResult<>(List.of("Error fetching workspaces: " + e.getMessage()));
        }
    }

    @Transactional(readOnly = true)
    public ServiceResult<WorkspaceDto> getWorkspaceById(Long id) {
        return workspaceRepository.findById(id)
                .map(workspace -> new ServiceResult<>(mapToDto(workspace)))
                .orElseGet(() -> new ServiceResult<>(List.of("Workspace not found with id: " + id)));
    }

    @Transactional
    public ServiceResult<WorkspaceDto> updateWorkspace(Long id, WorkspaceDto request) {
        try {
            return workspaceRepository.findById(id)
                    .map(workspace -> {
                        workspace.setName(request.getName());
                        Workspace updated = workspaceRepository.save(workspace);
                        return new ServiceResult<>(mapToDto(updated));
                    })
                    .orElseGet(() -> new ServiceResult<>(List.of("Workspace not found with id: " + id)));
        } catch (Exception e) {
            logger.error("Error updating workspace: {}", e.getMessage());
            return new ServiceResult<>(List.of("Error updating workspace: " + e.getMessage()));
        }
    }

    @Transactional
    public ServiceResult<Void> deleteWorkspace(Long id) {
        try {
            if (workspaceRepository.existsById(id)) {
                workspaceRepository.deleteById(id);
                return new ServiceResult<>(null);
            }
            return new ServiceResult<>(List.of("Workspace not found with id: " + id));
        } catch (Exception e) {
            logger.error("Error deleting workspace: {}", e.getMessage());
            return new ServiceResult<>(List.of("Error deleting workspace: " + e.getMessage()));
        }
    }

    private WorkspaceDto mapToDto(Workspace workspace) {
        return WorkspaceDto.builder()
                .id(workspace.getId())
                .name(workspace.getName())
                .build();
    }
}
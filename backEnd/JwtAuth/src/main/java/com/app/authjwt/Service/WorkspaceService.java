package com.app.authjwt.Service;

import com.app.authjwt.Model.Workspace;
import com.app.authjwt.Repository.WorkspaceRepository;
import com.app.authjwt.dto.ServiceResult;
import com.app.authjwt.dto.WorkspaceDto;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WorkspaceService {

    private final WorkspaceRepository workspaceRepository;
    private static final Logger logger = LoggerFactory.getLogger(WorkspaceService.class);

    public ServiceResult<List<WorkspaceDto>> listarWorkspacesByTeam(String teamName) {
        List<String> errors = new ArrayList<>();

        if (teamName == null || teamName.isBlank()) {
            errors.add("El nombre del equipo no puede estar vacío");
            return new ServiceResult<>(errors);
        }

        try {
            List<Workspace> workspaces = workspaceRepository.findAllByTeamName(teamName);

            if (workspaces.isEmpty()) {
                return new ServiceResult<>(Collections.emptyList()); // Considerar si vacío es error o no
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
            Workspace workspace = new Workspace();
            workspace.setName(request.getName());

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
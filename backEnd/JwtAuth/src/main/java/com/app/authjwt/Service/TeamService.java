package com.app.authjwt.Service;

import com.app.authjwt.Model.Team;
import com.app.authjwt.Model.User.Permission;
import com.app.authjwt.Model.User.Role;
import com.app.authjwt.Model.User.User;
import com.app.authjwt.Model.Workspace;
import com.app.authjwt.Repository.TeamRepository;
import com.app.authjwt.Repository.UserRepository;
import com.app.authjwt.Repository.WorkspaceRepository;
import com.app.authjwt.dto.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TeamService {

    private final UserRepository userRepository;
    private final TeamRepository teamRepository;
    private final WorkspaceRepository workspaceRepository;

    private static final Logger logger = LoggerFactory.getLogger(TeamService.class);

    public ServiceResult<TeamResponseDto> createTeam(TeamRequestDto request)
    {
        List<String> errors = new ArrayList<>();

        Optional<User> responsableOpt = userRepository.findById(request.getResponsable());
        if (responsableOpt.isEmpty()) {
            errors.add("No se encontró el usuario responsable con ID: " + request.getResponsable());
        }
        User responsable = responsableOpt.get();

        Set<User> users = new HashSet<>();
        if(request.getUsersIds() !=null && !request.getUsersIds().isEmpty()){
            users = new HashSet<>(userRepository.findAllById(request.getUsersIds()));
            if (users.size() != request.getUsersIds().size()) {
                Set<Long> foundIds = users.stream().map(User::getId).collect(Collectors.toSet());
                Set<Long> missingIds = request.getUsersIds().stream()
                        .filter(id -> !foundIds.contains(id))
                        .collect(Collectors.toSet());
                errors.add("No se encontraron los usuarios con IDs: " + missingIds);
            }
        }

        Set<Workspace> workspaces = new HashSet<>();
        if (request.getWorkspacesIDds() != null && !request.getWorkspacesIDds().isEmpty()) {
            workspaces = new HashSet<>(workspaceRepository.findAllById(request.getWorkspacesIDds()));

            if (workspaces.size() != request.getWorkspacesIDds().size()) {
                Set<Long> foundIds = workspaces.stream().map(Workspace::getId).collect(Collectors.toSet());
                Set<Long> missingIds = request.getWorkspacesIDds().stream()
                        .filter(id -> !foundIds.contains(id))
                        .collect(Collectors.toSet());

                errors.add("No se encontraron los workspaces con IDs: " + missingIds);
            }
        }

        try{
            Team team = Team.builder()
                    .nombre(request.getNombreTeam())
                    .responsable(responsable)
                    .users(users)
                    .workspaces(workspaces)
                    .build();
            Team teamSaved = teamRepository.save(team);

            logger.info("Usuarios guardados en el equipo: {}",
                    teamSaved.getUsers().stream().map(User::getUsername).collect(Collectors.toList()));

            Set<UserDto> userDto=new HashSet<>();
            for(User u:teamSaved.getUsers()){
                UserDto tmp=new UserDto();
                tmp.setUsermane(u.getUsername());
                tmp.setEmail(u.getEmail());
                List<RoleDto> roleDtos = u.getRoles().stream().map(role -> {
                    Set<PermissionDto> permissionDtos = role.getPermissions().stream()
                            .map(perm -> PermissionDto.builder()
                                    .name(perm.getName())
                                    .build())
                            .collect(Collectors.toSet());

                    return RoleDto.builder()
                            .name(role.getName())
                            .permissions(permissionDtos)
                            .build();
                }).collect(Collectors.toList());

                tmp.setRoles(roleDtos);
                tmp.setEnabled(true);
                userDto.add(tmp);
            }

            List<RoleDto> roleDtos = responsable.getRoles().stream().map(role -> {
                Set<PermissionDto> permissionDtos = role.getPermissions().stream()
                        .map(perm -> PermissionDto.builder()
                                .name(perm.getName())
                                .build())
                        .collect(Collectors.toSet());
                return RoleDto.builder()
                        .name(role.getName())
                        .permissions(permissionDtos)
                        .build();
            }).collect(Collectors.toList());

                UserDto responsableDto=new UserDto();
                responsableDto.setUsermane(responsable.getUsername());
                responsableDto.setEmail(responsable.getEmail());
                responsableDto.setRoles(roleDtos);
                responsableDto.setEnabled(true);

            TeamResponseDto response = TeamResponseDto.builder()
                    .nombreTeam(teamSaved.getNombre())
                    .responsable(responsableDto)
                    .users(userDto)
                    .workspaces(
                            teamSaved.getWorkspaces().stream()
                                    .map(this::mapWorkspaceToDto)
                                    .collect(Collectors.toSet())
                    )
                    .build();
            return new ServiceResult<>(response);
        }catch (Exception e){
            logger.info("Error al crear el Equipo de trabajo {}",e.getMessage());
            errors.add("Error al crear el Equipo de trabajo " + e.getMessage());
            return new ServiceResult<>(errors);
        }

    }

    @Transactional
    public ServiceResult<TeamResponseDto> getTeamById(Long id) {
        List<String> errors = new ArrayList<>();
        try {
            return teamRepository.findById(id)
                    .map(this::mapTeamToDto)
                    .map(ServiceResult::new)
                    .orElseGet(() -> {
                        errors.add("Team not found with id: " + id);
                        return new ServiceResult<>(errors);
                    });
        } catch (Exception e) {
            errors.add("Error fetching team: " + e.getMessage());
            return new ServiceResult<>(errors);
        }
    }
    @Transactional
    public ServiceResult<TeamResponseDto> updateTeam(Long id, TeamRequestDto request) {
        List<String> errors = new ArrayList<>();

        Optional<Team> teamOpt = teamRepository.findById(id);
        if (teamOpt.isEmpty()) {
            errors.add("Team not found with id: " + id);
            return new ServiceResult<>(errors);
        }

        Team team = teamOpt.get();

        // Update nombre
        if (request.getNombreTeam() != null) {
            team.setNombre(request.getNombreTeam());
        }

        // Update responsable
        if (request.getResponsable() != null) {
            Optional<User> responsableOpt = userRepository.findById(request.getResponsable());
            if (responsableOpt.isEmpty()) {
                errors.add("Responsable user not found with id: " + request.getResponsable());
                return new ServiceResult<>(errors);
            }
            team.setResponsable(responsableOpt.get());
        }

        // Update users
        if (request.getUsersIds() != null) {
            Set<User> users = new HashSet<>(userRepository.findAllById(request.getUsersIds()));
            team.setUsers(users);
        }

        // Update workspaces
        if (request.getWorkspacesIDds() != null) {
            Set<Workspace> workspaces = new HashSet<>(workspaceRepository.findAllById(request.getWorkspacesIDds()));
            team.setWorkspaces(workspaces);
        }

        try {
            Team updatedTeam = teamRepository.save(team);
            return new ServiceResult<>(mapTeamToDto(updatedTeam));
        } catch (Exception e) {
            logger.error("Error updating team: {}", e.getMessage());
            errors.add("Error updating team: " + e.getMessage());
            return new ServiceResult<>(errors);
        }
    }

    @Transactional
    public ServiceResult<Void> deleteTeam(Long id) {
        List<String> errors = new ArrayList<>();
        try {
            if (teamRepository.existsById(id)) {
                teamRepository.deleteById(id);
                return new ServiceResult<>(null);
            }
            errors.add("Team not found with id: " + id);
            return new ServiceResult<>(errors);
        } catch (Exception e) {
            logger.error("Error deleting team: {}", e.getMessage());
            errors.add("Error deleting team: " + e.getMessage());
            return new ServiceResult<>(errors);
        }
    }
    public ServiceResult<List<TeamResponseDto>> getAllTeams() {
        List<String> errors = new ArrayList<>();
        try {
            List<Team> teams = teamRepository.findAll();
            List<TeamResponseDto> response = teams.stream()
                    .map(this::mapTeamToDto)
                    .collect(Collectors.toList());
            return new ServiceResult<>(response);
        } catch (Exception e) {
            errors.add("Error fetching teams: " + e.getMessage());
            return new ServiceResult<>(errors);
        }
    }

    private TeamResponseDto mapTeamToDto(Team team) {
        // Map users to UserDto
        Set<UserDto> userDtos = team.getUsers().stream()
                .map(this::mapUserToDto)
                .collect(Collectors.toSet());

        // Map responsable to UserDto
        UserDto responsableDto = mapUserToDto(team.getResponsable());

        // Map workspaces to WorkspaceDto
        Set<WorkspaceDto> workspaceDtos = team.getWorkspaces().stream()
                .map(this::mapWorkspaceToDto)
                .collect(Collectors.toSet());

        return TeamResponseDto.builder()
                .id(team.getId())
                .nombreTeam(team.getNombre())
                .users(userDtos)
                .responsable(responsableDto)
                .workspaces(workspaceDtos)
                .build();
    }

    private UserDto mapUserToDto(User user) {
        return UserDto.builder()
                .usermane(user.getUsername())
                .Email(user.getEmail())
                .roles(mapRolesToDtos(user.getRoles()))
                .enabled(user.isEnabled())
                .build();
    }

    private List<RoleDto> mapRolesToDtos(Set<Role> roles) {
        return roles.stream()
                .map(role -> RoleDto.builder()
                        .name(role.getName())
                        .permissions(mapPermissionsToDtos(role.getPermissions()))
                        .build())
                .collect(Collectors.toList());
    }

    private Set<PermissionDto> mapPermissionsToDtos(Set<Permission> permissions) {
        return permissions.stream()
                .map(perm -> PermissionDto.builder()
                        .name(perm.getName())
                        .build())
                .collect(Collectors.toSet());
    }



    private WorkspaceDto mapWorkspaceToDto(Workspace workspace) {
        return WorkspaceDto.builder()
                .id(workspace.getId())
                .name(workspace.getName())
                .build();
    }}

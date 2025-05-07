package com.app.authjwt.Service;

import com.app.authjwt.Model.Team;
import com.app.authjwt.Model.User.User;
import com.app.authjwt.Model.Workspace;
import com.app.authjwt.Repository.TeamRepository;
import com.app.authjwt.Repository.UserRepository;
import com.app.authjwt.Repository.WorkspaceRepository;
import com.app.authjwt.dto.*;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
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
                    .workspaces(teamSaved.getWorkspaces())
                    .build();
            return new ServiceResult<>(response);
        }catch (Exception e){
            logger.info("Error al crear el Equipo de trabajo {}",e.getMessage());
            errors.add("Error al crear el Equipo de trabajo " + e.getMessage());
            return new ServiceResult<>(errors);
        }

    }
}

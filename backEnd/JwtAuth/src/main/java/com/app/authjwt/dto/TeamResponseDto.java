package com.app.authjwt.dto;

import com.app.authjwt.Model.User.User;
import com.app.authjwt.Model.Workspace;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Data
@Builder
public class TeamResponseDto {
    private String nombreTeam;
    private UserDto responsable;
    private Set<UserDto> users = new HashSet<>();
    private Set<WorkspaceDto> workspaces;
}

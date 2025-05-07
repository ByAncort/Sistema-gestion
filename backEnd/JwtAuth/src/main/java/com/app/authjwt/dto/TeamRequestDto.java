package com.app.authjwt.dto;

import com.app.authjwt.Model.User.User;
import com.app.authjwt.Model.Workspace;
import jakarta.persistence.*;
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
public class TeamRequestDto {

    private String nombreTeam;
    private Long responsable;
    private Set<Long> usersIds = new HashSet<>();
    private Set<Long> workspacesIDds = new HashSet<>();

}

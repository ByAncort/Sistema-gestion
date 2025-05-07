package com.app.authjwt.Model;

import com.app.authjwt.Model.User.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;


@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Team {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;

    @OneToOne
    private User responsable;
    @ManyToMany(mappedBy = "team")
    private Set<User> users = new HashSet<>();

    @ManyToMany
    @JoinTable(
            name = "workspace_teams",
            joinColumns = @JoinColumn(name = "team_id"),
            inverseJoinColumns = @JoinColumn(name = "workspace_id")
    )
    private Set<Workspace> workspaces = new HashSet<>();

}

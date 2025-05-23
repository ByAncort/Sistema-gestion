package com.app.authjwt.Repository;

import com.app.authjwt.Model.Team;
import com.app.authjwt.Model.Workspace;
import jdk.jfr.Registered;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TeamRepository extends JpaRepository<Team, Long>  {
    @EntityGraph(attributePaths = {
            "responsables",
            "responsables.roles",
            "responsables.roles.permissions",
            "users",
            "workspaces"
    })
    @Query("SELECT DISTINCT t FROM Team t")
    List<Team> findAllWithRelations();


}

package com.app.authjwt.Repository;

import com.app.authjwt.Model.Team;
import com.app.authjwt.Model.Workspace;
import jdk.jfr.Registered;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TeamRepository extends JpaRepository<Team, Long>  {
    @Query("SELECT DISTINCT t FROM Team t " +
            "LEFT JOIN FETCH t.responsable " +
            "LEFT JOIN FETCH t.users " +
            "LEFT JOIN FETCH t.workspaces " +
            "LEFT JOIN FETCH t.responsable.roles r " +
            "LEFT JOIN FETCH r.permissions")
    List<Team> findAllWithRelations();
}

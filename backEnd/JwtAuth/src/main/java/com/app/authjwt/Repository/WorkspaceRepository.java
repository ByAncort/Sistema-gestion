package com.app.authjwt.Repository;

import com.app.authjwt.Model.Workspace;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WorkspaceRepository extends JpaRepository<Workspace,Long> {
    @Query("SELECT w FROM Workspace w JOIN w.teams t WHERE t.nombre = :teamName")
    List<Workspace> findAllByTeamName(@Param("teamName") String teamName);
}

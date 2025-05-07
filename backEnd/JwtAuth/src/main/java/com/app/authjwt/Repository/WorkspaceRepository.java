package com.app.authjwt.Repository;

import com.app.authjwt.Model.Workspace;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WorkspaceRepository extends JpaRepository<Workspace,Long> {
}

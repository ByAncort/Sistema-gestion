package com.app.authjwt.Repository;

import com.app.authjwt.Model.Team;
import com.app.authjwt.Model.Workspace;
import jdk.jfr.Registered;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TeamRepository extends JpaRepository<Team, Long>  {
}

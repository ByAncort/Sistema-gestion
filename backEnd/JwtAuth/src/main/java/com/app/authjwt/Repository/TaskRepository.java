package com.app.authjwt.Repository;

import com.app.authjwt.Model.BoardColumn;
import com.app.authjwt.Model.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findByColumnIn(List<BoardColumn> columns);
    @Query("SELECT DISTINCT t FROM Task t JOIN t.subtasks s WHERE s.assignee.username = :username")
    List<Task> findTasksBySubtaskAssigneeUsername(@Param("username") String username);
}

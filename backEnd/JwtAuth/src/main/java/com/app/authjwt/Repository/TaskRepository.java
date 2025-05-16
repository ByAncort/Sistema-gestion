package com.app.authjwt.Repository;

import com.app.authjwt.Model.BoardColumn;
import com.app.authjwt.Model.Task;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findByColumnIn(List<BoardColumn> columns);

}

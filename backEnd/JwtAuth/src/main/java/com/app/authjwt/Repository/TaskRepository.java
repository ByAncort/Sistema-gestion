package com.app.authjwt.Repository;

import com.app.authjwt.Model.Task;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepository extends JpaRepository<Task, Long> {
}

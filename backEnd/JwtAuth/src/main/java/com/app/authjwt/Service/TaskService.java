package com.app.authjwt.Service;

import com.app.authjwt.Model.Task;
import com.app.authjwt.Repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskService {
    private final TaskRepository taskRepository;

    public List<Task> listTask(){
        return taskRepository.findAll();
    }
}

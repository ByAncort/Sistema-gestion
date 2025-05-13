package com.app.authjwt.Service;

import com.app.authjwt.Model.*;
import com.app.authjwt.Model.User.User;
import com.app.authjwt.Repository.*;
import com.app.authjwt.dto.ServiceResult;
import com.app.authjwt.dto.SubtaskDto;
import com.app.authjwt.dto.TaskDto;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Service
@RequiredArgsConstructor
public class TaskService {
    private final TaskRepository taskRepository;
    private final SubtaskRepository subtaskRepository;
    private final BoardColumnRepository boardColumnRepository;
    private final BoardRepository boardRepository;
    private final UserRepository userRepository;


    public ServiceResult<TaskDto> guardarTask(TaskDto taskDto){
        BoardColumn column = boardColumnRepository.findById(taskDto.getColumnId())
                .orElseThrow(() -> new EntityNotFoundException("Columna no encontrada"));

        Task newTask = Task.builder()
                .title(taskDto.getTitle())
                .description(taskDto.getDescription())
                .dueDate(taskDto.getDueDate())
                .column(column)
                .position(1)
                .subtasks(new ArrayList<>())
                .build();

        taskRepository.save(newTask);
        TaskDto result =taskDto;
        result.setId(newTask.getId());

        return new ServiceResult<>(result);
    }

    public ServiceResult<List<SubtaskDto>> addSubtasksToTask(Long taskId, List<SubtaskDto> subtaskDTOs) {
        try {
            Task task = taskRepository.findById(taskId)
                    .orElseThrow(() -> new EntityNotFoundException("Tarea no encontrada con ID: " + taskId));

            List<Subtask> createdSubtasks = new ArrayList<>();
            List<String> errors = new ArrayList<>();

            for (SubtaskDto dto : subtaskDTOs) {
                try {
                    // Manejar asignación de usuario
                    User assignee = null;
                    if (dto.getAssigneeId() != null) {
                        assignee = userRepository.findById(dto.getAssigneeId())
                                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado con ID: " + dto.getAssigneeId()));
                    }

                    // Construir y guardar subtarea
                    Subtask subtask = Subtask.builder()
                            .title(dto.getTitle())
                            .assignee(assignee)
                            .status(dto.getStatus() != null ? dto.getStatus() : SubtaskStatus.Pending)
                            .horas(dto.getHoras())
                            .puntos(dto.getPuntos() != null ? dto.getPuntos() : 0)
                            .dueDate(dto.getDueDate())
                            .priority(dto.getPriority() != null ? dto.getPriority() : TaskPriority.Medium)
                            .task(task)
                            .build();

                    createdSubtasks.add(subtaskRepository.save(subtask));

                } catch (EntityNotFoundException e) {
                    errors.add("Subtask '" + dto.getTitle() + "': " + e.getMessage());
                }
            }

            if (!errors.isEmpty()) {
                // Combinar errores con subtareas exitosas si se requiere
                return new ServiceResult<>(errors);
            }

            return new ServiceResult<>(subtaskDTOs);

        } catch (EntityNotFoundException e) {
            return new ServiceResult<>(List.of(e.getMessage()));
        }
    }
}
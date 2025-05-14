package com.app.authjwt.Service;

import com.app.authjwt.Model.*;
import com.app.authjwt.Model.User.User;
import com.app.authjwt.Repository.*;
import com.app.authjwt.dto.FullTaskDto;
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
//                .status(taskDto.getStatus())
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
                    User assignee = null;
                    if (dto.getAssigneeId() != null) {
                        assignee = userRepository.findById(dto.getAssigneeId())
                                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado con ID: " + dto.getAssigneeId()));
                    }


                    Subtask subtask = Subtask.builder()
                            .title(dto.getTitle())
                            .assignee(assignee)
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
                return new ServiceResult<>(errors);
            }

            return new ServiceResult<>(subtaskDTOs);

        } catch (EntityNotFoundException e) {
            return new ServiceResult<>(List.of(e.getMessage()));
        }
    }
/*    public ServiceResult<List<SubtaskDto>> buscarBoard(String idBoard){

    }*/
    public List<FullTaskDto> getFullTasksByBoardId(Long boardId) {
        // Buscar el board
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new RuntimeException("Board no encontrado con ID: " + boardId));

        List<FullTaskDto> result = new ArrayList<>();

        // Recorremos todas las columnas del board
        for (BoardColumn column : board.getColumns()) {
            for (Task task : column.getTasks()) {
                FullTaskDto dto = FullTaskDto.builder()
                        .id(task.getId())
                        .title(task.getTitle())
                        .description(task.getDescription())
                        .dueDate(task.getDueDate())
                        .position(task.getPosition())
                        .createdAt(task.getCreatedAt())
                        .startedAt(task.getStartedAt())
                        .closedAt(task.getClosedAt())
//                        .status(task.getStatus())

                        .columnId(column.getId())
                        .columnName(column.getName())
                        .columnPosition(column.getPosition())
                        .wipLimit(column.getWipLimit())

                        .boardId(board.getId())
                        .boardName(board.getName())
                        .boardCreatedAt(board.getCreatedAt())

                        .subtasks(task.getSubtasks().stream().map(subtask ->
                                FullTaskDto.SubtaskInfo.builder()
                                        .id(subtask.getId())
                                        .title(subtask.getTitle())
                                        .assigneeName(subtask.getAssignee() != null
                                                ? subtask.getAssignee().getUsername()
                                                : null)
                                        .horas(subtask.getHoras())
                                        .puntos(subtask.getPuntos())
                                        .dueDate(subtask.getDueDate())
                                        .createdAt(subtask.getCreatedAt())
                                        .priority(subtask.getPriority())
                                        .build()
                        ).toList())
                        .build();

                result.add(dto);
            }
        }

        return result;
    }


}
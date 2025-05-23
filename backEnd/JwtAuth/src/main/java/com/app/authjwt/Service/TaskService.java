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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.ServiceLoader;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class TaskService {
    private final TaskRepository taskRepository;
    private final SubtaskRepository subtaskRepository;
    private final BoardColumnRepository boardColumnRepository;
    private final BoardRepository boardRepository;
    private final UserRepository userRepository;
    private static final Logger logger = LoggerFactory.getLogger(TaskService.class);

    public ServiceResult<List<FullTaskDto>> traerTaskLogger() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = null;

        if (principal instanceof UserDetails) {
            username = ((UserDetails) principal).getUsername();
        }

        if (username == null) {
            return new ServiceResult<>(List.of("No se pudo obtener el usuario autenticado."));
        }
        logger.info("Obteniendo Task para "+username);
        List<Task> tasksList = taskRepository.findTasksBySubtaskAssigneeUsername(username);

        List<FullTaskDto> result = tasksList.stream()
                .map(task -> {
                    BoardColumn column = task.getColumn();
                    Board board = column != null ? column.getBoard() : null;
                    return mapToFullTaskDto(task, column, board);
                })
                .collect(Collectors.toList());

        return new ServiceResult<>(result);
    }

    public ServiceResult<TaskDto> guardarTask(Long boardId, TaskDto taskDto) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new EntityNotFoundException("Board no encontrado"));
        BoardColumn firstColumn = boardColumnRepository.findFirstByBoardOrderByPositionAsc(board)
                .orElseThrow(() -> new EntityNotFoundException("No se encontró ninguna columna en el board"));


        Task newTask = Task.builder()
                .title(taskDto.getTitle())
                .description(taskDto.getDescription())
                .dueDate(taskDto.getDueDate())
                .column(firstColumn)
                .position(1)
                .subtasks(new ArrayList<>())
                .build();


        taskRepository.save(newTask);



        return new ServiceResult<>(taskDto);
    }

    public ServiceResult<TaskDto> actualizarTask( TaskDto taskDto){
        List<String> errors = new ArrayList<>();
        try {
            //validaciones
            if (taskDto.getId()==null ){
                errors.add("El id no esta presente");
            }
            if (taskDto ==null){
                errors.add("no trae cuerpo la solicitud");
            }

            Task actulizar = taskRepository.findById(taskDto.getId()).orElseThrow(() -> new EntityNotFoundException("Tarea no encontrada"));

            actulizar.setDescription(taskDto.getDescription());
            actulizar.setTitle(taskDto.getTitle());
            actulizar.setColumn(boardColumnRepository.findById(taskDto.getColumnId()).orElse(actulizar.getColumn()));
            actulizar.setDueDate(taskDto.getDueDate());

            taskRepository.save(actulizar);
            return new ServiceResult<>(taskDto);


        }catch (Exception e){
            errors.add("Error: " + e.getMessage());
            return new ServiceResult<>(errors);

        }

    }

    public ServiceResult<SubtaskDto> addSubtaskToTask(Long taskId, SubtaskDto subtaskDTO) {
        try {
            // Verificar que existe la tarea padre
            Task task = taskRepository.findById(taskId)
                    .orElseThrow(() -> new EntityNotFoundException("Tarea no encontrada con ID: " + taskId));

            // Buscar el usuario asignado (si existe)
            User assignee = null;
            if (subtaskDTO.getAssigneeId() != null) {
                assignee = userRepository.findById(subtaskDTO.getAssigneeId())
                        .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado con ID: " + subtaskDTO.getAssigneeId()));
            }

            // Construir y guardar la subtarea
            Subtask subtask = Subtask.builder()
                    .title(subtaskDTO.getTitle())
                    .assignee(assignee)
                    .horas(subtaskDTO.getHoras())
                    .puntos(subtaskDTO.getPuntos() != null ? subtaskDTO.getPuntos() : 0)
                    .dueDate(subtaskDTO.getDueDate())
                    .priority(subtaskDTO.getPriority() != null ? subtaskDTO.getPriority() : TaskPriority.Medium)
                    .task(task)
                    .build();

            Subtask savedSubtask = subtaskRepository.save(subtask);

            // Convertir la subtarea guardada a DTO para retornarla
            SubtaskDto resultDto = SubtaskDto.builder()
                    .title(savedSubtask.getTitle())
                    .assigneeId(savedSubtask.getAssignee() != null ? savedSubtask.getAssignee().getId() : null)
                    .horas(savedSubtask.getHoras())
                    .puntos(savedSubtask.getPuntos())
                    .dueDate(savedSubtask.getDueDate())
                    .priority(savedSubtask.getPriority())
                    .build();

            return new ServiceResult<>(resultDto);

        } catch (EntityNotFoundException e) {
            return new ServiceResult<>(List.of(e.getMessage()));
        }
    }

    public ServiceResult<SubtaskDto> deleteSubtask(Long subtaskId) {
        try {
            Subtask subtask = subtaskRepository.findById(subtaskId)
                    .orElseThrow(() -> new EntityNotFoundException("Subtask no encontrada con ID: " + subtaskId));

            subtaskRepository.delete(subtask);
            SubtaskDto sb=new SubtaskDto();

            return new ServiceResult<>(sb);

        } catch (EntityNotFoundException e) {
            return new ServiceResult<>(List.of(e.getMessage()));
        }
    }


    @Transactional(readOnly = true)
    public List<FullTaskDto> getFullTasksByBoardId(Long boardId) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new RuntimeException("Board no encontrado con ID: " + boardId));

        List<BoardColumn> columns = boardColumnRepository.findByBoardId(boardId);

        List<Task> tasks = taskRepository.findByColumnIn(columns);

        return tasks.stream()
                .map(task -> {
                    BoardColumn column = task.getColumn();
                    return mapToFullTaskDto(task, column, board);
                })
                .collect(Collectors.toList());
    }
    private FullTaskDto mapToFullTaskDto(Task task, BoardColumn column, Board board) {
        if (task == null) {
            throw new IllegalArgumentException("Tarea nula detectada al mapear DTO");
        }

        return FullTaskDto.builder()
                .id(task.getId())
                .title(task.getTitle())
                .description(task.getDescription())
                .dueDate(task.getDueDate())
                .createdAt(task.getCreatedAt())
                .startedAt(task.getStartedAt())
                .closedAt(task.getClosedAt())
                .position(task.getPosition())
                .columnId(column != null ? column.getId() : null)
                .columnName(column != null ? column.getName() : null)
                .boardId(board != null ? board.getId() : null)
                .boardName(board != null ? board.getName() : null)
                .subtasks(task.getSubtasks().stream()
                        .filter(Objects::nonNull)
                        .map(this::mapToSubtaskInfo)
                        .toList())
                .build();
    }

    private FullTaskDto.SubtaskInfo mapToSubtaskInfo(Subtask subtask) {
        if (subtask == null) {
            return null;
        }

        return FullTaskDto.SubtaskInfo.builder()
                .id(subtask.getId())
                .title(subtask.getTitle())
                .assigneeName(subtask.getAssignee() != null ? subtask.getAssignee().getUsername() : null)
                .horas(subtask.getHoras())
                .puntos(subtask.getPuntos())
                .dueDate(subtask.getDueDate())
                .createdAt(subtask.getCreatedAt())
                .priority(subtask.getPriority())
                .build();
    }


    public String moverTarjetaColumna(Long idTask, Long columnId) {
        Task taskUpdate = taskRepository.findById(idTask)
                .orElseThrow(() -> new RuntimeException("task no encontrado con ID: " + idTask));
        taskUpdate.setColumn(boardColumnRepository.findById(columnId).orElse(taskUpdate.getColumn()));
        taskRepository.save(taskUpdate);
        return "success";
    }

}
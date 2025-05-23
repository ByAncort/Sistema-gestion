package com.app.authjwt.Controller;

import com.app.authjwt.Service.TaskService;
import com.app.authjwt.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/api/boards")
@RequiredArgsConstructor
public class TaskController {
    private final TaskService taskService;

    @PostMapping("/{boardId}/tasks/save")
    public ResponseEntity<ServiceResult<TaskDto>> save(@PathVariable Long boardId, @RequestBody TaskDto taskDto) {
        ServiceResult<TaskDto> result = taskService.guardarTask(boardId, taskDto);
        return new ResponseEntity<>(result, result.isSuccess() ? HttpStatus.CREATED : HttpStatus.BAD_REQUEST);
    }

    @PutMapping("update/{idTask}/column/{columnId}")
    public ResponseEntity<?> moverTask(@PathVariable Long idTask,@PathVariable Long columnId){
        try {
            String result = taskService.moverTarjetaColumna(idTask, columnId);
            return ResponseEntity.ok(Collections.singletonMap("message", result));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.singletonMap("error", e.getMessage()));
        }
    }


    @PutMapping("actualizar")
    public ResponseEntity<?>  actualizar(@RequestBody TaskDto taskDto){
        ServiceResult<TaskDto> result= taskService.actualizarTask(taskDto);

        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body(result.getErrors());
        }
        return ResponseEntity.status(HttpStatus.OK).body(result.getData());
    }


    @DeleteMapping("delete-subtask/{idSubtask}")
    public  ResponseEntity<ServiceResult<SubtaskDto>> deleteSubtask(@PathVariable Long idSubtask){
        ServiceResult<SubtaskDto> result=taskService.deleteSubtask(idSubtask);
        return new ResponseEntity<>(result, result.isSuccess() ? HttpStatus.CREATED : HttpStatus.BAD_REQUEST);

    }
    @PostMapping("save-subtask/{idTask}")
    public ResponseEntity<ServiceResult<SubtaskDto>> addSubtask(@PathVariable Long idTask,@RequestBody SubtaskDto dto){
        ServiceResult<SubtaskDto> result =taskService.addSubtaskToTask(idTask,dto);
        return new ResponseEntity<>(result, result.isSuccess() ? HttpStatus.CREATED : HttpStatus.BAD_REQUEST);

    }

    @GetMapping("/boards/{boardId}/full-tasks")
    public ResponseEntity<List<FullTaskDto>> getTasksByBoard(@PathVariable Long boardId) {
        List<FullTaskDto> tasks = taskService.getFullTasksByBoardId(boardId);
        return ResponseEntity.ok(tasks);
    }
    @GetMapping("/boards/full-tasks/for-user")
    public ResponseEntity<List<FullTaskDto>> getTaskForUser() {
        ServiceResult<List<FullTaskDto>> tasks = taskService.traerTaskLogger();
        return ResponseEntity.ok(tasks.getData());
    }



    }

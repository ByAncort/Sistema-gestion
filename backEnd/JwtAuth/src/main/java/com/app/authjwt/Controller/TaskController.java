package com.app.authjwt.Controller;

import com.app.authjwt.Service.TaskService;
import com.app.authjwt.dto.ServiceResult;
import com.app.authjwt.dto.SubtaskDto;
import com.app.authjwt.dto.TaskDto;
import com.app.authjwt.dto.TeamResponseDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/boards")
@RequiredArgsConstructor
public class TaskController {
    private final TaskService taskService;

    @PostMapping("save")
    public ResponseEntity<ServiceResult<TaskDto>> save(@RequestBody TaskDto taskDto){
        ServiceResult<TaskDto> result = taskService.guardarTask(taskDto);
        return new ResponseEntity<>(result, result.isSuccess() ? HttpStatus.CREATED : HttpStatus.BAD_REQUEST);
    }
    @PostMapping("save-subtask/{idTask}")
    public ResponseEntity<ServiceResult<List<SubtaskDto>>> addSubtask(@PathVariable Long idTask,@RequestBody List<SubtaskDto> dto){
        ServiceResult<List<SubtaskDto>> result =taskService.addSubtasksToTask(idTask,dto);
        return new ResponseEntity<>(result, result.isSuccess() ? HttpStatus.CREATED : HttpStatus.BAD_REQUEST);

    }
}

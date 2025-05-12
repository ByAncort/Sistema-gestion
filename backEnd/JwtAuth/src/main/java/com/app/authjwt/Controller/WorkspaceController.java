package com.app.authjwt.Controller;

import com.app.authjwt.Service.WorkspaceService;
import com.app.authjwt.dto.ServiceResult;
import com.app.authjwt.dto.WorkspaceDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/workspaces")
@RequiredArgsConstructor
public class WorkspaceController {

    private final WorkspaceService workspaceService;

    @PostMapping
    public ResponseEntity<ServiceResult<WorkspaceDto>> createWorkspace(@RequestBody WorkspaceDto request) {
        ServiceResult<WorkspaceDto> result = workspaceService.createWorkspace(request);
        return new ResponseEntity<>(result, result.isSuccess() ? HttpStatus.CREATED : HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/find/team-name/{nombreTeam}")
    public ResponseEntity<ServiceResult<List<WorkspaceDto>>> getByTeam(@PathVariable String nombreTeam) {
        ServiceResult<List<WorkspaceDto>> result = workspaceService.listarWorkspacesByTeam(nombreTeam);
        return ResponseEntity.status(result.isSuccess() ? HttpStatus.OK : HttpStatus.BAD_REQUEST).body(result);
    }


    @GetMapping
    public ResponseEntity<ServiceResult<List<WorkspaceDto>>> getAllWorkspaces() {
        ServiceResult<List<WorkspaceDto>> result = workspaceService.getAllWorkspaces();
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ServiceResult<WorkspaceDto>> getWorkspaceById(@PathVariable Long id) {
        ServiceResult<WorkspaceDto> result = workspaceService.getWorkspaceById(id);
        return result.isSuccess() ? ResponseEntity.ok(result) : ResponseEntity.notFound().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<ServiceResult<WorkspaceDto>> updateWorkspace(
            @PathVariable Long id, @RequestBody WorkspaceDto request) {
        ServiceResult<WorkspaceDto> result = workspaceService.updateWorkspace(id, request);
        return result.isSuccess() ? ResponseEntity.ok(result) : ResponseEntity.badRequest().body(result);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ServiceResult<Void>> deleteWorkspace(@PathVariable Long id) {
        ServiceResult<Void> result = workspaceService.deleteWorkspace(id);
        return result.isSuccess() ? ResponseEntity.noContent().build() : ResponseEntity.badRequest().body(result);
    }
}
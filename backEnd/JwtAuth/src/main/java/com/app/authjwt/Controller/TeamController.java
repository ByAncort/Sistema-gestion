package com.app.authjwt.Controller;

import com.app.authjwt.Service.TeamService;
import com.app.authjwt.dto.ServiceResult;
import com.app.authjwt.dto.TeamRequestDto;
import com.app.authjwt.dto.TeamResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/teams")
@RequiredArgsConstructor
public class TeamController {

    private final TeamService teamService;

    @PostMapping
    public ResponseEntity<ServiceResult<TeamResponseDto>> createTeam(@RequestBody TeamRequestDto request) {
        ServiceResult<TeamResponseDto> result = teamService.createTeam(request);
        return new ResponseEntity<>(result, result.isSuccess() ? HttpStatus.CREATED : HttpStatus.BAD_REQUEST);
    }

    @GetMapping
    public ResponseEntity<ServiceResult<List<TeamResponseDto>>> getAllTeams() {
        ServiceResult<List<TeamResponseDto>> result = teamService.getAllTeams();
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ServiceResult<TeamResponseDto>> getTeamById(@PathVariable Long id) {
        ServiceResult<TeamResponseDto> result = teamService.getTeamById(id);
        return result.isSuccess() ? ResponseEntity.ok(result) : ResponseEntity.notFound().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<ServiceResult<TeamResponseDto>> updateTeam(
            @PathVariable Long id, @RequestBody TeamRequestDto request) {
        ServiceResult<TeamResponseDto> result = teamService.updateTeam(id, request);
        return result.isSuccess() ? ResponseEntity.ok(result) : ResponseEntity.badRequest().body(result);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ServiceResult<Void>> deleteTeam(@PathVariable Long id) {
        ServiceResult<Void> result = teamService.deleteTeam(id);
        return result.isSuccess() ? ResponseEntity.noContent().build() : ResponseEntity.badRequest().body(result);
    }
}
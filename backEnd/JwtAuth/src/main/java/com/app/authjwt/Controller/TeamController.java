package com.app.authjwt.Controller;

import com.app.authjwt.Model.Team;
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
@RequiredArgsConstructor
@RequestMapping("/api/team/")
public class TeamController {
    private final TeamService teamService;

    @PostMapping("create-team")
    public ResponseEntity<?> crateTeam(@RequestBody TeamRequestDto teamRequestDto){
        ServiceResult<TeamResponseDto> result=teamService.createTeam(teamRequestDto);
        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body(result.getErrors());
        } else {
            return ResponseEntity.status(HttpStatus.CREATED).body(result.getData());
        }
    }
    @GetMapping("listar-teams")
    public ResponseEntity<?> listar(){
        ServiceResult<List<TeamResponseDto>> result= teamService.getAllTeams();
        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body(result.getErrors());
        } else {
            return ResponseEntity.status(HttpStatus.CREATED).body(result.getData());
        }
    }
}

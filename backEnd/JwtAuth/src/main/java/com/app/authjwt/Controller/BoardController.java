package com.app.authjwt.Controller;
import com.app.authjwt.Model.Board;
import com.app.authjwt.Service.*;
import com.app.authjwt.dto.BoardDto;
import com.app.authjwt.dto.ServiceResult;
import com.fasterxml.jackson.databind.util.JSONPObject;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/boards")
@RequiredArgsConstructor
public class BoardController {
    private final BoardService boardService;

    @PostMapping("/workspace/{workspaceId}")
    public ResponseEntity<Board> createBoard(
            @PathVariable Long workspaceId,
            @RequestBody BoardDto board) {
            ServiceResult<BoardDto> result = boardService.createBoardWithDefaultColumns(workspaceId, board);
            return new ResponseEntity(result, HttpStatus.OK);
    }
    @GetMapping("/workspace/{workspaceId}")
    public ResponseEntity<Board> listBoard(@PathVariable Long workspaceId){
        ServiceResult<BoardDto> result= boardService.mostrarBoardWorkspace(workspaceId);
        return new ResponseEntity(result,HttpStatus.OK);
    }

    @PatchMapping("/{boardId}/columns/order")
    public ResponseEntity<Void> reorderColumns(
            @PathVariable Long boardId,
            @RequestBody List<Long> newColumnOrder) {
        boardService.reorderColumns(boardId, newColumnOrder);
        return ResponseEntity.ok().build();
    }

}

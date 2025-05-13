package com.app.authjwt.Controller;

import com.app.authjwt.Service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/columns")
@RequiredArgsConstructor
public class BoardColumnController {
    private final BoardColumnService columnService;

    @PostMapping("/{columnId}/move-task")
    public ResponseEntity<Void> moveTask(
            @PathVariable Long columnId,
            @RequestParam Long targetColumnId,
            @RequestParam Long taskId,
            @RequestParam Integer newPosition) {
        columnService.moveTaskBetweenColumns(columnId, targetColumnId, taskId, newPosition);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{columnId}/wip-limit")
    public ResponseEntity<Void> updateWipLimit(
            @PathVariable Long columnId,
            @RequestBody Integer newWipLimit) {
        columnService.updateColumnWipLimit(columnId, newWipLimit);
        return ResponseEntity.ok().build();
    }
}

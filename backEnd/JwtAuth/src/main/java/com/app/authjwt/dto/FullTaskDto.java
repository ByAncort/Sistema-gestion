package com.app.authjwt.dto;

import com.app.authjwt.Model.TaskPriority;
import com.app.authjwt.Model.SubtaskStatus;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FullTaskDto {

    // Task fields
    private Long id;
    private String title;
    private String description;
    private LocalDate dueDate;
    private Integer position;
    private LocalDateTime createdAt;
    private LocalDateTime startedAt;
    private LocalDateTime closedAt;
    // BoardColumn info
    private Long columnId;
    private String columnName;
    private Integer columnPosition;
    private Integer wipLimit;
    private Long boardId;
    private String boardName;
    private LocalDateTime boardCreatedAt;
    private List<SubtaskInfo> subtasks;
//    private SubtaskStatus status;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class SubtaskInfo {
        private Long id;
        private String title;
        private String assigneeName;
        private Double horas;
        private int puntos;
        private LocalDate dueDate;
        private LocalDateTime createdAt;
        private TaskPriority priority;
    }
}

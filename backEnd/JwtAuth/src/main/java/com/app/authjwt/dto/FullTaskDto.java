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


    private Long id;
    private String title;
    private String description;
    private LocalDate dueDate;
    private LocalDateTime createdAt;
    private LocalDateTime startedAt;
    private LocalDateTime closedAt;
    private Integer position;
    private Long columnId;
    private String columnName;
    private Long boardId;
    private String boardName;
    private List<SubtaskInfo> subtasks;


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

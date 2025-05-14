package com.app.authjwt.dto;

import com.app.authjwt.Model.SubtaskStatus;
import com.app.authjwt.Model.TaskPriority;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SubtaskDto {
    @NotBlank
    private String title;
    private Long assigneeId;
    private Double horas;
    private Integer puntos;
    private LocalDate dueDate;
    private TaskPriority priority;
}



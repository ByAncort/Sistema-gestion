package com.app.authjwt.dto;

import com.app.authjwt.Model.TaskPriority;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TaskDto {
    private Long id;
    @NotBlank
    private String title;

    private String description;
    private LocalDate dueDate;

    @NotNull
    private Long columnId;

    private Integer position;
}
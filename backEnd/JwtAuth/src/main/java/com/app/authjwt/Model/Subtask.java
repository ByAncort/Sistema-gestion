package com.app.authjwt.Model;

import com.app.authjwt.Model.User.User;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "subtasks")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
@Builder
public class Subtask {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "task_id", nullable = false)
    private Task task;

    @Column(nullable = false, length = 255)
    private String title;

    @OneToOne(fetch = FetchType.LAZY)
    private User assignee;

    @Enumerated(EnumType.STRING)
    private SubtaskStatus status = SubtaskStatus.Pending;

    @Column(name="horas_estimacion")
    private Double horas;

    @Column(name = "puntos_historia")
    private int puntos;

    @Column(name = "due_date")
    private LocalDate dueDate;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Enumerated(EnumType.STRING)
    private TaskPriority priority = TaskPriority.Medium;
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}

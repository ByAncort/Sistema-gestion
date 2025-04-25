package com.app.authjwt.Model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "subtasks")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class Subtask {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "task_id", nullable = false)
    private Task task;

    @Column(nullable = false, length = 255)
    private String title;

    @Lob
    private String description;

    @Enumerated(EnumType.STRING)
    private SubtaskStatus status = SubtaskStatus.Pending;

    @Column(name = "due_date")
    private LocalDate dueDate;

    @Column(nullable = false)
    private Integer position;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}

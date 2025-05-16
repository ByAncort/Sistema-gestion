package com.app.authjwt.Model;

import lombok.*;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "tasks")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
@Builder
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "column_id", nullable = false)
    private BoardColumn column;

    @Column(nullable = false, length = 255)
    private String title;

    @Lob
    private String description;

    @Column(name = "due_date")
    private LocalDate dueDate;

    public Board getBoard() {
        return this.column != null ? this.column.getBoard() : null;
    }
//    @Enumerated(EnumType.STRING)
//    private SubtaskStatus status = SubtaskStatus.Pending;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "started_at")
    private LocalDateTime startedAt;

    @Column(name = "closed_at")
    private LocalDateTime closedAt;

    @Column(nullable = false)
    private Integer position;

    @OneToMany(mappedBy = "task", cascade = CascadeType.ALL, orphanRemoval = true)
//    @OrderColumn(name = "position")
    private List<Subtask> subtasks = new ArrayList<>();

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}
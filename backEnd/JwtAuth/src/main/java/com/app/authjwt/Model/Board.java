package com.app.authjwt.Model;



import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Table(name = "boards")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class Board {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BoardColumn> columns = new ArrayList<>();

    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL)
    private List<Metric> metrics = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "workspace_id")
    private Workspace workspace;
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    public List<Task> getAllTasks() {
        return this.columns.stream()
                .flatMap(column -> column.getTasks().stream())
                .collect(Collectors.toList());
    }
}
package com.app.authjwt.Model;


import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "metrics")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class Metric {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id", nullable = false)
    private Board board;

    @Column(nullable = false)
    private LocalDate date;

    @Column(name = "avg_lead_time", precision = 5, scale = 2)
    private BigDecimal avgLeadTime;

    @Column(name = "tasks_completed")
    private Integer tasksCompleted;
}

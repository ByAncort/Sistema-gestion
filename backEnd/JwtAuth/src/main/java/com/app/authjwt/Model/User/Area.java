package com.app.authjwt.Model.User;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;

import java.security.Timestamp;
import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Area {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, unique = true)
    private String name;
    @CreatedDate
    @Column(updatable = false)
    private Timestamp created;
    @OneToMany(mappedBy = "area", fetch = FetchType.LAZY)
    @Builder.Default
    private Set<User> users = new HashSet<>();


}

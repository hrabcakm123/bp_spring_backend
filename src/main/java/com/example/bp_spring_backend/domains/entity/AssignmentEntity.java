package com.example.bp_spring_backend.domains.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "assignments")
public class AssignmentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "block_id", referencedColumnName = "id", nullable = false)
    private BlockEntity blockEntity;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "note", nullable = true)
    private String note;

    @Column(name = "max_points", nullable = false)
    private Double maxPoints;
}

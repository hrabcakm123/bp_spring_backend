package com.example.bp_spring_backend.domains.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(
        name = "assignments",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"block_id", "name"})
        }
)
@SQLRestriction("is_deleted = false")
@SQLDelete(sql = "UPDATE assignments SET is_deleted = true WHERE id = ?")
public class AssignmentEntity {

        @Id
        @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "users_gen")
        @SequenceGenerator(name = "users_gen", sequenceName = "users_id_seq", allocationSize = 1)
    @Column(name = "id", nullable = false)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "block_id", referencedColumnName = "id", nullable = false)
    private BlockEntity blockEntity;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "max_points", nullable = false)
    private Double maxPoints;

    @Column(name = "is_deleted", nullable = false)
    private boolean isDeleted;
}

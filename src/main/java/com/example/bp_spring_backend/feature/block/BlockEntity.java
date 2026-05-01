package com.example.bp_spring_backend.feature.block;

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
@Table(name = "blocks")
@SQLRestriction("is_deleted = false")
@SQLDelete(sql = "UPDATE blocks SET is_deleted = true WHERE id = ?")
public class BlockEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @Column(name = "max_points", nullable = false)
    private Double maxPoints;

    @Column(name = "required_points", nullable = false)
    private Double requiredPoints;

    @Column(name = "is_deleted", nullable = false)
    private boolean isDeleted;
}

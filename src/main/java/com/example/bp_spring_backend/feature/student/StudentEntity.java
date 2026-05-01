package com.example.bp_spring_backend.feature.student;

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
@Table(name = "students")
@SQLRestriction("is_deleted = false")
@SQLDelete(sql = "UPDATE students SET is_deleted = true WHERE id = ?")
public class StudentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "ais_id", nullable = false, unique = true)
    private Integer aisId;

    @Column(name = "full_name", nullable = false)
    private String fullName;

    @Column(name = "is_deleted", nullable = false)
    private boolean isDeleted;
}

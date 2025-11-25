package com.example.bp_spring_backend.domains.entity;

import com.example.bp_spring_backend.domains.enums.RoomEnum;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "exercises")
@SQLRestriction("is_deleted = false")
@SQLDelete(sql = "UPDATE exercises SET is_deleted = true WHERE id = ?")
public class ExerciseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "first_session_date", nullable = false)
    private LocalDate firstSessionDate;

    @Column(name = "start_time", nullable = false)
    private LocalTime startTime;

    @Enumerated(EnumType.STRING)
    @Column(name = "room_enum", nullable = false)
    private RoomEnum roomEnum;

    @Column(name = "is_deleted", nullable = false)
    private boolean isDeleted;
}

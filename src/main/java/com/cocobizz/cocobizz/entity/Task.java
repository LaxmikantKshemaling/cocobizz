package com.cocobizz.cocobizz.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "tasks")
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long taskId;

    @Column(nullable = false)
    private String title;

    // 🔥 THIS IS THE REAL MAPPING
    @ManyToOne
    @JoinColumn(name = "assigned_to", referencedColumnName = "user_id", nullable = false)
    private Users assignedTo;

    @Enumerated(EnumType.STRING)
    private Priority priority;

    @Enumerated(EnumType.STRING)
    private TaskStatus status;

    private LocalDate dueDate;
    private LocalTime dueTime;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        if (this.status == null) this.status = TaskStatus.PENDING;
        if (this.priority == null) this.priority = Priority.MEDIUM;
    }

    public enum Priority {
        LOW, MEDIUM, HIGH
    }

    public enum TaskStatus {
        PENDING, IN_PROGRESS, COMPLETED
    }
}
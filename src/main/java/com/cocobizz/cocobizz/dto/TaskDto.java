package com.cocobizz.cocobizz.dto;

import lombok.*;
import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TaskDto {

    private String title;

    private Long assignedToUserId;   // ✅ MUST BE Long

    private String priority;

    private String status;

    private LocalDate dueDate;

    private LocalTime dueTime;
}
package com.cocobizz.cocobizz.service;

import com.cocobizz.cocobizz.dto.TaskDto;
import com.cocobizz.cocobizz.entity.Task;

import java.util.List;

public interface TaskService {

    Task assignTask(TaskDto dto);

    List<Task> getAllTasks();

    List<Task> getTasksByUser(Long userId);

    Task updateTaskStatus(Long taskId, String status);
}
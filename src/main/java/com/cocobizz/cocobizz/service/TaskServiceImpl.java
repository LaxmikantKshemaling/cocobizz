package com.cocobizz.cocobizz.service;

import com.cocobizz.cocobizz.dto.TaskDto;
import com.cocobizz.cocobizz.entity.Task;
import com.cocobizz.cocobizz.entity.Users;
import com.cocobizz.cocobizz.repository.TaskRepository;
import com.cocobizz.cocobizz.repository.UserRepository;
import com.cocobizz.cocobizz.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;

    @Override
    public Task assignTask(TaskDto dto) {

        if (dto.getAssignedToUserId() == null) {
            throw new RuntimeException("Assigned user ID is required");
        }

        Users assignedUser = userRepository.findById(dto.getAssignedToUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Task task = Task.builder()
                .title(dto.getTitle())
                .assignedTo(assignedUser)
                .priority(dto.getPriority() != null
                        ? Task.Priority.valueOf(dto.getPriority().toUpperCase())
                        : Task.Priority.MEDIUM)
                .status(dto.getStatus() != null
                        ? Task.TaskStatus.valueOf(dto.getStatus().toUpperCase())
                        : Task.TaskStatus.PENDING)
                .dueDate(dto.getDueDate())
                .dueTime(dto.getDueTime())
                .build();

        return taskRepository.save(task);
    }

    @Override
    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }

    @Override
    public List<Task> getTasksByUser(Long userId) {

        if (userId == null) {
            throw new RuntimeException("User ID is required");
        }

        Users user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return taskRepository.findByAssignedTo(user);
    }

    @Override
    public Task updateTaskStatus(Long taskId, String status) {

        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Task not found"));

        task.setStatus(Task.TaskStatus.valueOf(status.toUpperCase()));

        return taskRepository.save(task);
    }
}
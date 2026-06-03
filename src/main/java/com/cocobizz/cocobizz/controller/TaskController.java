package com.cocobizz.cocobizz.controller;

import com.cocobizz.cocobizz.dto.TaskDto;
import com.cocobizz.cocobizz.entity.Task;
import com.cocobizz.cocobizz.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequestMapping("/api/tasks")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
public class TaskController {

    private final TaskService taskService;

    @PostMapping("/assign")
    public ResponseEntity<?> assignTask(@RequestBody TaskDto dto) {
        return ResponseEntity.status(201).body(taskService.assignTask(dto));
    }

    @GetMapping
    public ResponseEntity<?> getAllTasks() {
        return ResponseEntity.ok(taskService.getAllTasks());
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<?> getTasksByUser(@PathVariable Long userId) {
        return ResponseEntity.ok(taskService.getTasksByUser(userId));
    }

    @PutMapping("/{taskId}/status")
    public ResponseEntity<?> updateStatus(@PathVariable Long taskId,
                                          @RequestParam String status) {
        return ResponseEntity.ok(taskService.updateTaskStatus(taskId, status));
    }
}
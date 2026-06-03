package com.cocobizz.cocobizz.repository;

import com.cocobizz.cocobizz.entity.Task;
import com.cocobizz.cocobizz.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

    List<Task> findByAssignedTo(Users user);
}
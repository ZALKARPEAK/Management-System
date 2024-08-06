package com.example.taskmanagmentsystem.repository;

import com.example.taskmanagmentsystem.model.Task;
import com.example.taskmanagmentsystem.model.UserInfo;
import com.example.taskmanagmentsystem.util.enums.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findAllByCreatorId(Long creatorId);
    Page<Task> findByCreatorAndStatus(UserInfo creator, Status status, Pageable pageable);
}
package org.kamal.taskmanager.repository;

import org.kamal.taskmanager.models.Board;
import org.kamal.taskmanager.models.Task;
import org.kamal.taskmanager.models.TaskStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;


public interface TaskRepository extends JpaRepository<Task, Long> {

    Page<Task> findByBoardAndStatus(Board board, TaskStatus status, Pageable pageable);

    Page<Task> findByBoard(Board board, Pageable pageable);
}

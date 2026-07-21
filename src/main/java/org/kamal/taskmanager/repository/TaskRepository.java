package org.kamal.taskmanager.repository;

import org.kamal.taskmanager.models.Board;
import org.kamal.taskmanager.models.Task;
import org.kamal.taskmanager.models.TaskStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface TaskRepository extends JpaRepository<Task, Long> {

    List<Task> findByBoardAndStatus(Board board, TaskStatus status);

    List<Task> findByBoard(Board board);
}

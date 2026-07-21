package org.kamal.taskmanager.controllers;

import org.kamal.taskmanager.dto.request.CreateTaskRequest;
import org.kamal.taskmanager.dto.response.TaskResponse;
import org.kamal.taskmanager.models.Board;
import org.kamal.taskmanager.models.Task;
import org.kamal.taskmanager.models.TaskStatus;
import org.kamal.taskmanager.models.User;
import org.kamal.taskmanager.security.UserDetailsImpl;
import org.kamal.taskmanager.services.BoardService;
import org.kamal.taskmanager.services.TaskService;
import org.kamal.taskmanager.services.UserService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    private final TaskService taskService;
    private final UserService userService;
    private final BoardService boardService;

    public TaskController(TaskService taskService, UserService userService, BoardService boardService) {
        this.taskService = taskService;
        this.userService = userService;
        this.boardService = boardService;
    }

    @PostMapping
    public TaskResponse createTask(@RequestBody CreateTaskRequest request, @AuthenticationPrincipal UserDetailsImpl principal) {
        Board board = boardService.getBoardById(request.getBoardId());
        User creator = principal.getUser();
        Task task = taskService.createTask(request.getName(), request.getDescription(), board, creator);
        return TaskResponse.fromEntity(task);
    }

    @PutMapping("/{taskId}/assign")
    public TaskResponse assignTask(@PathVariable Long taskId, @RequestParam(("assigneeId")) Long assigneeUserId, @AuthenticationPrincipal UserDetailsImpl principal) {
        User assignee = userService.getUserById(assigneeUserId);
        Task task = taskService.assignTask(taskId, assignee, principal.getUser());
        return TaskResponse.fromEntity(task);
    }

    @PutMapping("/{taskId}/status")
    public TaskResponse changeStatus(@PathVariable Long taskId, @RequestParam TaskStatus status, @AuthenticationPrincipal UserDetailsImpl principal) {
        Task task = taskService.changeStatus(taskId, status, principal.getUser());
        return TaskResponse.fromEntity(task);
    }

    @GetMapping
    public List<TaskResponse> getTasksByBoard(@RequestParam Long boardId, @RequestParam(required = false) TaskStatus status) {
        Board board = boardService.getBoardById(boardId);
        List<Task> task = (status == null) ? taskService.getTasksByBoard(board) : taskService.getTasksByBoardAndStatus(board, status);
        return task.stream().map(TaskResponse::fromEntity).toList();
    }
}
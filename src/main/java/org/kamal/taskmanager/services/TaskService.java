package org.kamal.taskmanager.services;

import org.kamal.taskmanager.exceptions.AccessDeniedException;
import org.kamal.taskmanager.exceptions.ResourceNotFoundException;
import org.kamal.taskmanager.models.*;
import org.kamal.taskmanager.repository.BoardMembershipRepository;
import org.kamal.taskmanager.repository.TaskRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskService {

    private final TaskRepository taskRepository;
    private final BoardMembershipRepository boardMembershipRepository;
    private final BoardService boardService;

    public TaskService(TaskRepository taskRepository, BoardMembershipRepository boardMembershipRepository, BoardService boardService) {
        this.taskRepository = taskRepository;
        this.boardMembershipRepository = boardMembershipRepository;
        this.boardService = boardService;
    }

    public Task createTask(String name, String description, Board board, User creatorUser) {
        BoardMembership creator = boardMembershipRepository.findByUserAndBoard(creatorUser, board)
                .orElseThrow(() -> new AccessDeniedException(creatorUser.getName() + " is not member of " + board.getName()));
        Task task = new Task();
        task.setName(name);
        task.setDescription(description);
        task.setBoard(board);
        task.setCreator(creator);
        task.setStatus(TaskStatus.TODO);
        return taskRepository.save(task);
    }


    public Task assignTask(Long id, User assigneeUser, User requester) {
        Task task = taskRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Task not found"));
        boolean isAdmin = boardService.isAdmin(requester,  task.getBoard());
        if(!isAdmin) {
            throw new  AccessDeniedException(requester.getName() + " is not a admin of the board");
        }
        BoardMembership assigneeMembership = boardMembershipRepository.findByUserAndBoard(assigneeUser, task.getBoard())
                .orElseThrow(() -> new AccessDeniedException(assigneeUser.getName() + " is not a member of the board"));

        task.setAssignee(assigneeMembership);
        return taskRepository.save(task);
    }

    public Task changeStatus(Long id, TaskStatus newStatus, User requester) {
        Task task = taskRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Task not found"));
        boolean isAdmin = boardService.isAdmin(requester, task.getBoard());
        boolean isAssignee =
                task.getAssignee() != null &&
                        task.getAssignee().getUser().getId().equals(requester.getId());
        if(!(isAdmin || isAssignee)) {
            throw new AccessDeniedException(requester.getName() + " is not allowed to change the task status.");
        }
        task.setStatus(newStatus);
        return taskRepository.save(task);
    }

    public Page<Task> getTasksByBoard(Board board, Pageable pageable) {
        return taskRepository.findByBoard(board, pageable);
    }

    public Page<Task> getTasksByBoardAndStatus(Board board, TaskStatus status, Pageable pageable) {
        return  taskRepository.findByBoardAndStatus(board, status, pageable);
    }
}

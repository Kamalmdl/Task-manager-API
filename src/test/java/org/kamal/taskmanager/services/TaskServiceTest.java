package org.kamal.taskmanager.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.kamal.taskmanager.exceptions.AccessDeniedException;
import org.kamal.taskmanager.models.*;
import org.kamal.taskmanager.repository.BoardMembershipRepository;
import org.kamal.taskmanager.repository.TaskRepository;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private BoardMembershipRepository boardMembershipRepository;

    @Mock
    private BoardService boardService;

    @InjectMocks
    private TaskService taskService;

    @Test
    void changeStatus_shouldSucceed_whenRequesterIsAssignee() {
        User assigneeUser = new User();
        assigneeUser.setId(2L);

        Board board = new Board();
        board.setId(1L);

        BoardMembership assigneeMembership = new BoardMembership();
        assigneeMembership.setUser(assigneeUser);

        Task task = new Task();
        task.setId(1L);
        task.setBoard(board);
        task.setAssignee(assigneeMembership);
        task.setStatus(TaskStatus.TODO);

        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));
        when(boardService.isAdmin(assigneeUser, board)).thenReturn(false);
        when(taskRepository.save(any(Task.class))).thenAnswer(i -> i.getArgument(0));


        Task result = taskService.changeStatus(1L, TaskStatus.IN_PROGRESS, assigneeUser);


        assertEquals(TaskStatus.IN_PROGRESS, result.getStatus());
    }

    @Test
    void changeStatus_shouldThrowException_whenRequesterIsNotAdminOrAssignee() {
        User strangerUser = new User();
        strangerUser.setId(2L);

        Board board = new Board();
        board.setId(1L);

        Task task = new Task();
        task.setId(1L);
        task.setBoard(board);

        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));
        when(boardService.isAdmin(strangerUser, board)).thenReturn(false);

        assertThrows(AccessDeniedException.class, () -> taskService.changeStatus(1L, TaskStatus.IN_PROGRESS, strangerUser));
    }

    @Test
    void assignTask_shouldThrowException_whenRequesterIsNotAdmin() {
        User strangerUser = new User();
        strangerUser.setId(2L);

        User assigneeUser = new User();
        assigneeUser.setId(3L);

        Board board = new Board();
        board.setId(1L);

        Task task = new Task();
        task.setId(1L);
        task.setBoard(board);

        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));
        when(boardService.isAdmin(strangerUser, board)).thenReturn(false);

        assertThrows(AccessDeniedException.class, () -> taskService.assignTask(1L, assigneeUser,  strangerUser));
    }

    @Test
    void assignTask_shouldSucceed_whenRequesterIsAdmin() {
        User requesterUser = new User();
        requesterUser.setId(2L);

        User assigneeUser = new User();
        assigneeUser.setId(3L);

        Board board = new Board();
        board.setId(1L);

        BoardMembership assigneeMembership = new BoardMembership();
        assigneeMembership.setUser(assigneeUser);

        Task task = new Task();
        task.setId(1L);
        task.setBoard(board);

        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));
        when(boardService.isAdmin(requesterUser, board)).thenReturn(true);
        when(boardMembershipRepository.findByUserAndBoard(assigneeUser, task.getBoard())).thenReturn(Optional.of(assigneeMembership));
        when(taskRepository.save(any(Task.class))).thenAnswer(i -> i.getArgument(0));

        Task result =  taskService.assignTask(1L, assigneeUser, requesterUser);

        assertEquals(3L, result.getAssignee().getUser().getId());
    }
}
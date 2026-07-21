package org.kamal.taskmanager.controllers;

import org.kamal.taskmanager.dto.request.InviteMemberRequest;
import org.kamal.taskmanager.dto.response.BoardMembershipResponse;
import org.kamal.taskmanager.dto.response.BoardResponse;
import org.kamal.taskmanager.dto.request.CreateBoardRequest;
import org.kamal.taskmanager.models.Board;
import org.kamal.taskmanager.models.BoardMembership;
import org.kamal.taskmanager.models.User;
import org.kamal.taskmanager.security.UserDetailsImpl;
import org.kamal.taskmanager.services.BoardService;
import org.kamal.taskmanager.services.UserService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/boards")
public class BoardController {

    private final BoardService boardService;
    private final UserService userService;

    public BoardController(BoardService boardService, UserService userService) {
        this.boardService = boardService;
        this.userService = userService;
    }

    @PostMapping
    public BoardResponse createBoard(@RequestBody CreateBoardRequest request, @AuthenticationPrincipal UserDetailsImpl principal) {
        User creator = principal.getUser();
        Board board = boardService.createBoard(request.getName(), request.getDescription(), creator);
        return BoardResponse.fromEntity(board);
    }

    @GetMapping("/{id}")
    public BoardResponse getBoard(@PathVariable Long id) {
        Board board = boardService.getBoardById(id);
        return BoardResponse.fromEntity(board);
    }

    @PostMapping("/{id}/invite")
    public BoardMembershipResponse inviteMember(@PathVariable Long id, @RequestBody InviteMemberRequest request
                                                , @AuthenticationPrincipal UserDetailsImpl principal) {
        Board board = boardService.getBoardById(id);
        User user = userService.getUserById(request.getUserId());
        User inviter = principal.getUser();
        BoardMembership boardMembership = boardService.inviteMember(board, user, request.getRole(), inviter);
        return BoardMembershipResponse.fromEntity(boardMembership);
    }
}

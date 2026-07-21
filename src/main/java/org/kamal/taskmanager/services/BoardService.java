package org.kamal.taskmanager.services;


import org.kamal.taskmanager.exceptions.AccessDeniedException;
import org.kamal.taskmanager.exceptions.ResourceAlreadyExistsException;
import org.kamal.taskmanager.exceptions.ResourceNotFoundException;
import org.kamal.taskmanager.models.Board;
import org.kamal.taskmanager.models.BoardMembership;
import org.kamal.taskmanager.models.BoardRole;
import org.kamal.taskmanager.models.User;
import org.kamal.taskmanager.repository.BoardMembershipRepository;
import org.kamal.taskmanager.repository.BoardRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class BoardService {

    private final BoardRepository boardRepository;
    private final BoardMembershipRepository boardMembershipRepository;

    public BoardService(BoardRepository boardRepository,  BoardMembershipRepository boardMembershipRepository) {
        this.boardRepository = boardRepository;
        this.boardMembershipRepository = boardMembershipRepository;
    }

    @Transactional
    public Board createBoard(String name, String description, User creator) {
        Board board = new Board();
        board.setName(name);
        board.setDescription(description);
        board.setOwner(creator);
        boardRepository.save(board);

        BoardMembership boardMembership = new BoardMembership();
        boardMembership.setBoard(board);
        boardMembership.setUser(creator);
        boardMembership.setRole(BoardRole.ADMIN);
        boardMembershipRepository.save(boardMembership);

        return board;
    }

    public Board getBoardById(Long id) {
        return boardRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Board with id " + id + " does not exist"));
    }

    public boolean isMember(User user, Board board) {
        return boardMembershipRepository.findByUserAndBoard(user, board).isPresent();
    }

    public boolean isAdmin(User user, Board board) {
        Optional<BoardMembership> boardMembership = boardMembershipRepository.findByUserAndBoard(user, board);
        return boardMembership.isPresent() && boardMembership.get().getRole().equals(BoardRole.ADMIN);
    }

    public BoardMembership inviteMember(Board board, User user, BoardRole role, User inviter) {
        if(isMember(user, board)) {
            throw new ResourceAlreadyExistsException("User " + user.getName() + " already is member of board" + board.getName());
        }
        if(!isAdmin(inviter, board)) {
            throw new AccessDeniedException("Inviter is not a admin of board with id " + board.getId());
        }
        BoardMembership boardMembership = new BoardMembership();
        boardMembership.setBoard(board);
        boardMembership.setUser(user);
        boardMembership.setRole(role);
        return boardMembershipRepository.save(boardMembership);
    }
}

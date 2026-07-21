package org.kamal.taskmanager.repository;

import org.kamal.taskmanager.models.Board;
import org.kamal.taskmanager.models.BoardMembership;
import org.kamal.taskmanager.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BoardMembershipRepository extends JpaRepository<BoardMembership, Long> {
    Optional<BoardMembership> findByUserAndBoard(User user, Board board);
}

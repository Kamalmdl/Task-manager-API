package org.kamal.taskmanager.dto.response;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.kamal.taskmanager.models.BoardMembership;
import org.kamal.taskmanager.models.BoardRole;

@Data
@NoArgsConstructor
public class BoardMembershipResponse {
    private UserResponse user;
    private BoardRole role;

    public BoardMembershipResponse(UserResponse user, BoardRole role) {
        this.user = user;
        this.role = role;
    }

    public static BoardMembershipResponse fromEntity(BoardMembership boardMembership) {
        return new BoardMembershipResponse(UserResponse.fromEntity(boardMembership.getUser()), boardMembership.getRole());
    }
}

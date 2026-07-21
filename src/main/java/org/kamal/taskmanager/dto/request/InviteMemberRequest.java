package org.kamal.taskmanager.dto.request;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.kamal.taskmanager.models.BoardRole;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class InviteMemberRequest {
    private Long userId;
    private BoardRole role;
}

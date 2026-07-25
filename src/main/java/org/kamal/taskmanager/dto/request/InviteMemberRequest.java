package org.kamal.taskmanager.dto.request;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.kamal.taskmanager.models.BoardRole;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class InviteMemberRequest {
    @NotNull(message="userId is required!")
    private Long userId;
    @NotNull(message = "Role is required!")
    private BoardRole role;
}

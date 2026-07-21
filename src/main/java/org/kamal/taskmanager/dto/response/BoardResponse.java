package org.kamal.taskmanager.dto.response;

import org.kamal.taskmanager.models.Board;

public class BoardResponse {
    private Long id;
    private String name;
    private String description;
    private UserResponse owner;

    public BoardResponse(Long id, String name, String description, UserResponse owner) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.owner = owner;
    }

    public BoardResponse() {
    }

    public static BoardResponse fromEntity(Board board) {
        return new BoardResponse(board.getId(), board.getName(), board.getDescription(), UserResponse.fromEntity(board.getOwner()));
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public UserResponse getOwner() {
        return owner;
    }

    public void setOwner(UserResponse owner) {
        this.owner = owner;
    }
}

package org.kamal.taskmanager.dto.request;

public class CreateTaskRequest {
    private String name;
    private String description;
    private Long boardId;

    public CreateTaskRequest() {
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public Long getBoardId() { return boardId; }
    public void setBoardId(Long boardId) { this.boardId = boardId; }

}
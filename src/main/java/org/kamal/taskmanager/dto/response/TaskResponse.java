package org.kamal.taskmanager.dto.response;

import org.kamal.taskmanager.models.Task;
import org.kamal.taskmanager.models.TaskStatus;

public class TaskResponse {
    private Long id;
    private String name;
    private String description;
    private UserResponse creator;
    private TaskStatus status;
    private UserResponse assignee;

    public TaskResponse(Long id, String name, String description, UserResponse creator, TaskStatus status, UserResponse assignee) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.creator = creator;
        this.status = status;
        this.assignee = assignee;
    }

    public static TaskResponse fromEntity(Task task) {
        UserResponse creatorResponse = UserResponse.fromEntity(task.getCreator().getUser());
        UserResponse assigneeResponse = (task.getAssignee()!=null) ? UserResponse.fromEntity(task.getAssignee().getUser()) : null;
        return new TaskResponse(task.getId(), task.getName(), task.getDescription(), creatorResponse, task.getStatus(), assigneeResponse);
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

    public UserResponse getCreator() {
        return creator;
    }

    public void setCreator(UserResponse creator) {
        this.creator = creator;
    }

    public TaskStatus getStatus() {
        return status;
    }

    public void setStatus(TaskStatus status) {
        this.status = status;
    }

    public UserResponse getAssignee() {
        return assignee;
    }

    public void setAssignee(UserResponse assignee) {
        this.assignee = assignee;
    }
}

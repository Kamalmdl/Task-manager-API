package org.kamal.taskmanager.dto.request;

public class CreateBoardRequest {
    private String name;
    private String description;

    public CreateBoardRequest() {

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
}

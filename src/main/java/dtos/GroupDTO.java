package dtos;

import com.fasterxml.jackson.annotation.JsonProperty;

public class GroupDTO {
    @JsonProperty("id")
    private String id;
    @JsonProperty("name")
    private String name;

    public GroupDTO() {
    }

    public GroupDTO(final String id, final String name) {
        this.id = id;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}

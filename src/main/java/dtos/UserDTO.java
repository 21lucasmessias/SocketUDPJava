package dtos;

import com.fasterxml.jackson.annotation.JsonProperty;

public class UserDTO {
    @JsonProperty("id")
    private String id;
    @JsonProperty("name")
    private String name;

    public UserDTO() {
    }

    public UserDTO(String id, String name) {
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

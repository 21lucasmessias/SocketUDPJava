package server.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import dtos.GroupDTO;
import dtos.UserDTO;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Group {
    @JsonProperty("id")
    private String id;
    @JsonProperty("name")
    private String name;
    @JsonProperty("users")
    private List<UserDTO> users = new ArrayList<>(List.of());
    @JsonProperty("owner")
    private UserDTO owner;

    public Group() {
    }

    public Group(final String id, final String name, final List<UserDTO> users, final UserDTO owner) {
        this.id = id;
        this.name = name;
        this.users = users;
        this.owner = owner;
    }

    public static Group from(final String name, final UserDTO owner) {
        return new Group(
                UUID.randomUUID().toString(),
                name,
                new ArrayList<>(List.of(owner)),
                owner
        );
    }

    public GroupDTO toDto() {
        return new GroupDTO(this.id, this.name);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<UserDTO> getUsers() {
        return users;
    }

    public void setUsers(List<UserDTO> users) {
        this.users = users;
    }
}

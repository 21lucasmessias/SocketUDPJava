package server.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import dtos.UserDTO;

import java.util.UUID;

public class User {
    @JsonProperty("id")
    private String id;
    @JsonProperty("username")
    private String username;
    @JsonProperty("password")
    private String password;

    public User() {
        this.id = "";
        this.username = "";
        this.password = "";
    }

    public User(final String id, final String username, final String password) {
        this.id = id;
        this.username = username;
        this.password = password;
    }

    public static User from(String username, String password) {
        return new User(UUID.randomUUID().toString(), username, password);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public UserDTO toDto() {
        return new UserDTO(this.id, this.username);
    }
}

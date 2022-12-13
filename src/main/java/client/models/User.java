package client.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import dtos.UserDTO;

import java.io.PrintWriter;
import java.net.Socket;
import java.util.UUID;

public class User {
    @JsonProperty("id")
    private String id;
    @JsonProperty("username")
    private String username;

    public User() {
    }

    public User(final String id, final String username) {
        this.id = id;
        this.username = username;
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
}

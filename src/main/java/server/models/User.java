package server.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import dtos.UserDTO;
import helpers.Mapper;
import messages.home.HomeGroupsMessage;
import messages.home.HomeUsersMessage;
import messages.login.Login;
import messages.login.LoginMessage;
import server.database.Database;

import java.io.PrintWriter;
import java.util.Optional;

public class User {
    @JsonProperty("id")
    private String id;
    @JsonProperty("name")
    private String name;
    @JsonProperty("username")
    private String username;
    @JsonProperty("password")
    private String password;

    public User() {
        this.id = "";
        this.name = "";
        this.username = "";
        this.password = "";
    }

    public User(final String id, final String name, final String username, final String password) {
        this.id = id;
        this.name = name;
        this.username = username;
        this.password = password;
    }

    public static void Login(String message, Mapper mapper, Database database, PrintWriter os) throws JsonProcessingException {
        final Login login = mapper.getMapper().readValue(message, LoginMessage.class).getLogin();
        final Optional<User> user = database.login(login.getUsername(), login.getPassword());

        if (user.isPresent()) {
            os.println("welcome " + user.get().getName());

            os.flush();

            final HomeUsersMessage homeUsersMessage = new HomeUsersMessage(database.getUsers());
            os.println(mapper.getMapper().writeValueAsString(homeUsersMessage));

            os.flush();

            final HomeGroupsMessage homeGroupsMessage = new HomeGroupsMessage(database.getGroups());
            os.println(mapper.getMapper().writeValueAsString(homeGroupsMessage));
        } else {
            os.println("user-not-found");
        }
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
        return new UserDTO(this.id, this.name);
    }
}

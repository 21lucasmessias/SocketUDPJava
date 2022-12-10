package server.messages;

import com.fasterxml.jackson.annotation.JsonProperty;
import server.models.Login;

public class LoginMessage {
    @JsonProperty("login")
    private Login login;

    public LoginMessage() {
    }

    public LoginMessage(Login login) {
        this.login = login;
    }

    public Login getLogin() {
        return login;
    }

    public void setLogin(Login login) {
        this.login = login;
    }
}

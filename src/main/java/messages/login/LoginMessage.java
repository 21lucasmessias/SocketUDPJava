package messages.login;

import com.fasterxml.jackson.annotation.JsonProperty;

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

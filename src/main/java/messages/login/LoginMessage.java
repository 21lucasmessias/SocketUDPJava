package messages.login;

import com.fasterxml.jackson.annotation.JsonProperty;
import messages.Message;

public class LoginMessage implements Message {
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

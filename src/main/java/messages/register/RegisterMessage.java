package messages.register;

import com.fasterxml.jackson.annotation.JsonProperty;

public class RegisterMessage {
    @JsonProperty("register")
    private Register register;

    public RegisterMessage() {
    }

    public RegisterMessage(Register register) {
        this.register = register;
    }

    public Register getRegister() {
        return register;
    }

    public void setRegister(Register register) {
        this.register = register;
    }
}

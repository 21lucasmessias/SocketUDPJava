package messages.login;

import com.fasterxml.jackson.annotation.JsonProperty;
import dtos.UserDTO;
import messages.Message;

import java.util.ArrayList;
import java.util.List;

public class NewConnectionMessage implements Message {
    @JsonProperty("newConnectionMessage")
    private List<UserDTO> newConnectionMessage = new ArrayList<>(List.of());

    public NewConnectionMessage() {
    }

    public NewConnectionMessage(List<UserDTO> newConnectionMessage) {
        this.newConnectionMessage = newConnectionMessage;
    }

    public List<UserDTO> getNewConnectionMessage() {
        return newConnectionMessage;
    }

    public void setNewConnectionMessage(List<UserDTO> newConnectionMessage) {
        this.newConnectionMessage = newConnectionMessage;
    }
}

package messages.home;

import com.fasterxml.jackson.annotation.JsonProperty;
import dtos.UserDTO;
import messages.Message;

import java.util.ArrayList;
import java.util.List;

public class DisconnectMessage implements Message {
    @JsonProperty("disconnectMessage")
    private List<UserDTO> disconnectMessage = new ArrayList<>(List.of());

    public DisconnectMessage() {
    }

    public DisconnectMessage(List<UserDTO> disconnectMessage) {
        this.disconnectMessage = disconnectMessage;
    }

    public List<UserDTO> getDisconnectMessage() {
        return disconnectMessage;
    }

    public void setDisconnectMessage(List<UserDTO> disconnectMessage) {
        this.disconnectMessage = disconnectMessage;
    }
}

package messages.home;

import com.fasterxml.jackson.annotation.JsonProperty;
import dtos.UserDTO;
import messages.Message;

import java.util.ArrayList;
import java.util.List;

public class HomeUsersMessage implements Message {
    @JsonProperty("homeUsersMessage")
    private List<UserDTO> homeUsersMessage = new ArrayList<>(List.of());

    public HomeUsersMessage() {
    }

    public HomeUsersMessage(List<UserDTO> homeUsersMessage) {
        this.homeUsersMessage = homeUsersMessage;
    }

    public List<UserDTO> getHomeUsersMessage() {
        return homeUsersMessage;
    }

    public void setHomeUsersMessage(List<UserDTO> homeUsersMessage) {
        this.homeUsersMessage = homeUsersMessage;
    }
}

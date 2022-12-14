package messages.home;

import com.fasterxml.jackson.annotation.JsonProperty;
import dtos.GroupDTO;
import messages.Message;

import java.util.ArrayList;
import java.util.List;

public class HomeGroupsMessage implements Message {
    @JsonProperty("homeGroupsMessage")
    private List<GroupDTO> homeGroupsMessage = new ArrayList<>(List.of());

    public HomeGroupsMessage() {
    }

    public HomeGroupsMessage(List<GroupDTO> homeGroupsMessage) {
        this.homeGroupsMessage = homeGroupsMessage;
    }

    public List<GroupDTO> getHomeGroupsMessage() {
        return homeGroupsMessage;
    }

    public void setHomeGroupsMessage(List<GroupDTO> homeGroupsMessage) {
        this.homeGroupsMessage = homeGroupsMessage;
    }
}

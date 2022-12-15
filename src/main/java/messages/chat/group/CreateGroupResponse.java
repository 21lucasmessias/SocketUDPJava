package messages.chat.group;

import com.fasterxml.jackson.annotation.JsonProperty;
import dtos.GroupDTO;

import java.util.List;

public class CreateGroupResponse {
    @JsonProperty("groups")
    private List<GroupDTO> groups;

    public CreateGroupResponse() {}

    public CreateGroupResponse(List<GroupDTO> groups) {
        this.groups = groups;
    }

    public List<GroupDTO> getGroups() {
        return groups;
    }

    public void setGroups(List<GroupDTO> groups) {
        this.groups = groups;
    }
}

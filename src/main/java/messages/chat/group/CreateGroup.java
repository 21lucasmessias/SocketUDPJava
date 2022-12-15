package messages.chat.group;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CreateGroup {
    @JsonProperty("name")
    private String name;

    public CreateGroup() {
    }

    public CreateGroup(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

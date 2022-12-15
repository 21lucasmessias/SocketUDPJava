package messages.chat.group;

import com.fasterxml.jackson.annotation.JsonProperty;
import dtos.ChatMessageDTO;

import java.util.List;

public class ResponseJoinGroupMessage {
    @JsonProperty("responseJoinGroupMessage")
    private List<ChatMessageDTO> responseJoinGroupMessage;

    public ResponseJoinGroupMessage() {
    }

    public ResponseJoinGroupMessage(List<ChatMessageDTO> responseJoinGroupMessage) {
        this.responseJoinGroupMessage = responseJoinGroupMessage;
    }

    public List<ChatMessageDTO> getResponseJoinGroupMessage() {
        return responseJoinGroupMessage;
    }

    public void setResponseJoinGroupMessage(List<ChatMessageDTO> responseJoinGroupMessage) {
        this.responseJoinGroupMessage = responseJoinGroupMessage;
    }
}

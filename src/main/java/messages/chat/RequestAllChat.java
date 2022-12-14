package messages.chat;

import com.fasterxml.jackson.annotation.JsonProperty;

public class RequestAllChat {
    @JsonProperty("from")
    private String from;

    @JsonProperty("to")
    private String to;

    public RequestAllChat() {
    }

    public RequestAllChat(String from, String to) {
        this.from = from;
        this.to = to;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }
}

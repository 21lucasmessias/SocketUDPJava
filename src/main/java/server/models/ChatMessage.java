package server.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.Instant;

public class ChatMessage {
    @JsonProperty("createdAt")
    private Instant createdAt;
    @JsonProperty("user")
    private User user;
    @JsonProperty("content")
    private String content;

    public ChatMessage() {
    }

    public ChatMessage(final User user, final String content) {
        this.user = user;
        this.content = content;

        this.createdAt = Instant.now();
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }
}

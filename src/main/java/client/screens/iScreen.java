package client.screens;

import com.fasterxml.jackson.core.JsonProcessingException;

public interface iScreen {
    public void handleMessage(String message) throws JsonProcessingException;
}

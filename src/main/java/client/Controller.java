package client;

import com.fasterxml.jackson.core.JsonProcessingException;
import helpers.Mapper;
import messages.login.Login;
import messages.login.LoginMessage;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

public class Controller {
    protected final PrintWriter writer;
    private final Mapper mapper;

    public Controller(Socket socket) throws IOException {
        this.writer = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())));
        this.mapper = new Mapper();
    }

    public void Login(String username, String password) throws JsonProcessingException {
        LoginMessage loginMessage = new LoginMessage(new Login(username, password));
        this.writer.println(mapper.getMapper().writeValueAsString(loginMessage));
        this.writer.flush();
    }
}

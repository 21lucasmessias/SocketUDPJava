package client;

import client.screens.LoginScreen;
import client.screens.Screen;
import com.fasterxml.jackson.databind.ObjectMapper;
import helpers.Mapper;

import javax.swing.*;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

public class Controller {
    protected final Reader reader;
    protected final PrintWriter writer;
    private final Mapper mapper;
    private final Socket socket;
    private Screen frame;

    public Controller(Socket socket) throws IOException {
        this.mapper = new Mapper();

        this.socket = socket;
        this.writer = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())));
        this.reader = new Reader(socket, this);

        try {
            Thread thread = new Thread(this.reader);
            thread.start();
        } catch (Exception e) {
            this.closeConnection();

            e.printStackTrace();
        }

        this.frame = new LoginScreen(this);
    }

    public void closeConnection() {
        try {
            this.writer.close();
            this.reader.close();
            this.socket.close();
            this.frame.dispose();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ObjectMapper getMapper() {
        return mapper.getMapper();
    }

    public PrintWriter getWriter() {
        return writer;
    }

    public Screen getFrame() {
        return frame;
    }

    public void setFrame(Screen frame) {
        this.frame = frame;
    }
}

package client.screens;

import client.Controller;

import javax.swing.*;

public class Home extends Screen {
    private JPanel container;
    private JList listOfUsers;

    private final Controller controller;

    public Home(Controller controller) {
        this.controller = controller;

        this.setContentPane(this.container);
        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        this.setSize(1080, 720);
    }

    @Override
    public void handleMessage(String message) {

    }
}

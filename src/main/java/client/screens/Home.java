package client.screens;

import client.Controller;
import dtos.UserDTO;
import messages.home.HomeUsersMessage;

import javax.swing.*;

public class Home extends Screen {
    private final Controller controller;
    private JPanel container;
    private JList<UserDTO> listOfUsers;
    private JLabel usersLabel;

    public Home(Controller controller) {
        this.controller = controller;

        this.setContentPane(this.container);
        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        this.setSize(1080, 720);
    }

    @Override
    public void handleMessage(String message) {
        if (message.startsWith("list-of-users")) {
            System.out.println(message);
        } else if (message.startsWith("{\"homeUsersMessage")) {
            try {
                final HomeUsersMessage homeUsersMessage = this.controller.getMapper().readValue(message, HomeUsersMessage.class);
                homeUsersMessage.getHomeUsersMessage().forEach(user -> {
                    final JLabel userName = new JLabel();
                    System.out.println(user.getName());
                    userName.setText(user.getName());
                    listOfUsers.add(userName);
                });

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}

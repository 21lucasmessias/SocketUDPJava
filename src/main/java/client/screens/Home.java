package client.screens;

import client.Controller;
import dtos.UserDTO;
import messages.home.HomeUsersMessage;

import javax.swing.*;
import java.util.ArrayList;

public class Home extends Screen {
    private final Controller controller;
    private JPanel container;
    private JList<String> listOfUsers;
    private JList<String> listOfGroups;

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
                final java.util.List<String> users = new ArrayList<>(java.util.List.of());

                for (UserDTO user : homeUsersMessage.getHomeUsersMessage()) {
                    users.add(user.getId() + " - " + user.getName());
                }

                listOfUsers.setListData(users.toArray(String[]::new));
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (message.startsWith("{\"homeGroupsMessage")) {
            try {
                final HomeUsersMessage homeUsersMessage = this.controller.getMapper().readValue(message, HomeUsersMessage.class);
                final java.util.List<String> users = new ArrayList<>(java.util.List.of());

                for (UserDTO user : homeUsersMessage.getHomeUsersMessage()) {
                    users.add(user.getId() + " - " + user.getName());
                }

                listOfGroups.setListData(users.toArray(String[]::new));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}

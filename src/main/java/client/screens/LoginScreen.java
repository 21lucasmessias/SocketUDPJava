package client.screens;

import client.Controller;
import client.models.User;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import messages.login.Login;
import messages.login.LoginMessage;
import messages.register.Register;
import messages.register.RegisterMessage;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginScreen extends Screen {
    private final Controller controller;
    private JButton loginButton;
    private JButton registerButton;
    private JPasswordField passwordField;
    private JTextField textField;
    private JPanel container;
    private JLabel alert;

    public LoginScreen(Controller controller) {
        this.setContentPane(this.container);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(1080, 720);
        this.setVisible(true);

        this.alert.setVisible(false);

        this.controller = controller;

        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String loginText = textField.getText();
                String passwordText = String.valueOf(passwordField.getPassword());

                login(loginText, passwordText);
            }
        });
        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String loginText = textField.getText();
                String passwordText = String.valueOf(passwordField.getPassword());

                register(loginText, passwordText);
            }
        });
    }

    public void login(String username, String password) {
        try {
            LoginMessage loginMessage = new LoginMessage(new Login(username, password));
            this.controller.getWriter().println(this.controller.getMapper().writeValueAsString(loginMessage));
            this.controller.getWriter().flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void register(String username, String password) {
        try {
            RegisterMessage registerMessage = new RegisterMessage(new Register(username, password));
            this.controller.getWriter().println(this.controller.getMapper().writeValueAsString(registerMessage));
            this.controller.getWriter().flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void handleMessage(String message) throws JsonProcessingException {
        if (message.equals("user-not-found")) {
            this.alert.setText("User not found!");
            this.alert.setVisible(true);
        } else if (message.equals("invalid-user")) {
            this.alert.setText("User already exists!");
            this.alert.setVisible(true);
        } else if (message.startsWith("welcome")) {
            this.alert.setVisible(false);
            this.controller.openHomeScreen();
        } else if (message.startsWith("{\"setUser")) {
            this.controller.setUser(this.controller.getMapper().readValue(message, User.class));
        }
    }
}

package smartparking.ui;

import javax.swing.*;
import java.awt.*;
import smartparking.manager.DataStorage;
import smartparking.manager.UserManager;

public class LoginPanel {

    // Default Admin credentials
    static String[] adminCredentials =
            DataStorage.loadAdminCredentials();

    static String adminUsername =
            adminCredentials[0];

    static String adminPassword =
            adminCredentials[1];

    public LoginPanel() {

        // Main window
        JFrame frame = new JFrame("Park.Me | Login");
        frame.setSize(450, 235);
        frame.setResizable(false);
        frame.setLayout(new BorderLayout(10, 10));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);

        // Title
        JLabel title = new JLabel("Login Panel", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 22));

        // Form panel
        JPanel formPanel = new JPanel(new GridLayout(3, 2, 10, 10));

        // Username
        JLabel usernameLabel = new JLabel("     Username:");
        JTextField usernameField = new JTextField();

        // Password
        JLabel passwordLabel = new JLabel("     Password:");
        JPasswordField passwordField = new JPasswordField();

        // Role
        JLabel roleLabel = new JLabel("     Role:");

        JRadioButton userButton = new JRadioButton("User");
        JRadioButton adminButton = new JRadioButton("Admin");

        ButtonGroup roleGroup = new ButtonGroup();
        roleGroup.add(userButton);
        roleGroup.add(adminButton);

        // Admin selected initially
        adminButton.setSelected(true);

        JPanel rolePanel =
                new JPanel(new FlowLayout(FlowLayout.LEFT));

        rolePanel.add(userButton);
        rolePanel.add(adminButton);

        // Add to form
        formPanel.add(usernameLabel);
        formPanel.add(usernameField);

        formPanel.add(passwordLabel);
        formPanel.add(passwordField);

        formPanel.add(roleLabel);
        formPanel.add(rolePanel);

        // Login button
        JButton loginButton = new JButton("Login");

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(loginButton);

        // Login button action
        loginButton.addActionListener(e -> {

            String username =
                    usernameField.getText().trim();

            String password =
                    new String(
                            passwordField.getPassword()
                    );

            // Admin login
            if (adminButton.isSelected()) {

                if (username.equals(adminUsername)
                        && password.equals(adminPassword)) {

                    JOptionPane.showMessageDialog(
                            frame,
                            "Admin Login Successful!",
                            "Success",
                            JOptionPane.INFORMATION_MESSAGE
                    );

                    frame.dispose();
                    new AdminMainMenu();

                } else {

                    JOptionPane.showMessageDialog(
                            frame,
                            "Incorrect username or password!",
                            "Login Error",
                            JOptionPane.WARNING_MESSAGE
                    );
                }

            }

            // User login
            else if (userButton.isSelected()) {

                if (UserManager.checkLogin(
                        username,
                        password)) {

                    JOptionPane.showMessageDialog(
                            frame,
                            "User Login Successful!",
                            "Success",
                            JOptionPane.INFORMATION_MESSAGE
                    );

                    frame.dispose();
                    new UserMainMenu();

                    // User Main Panel will be connected here later.

                } else {

                    JOptionPane.showMessageDialog(
                            frame,
                            "Incorrect username or password!",
                            "Login Error",
                            JOptionPane.WARNING_MESSAGE
                    );
                }
            }

            // No role selected
            else {

                JOptionPane.showMessageDialog(
                        frame,
                        "Please select a role.",
                        "Warning",
                        JOptionPane.WARNING_MESSAGE
                );
            }
        });

        // Pressing Enter will activate the Login button
        frame.getRootPane().setDefaultButton(loginButton);

        // Add panels to frame
        frame.add(title, BorderLayout.NORTH);
        frame.add(formPanel, BorderLayout.CENTER);
        frame.add(buttonPanel, BorderLayout.SOUTH);

        // Show window
        frame.setVisible(true);
    }
}
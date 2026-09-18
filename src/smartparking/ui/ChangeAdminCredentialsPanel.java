package smartparking.ui;

import smartparking.manager.DataStorage;
import smartparking.manager.PasswordValidator;

import javax.swing.*;
import java.awt.*;

public class ChangeAdminCredentialsPanel {

    JFrame frame;

    public ChangeAdminCredentialsPanel() {

        frame = new JFrame(
                "Park.Me | Change Admin Credentials"
        );

        frame.setSize(500, 330);
        frame.setResizable(false);

        frame.setDefaultCloseOperation(
                JFrame.DISPOSE_ON_CLOSE
        );

        frame.setLocationRelativeTo(null);


        JPanel mainPanel =
                new JPanel(
                        new BorderLayout(10, 10)
                );

        mainPanel.setBorder(
                BorderFactory.createEmptyBorder(
                        15, 15, 15, 15
                )
        );


        // =========================
        // OLD CREDENTIALS
        // =========================

        JPanel oldPanel =
                new JPanel(
                        new GridLayout(
                                2, 2, 10, 10
                        )
                );

        oldPanel.setBorder(
                BorderFactory.createTitledBorder(
                        "Current Admin Credentials"
                )
        );


        JLabel oldUsernameLabel =
                new JLabel("Username:");

        JLabel oldPasswordLabel =
                new JLabel("Password:");


        JTextField oldUsernameField =
                new JTextField();

        JPasswordField oldPasswordField =
                new JPasswordField();


        oldPanel.add(oldUsernameLabel);
        oldPanel.add(oldUsernameField);

        oldPanel.add(oldPasswordLabel);
        oldPanel.add(oldPasswordField);


        // =========================
        // NEW CREDENTIALS
        // =========================

        JPanel newPanel =
                new JPanel(
                        new GridLayout(
                                2, 2, 10, 10
                        )
                );

        newPanel.setBorder(
                BorderFactory.createTitledBorder(
                        "New Admin Credentials"
                )
        );


        JLabel newUsernameLabel =
                new JLabel("New Username:");

        JLabel newPasswordLabel =
                new JLabel("New Password:");


        JTextField newUsernameField =
                new JTextField();

        JTextField newPasswordField =
                new JTextField();


        newPanel.add(newUsernameLabel);
        newPanel.add(newUsernameField);

        newPanel.add(newPasswordLabel);
        newPanel.add(newPasswordField);


        // =========================
        // UPDATE BUTTON
        // =========================

        JButton updateButton =
                new JButton("Update");


        JPanel buttonPanel =
                new JPanel();

        buttonPanel.add(updateButton);


        // =========================
        // UPDATE ACTION
        // =========================

        updateButton.addActionListener(f -> {

            String oldUsername =
                    oldUsernameField
                            .getText()
                            .trim();

            String oldPassword =
                    new String(
                            oldPasswordField
                                    .getPassword()
                    );


            String newUsername =
                    newUsernameField
                            .getText()
                            .trim();

            String newPassword =
                    newPasswordField
                            .getText()
                            .trim();


            // Check old credentials
            if (!oldUsername.equals(
                    LoginPanel.adminUsername)
                    ||
                    !oldPassword.equals(
                            LoginPanel.adminPassword)) {

                JOptionPane.showMessageDialog(
                        frame,
                        "Current username or password is incorrect!",
                        "Verification Failed",
                        JOptionPane.WARNING_MESSAGE
                );

                return;
            }


            // Check new credentials
            if (newUsername.isEmpty()
                    || newPassword.isEmpty()) {

                JOptionPane.showMessageDialog(
                        frame,
                        "New username and password cannot be empty!",
                        "Validation Error",
                        JOptionPane.WARNING_MESSAGE
                );

                return;
            }


            // Prevent storage-format problems
            if (newUsername.contains("|")
                    || newUsername.contains("\n")
                    || newPassword.contains("|")
                    || newPassword.contains("\n")) {

                JOptionPane.showMessageDialog(
                        frame,
                        "Username and password cannot contain | or new lines!",
                        "Validation Error",
                        JOptionPane.WARNING_MESSAGE
                );

                return;
            }


            // Check password strength
            String passwordError =
                    PasswordValidator.validate(newPassword);

            if (!passwordError.equals("")) {

                JOptionPane.showMessageDialog(
                        frame,
                        passwordError,
                        "Invalid Password",
                        JOptionPane.ERROR_MESSAGE
                );

                return;
            }


            // Save encrypted credentials
            DataStorage.saveAdminCredentials(
                    newUsername,
                    newPassword
            );


            // Update currently running application
            LoginPanel.adminUsername =
                    newUsername;

            LoginPanel.adminPassword =
                    newPassword;


            JOptionPane.showMessageDialog(
                    frame,
                    "Admin credentials updated successfully!",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE
            );


            frame.dispose();
        });


        // Pressing Enter will activate the Update button
        frame.getRootPane().setDefaultButton(updateButton);


        // =========================
        // ADD PANELS
        // =========================

        JPanel centerPanel =
                new JPanel(
                        new GridLayout(
                                2, 1, 10, 10
                        )
                );

        centerPanel.add(oldPanel);
        centerPanel.add(newPanel);


        mainPanel.add(
                centerPanel,
                BorderLayout.CENTER
        );

        mainPanel.add(
                buttonPanel,
                BorderLayout.SOUTH
        );


        frame.add(mainPanel);

        frame.setVisible(true);
    }
}
package smartparking.ui;

import javax.swing.*;
import java.awt.*;

public class AdminMainMenu {

    public AdminMainMenu() {

        // Main window
        JFrame frame =
                new JFrame("Park.Me | Admin Panel");

        frame.setSize(500, 400);
        frame.setResizable(false);

        frame.setDefaultCloseOperation(
                JFrame.EXIT_ON_CLOSE
        );

        frame.setLocationRelativeTo(null);


        // =========================
        // MAIN PANEL
        // =========================

        JPanel mainPanel =
                new JPanel(
                        new GridLayout(
                                4, 1, 15, 15
                        )
                );

        mainPanel.setBorder(
                BorderFactory.createEmptyBorder(
                        20, 20, 20, 20
                )
        );


        // =========================
        // ROW 1
        // =========================

        JPanel row1 =
                new JPanel(
                        new GridLayout(
                                1, 2, 15, 0
                        )
                );

        JButton createUserButton =
                new JButton("Create New User");

        JButton editUserButton =
                new JButton("Delete / Edit User");

        row1.add(createUserButton);
        row1.add(editUserButton);


        // =========================
        // ROW 2
        // =========================

        JPanel row2 =
                new JPanel(
                        new GridLayout(
                                1, 2, 15, 0
                        )
                );

        JButton tableButton =
                new JButton("Manage Data Table");

        JButton parkingButton =
                new JButton("Manage Parking Slot");

        row2.add(tableButton);
        row2.add(parkingButton);


        // =========================
        // ROW 3
        // =========================

        JPanel row3 =
                new JPanel(
                        new GridLayout(
                                1, 2, 15, 0
                        )
                );

        JButton cashButton =
                new JButton("Edit Cash Values");

        JButton credentialsButton =
                new JButton(
                        "Change Admin Credentials"
                );

        row3.add(cashButton);
        row3.add(credentialsButton);


        // =========================
        // ROW 4
        // =========================

        JPanel row4 =
                new JPanel(
                        new GridLayout(
                                1, 1
                        )
                );

        JButton logoutButton =
                new JButton("Logout");

        row4.add(logoutButton);


        // =========================
        // BUTTON ACTIONS
        // =========================

        createUserButton.addActionListener(e -> {

            if (!isWindowOpen(
                    "Create New User")) {

                new CreateUserPanel();
            }
        });


        editUserButton.addActionListener(e -> {

            if (!isWindowOpen(
                    "Delete / Edit User")) {

                new UserManagementPanel();
            }
        });

        tableButton.addActionListener(e -> {

            if (!isWindowOpen(
                    "Manage Data Table")) {

                new ManageDataTablePanel();

            }
        });


        parkingButton.addActionListener(e -> {

            if (!isWindowOpen(
                    "Manage Parking Slot")) {

                new ParkingSlotPanel();
            }
        });


        cashButton.addActionListener(e -> {

            if (!isWindowOpen(
                    "Edit Cash Values")) {

                new CashValuePanel();
            }
        });


        credentialsButton.addActionListener(e -> {

            if (!isWindowOpen(
                    "Change Admin Credentials")) {

                new ChangeAdminCredentialsPanel();
            }
        });


        // =========================
        // LOGOUT
        // =========================

        logoutButton.addActionListener(e -> {

            frame.dispose();

            new LoginPanel();
        });


        // =========================
        // ADD ROWS
        // =========================

        mainPanel.add(row1);
        mainPanel.add(row2);
        mainPanel.add(row3);
        mainPanel.add(row4);


        // =========================
        // ADD TO FRAME
        // =========================

        frame.add(mainPanel);

        frame.setVisible(true);
    }


    // =========================
    // CHECK WINDOW ALREADY OPEN
    // =========================

    private boolean isWindowOpen(
            String titlePart) {

        Window[] windows =
                Window.getWindows();


        for (Window window : windows) {

            if (window instanceof JFrame) {

                JFrame frame =
                        (JFrame) window;


                if (frame.isVisible()
                        && frame.getTitle() != null
                        && frame.getTitle()
                        .toLowerCase()
                        .contains(
                                titlePart.toLowerCase()
                        )) {

                    return true;
                }
            }
        }


        return false;
    }
}
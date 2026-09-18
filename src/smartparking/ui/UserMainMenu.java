package smartparking.ui;

import javax.swing.*;
import java.awt.*;

public class UserMainMenu {

    public UserMainMenu() {

        // Main window
        JFrame frame =
                new JFrame("Park.Me | User Panel");

        frame.setSize(600, 250);
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
                                2, 1, 15, 15
                        )
                );

        mainPanel.setBorder(
                BorderFactory.createEmptyBorder(
                        20, 30, 20, 30
                )
        );


        // =========================
        // TOP ROW
        // =========================

        JPanel topPanel =
                new JPanel(
                        new GridLayout(
                                1, 2, 15, 0
                        )
                );


        // =========================
        // BUTTONS
        // =========================

        JButton entryButton =
                new JButton("Entry");

        JButton outButton =
                new JButton("Out");

        JButton logoutButton =
                new JButton("Logout");


        // =========================
        // ENTRY BUTTON
        // =========================

        entryButton.addActionListener(e -> {

            if (!isWindowOpen(
                    "Vehicle Entry")) {

                new EntryPanel();
            }
        });


        // =========================
        // OUT BUTTON
        // =========================

        outButton.addActionListener(e -> {

            if (!isWindowOpen(
                    "Vehicle Out")) {

                new OutPanel();
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
        // ADD BUTTONS
        // =========================

        topPanel.add(entryButton);
        topPanel.add(outButton);


        // =========================
        // ADD TO MAIN PANEL
        // =========================

        mainPanel.add(topPanel);
        mainPanel.add(logoutButton);


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
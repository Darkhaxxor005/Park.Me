package smartparking.ui;

import smartparking.manager.RegistrationDataManager;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class RegistrationPanel extends JPanel {

    private JComboBox<String> areaComboBox;
    private JComboBox<String> initialComboBox;
    private JTextField numberField;

    public RegistrationPanel() {

        setLayout(new BorderLayout());

        setBorder(
                BorderFactory.createTitledBorder(
                        "Registration Number"
                )
        );


        // =========================
        // LOAD DATA
        // =========================

        ArrayList<String> areas =
                RegistrationDataManager.getAreas();

        ArrayList<String> initials =
                RegistrationDataManager.getInitials();


        // =========================
        // FORM
        // =========================

        JPanel formPanel =
                new JPanel(
                        new GridLayout(
                                3, 2, 10, 10
                        )
                );


        // =========================
        // AREA
        // =========================

        JLabel areaLabel =
                new JLabel("Area:");

        areaComboBox =
                new JComboBox<>();

        for (String area : areas) {

            areaComboBox.addItem(area);
        }


        // =========================
        // INITIAL
        // =========================

        JLabel initialLabel =
                new JLabel("Initial:");

        initialComboBox =
                new JComboBox<>();

        for (String initial : initials) {

            initialComboBox.addItem(initial);
        }


        // =========================
        // NUMBER
        // =========================

        JLabel numberLabel =
                new JLabel("Number:");

        numberField =
                new JTextField();


        // =========================
        // ADD TO FORM
        // =========================

        formPanel.add(areaLabel);
        formPanel.add(areaComboBox);

        formPanel.add(initialLabel);
        formPanel.add(initialComboBox);

        formPanel.add(numberLabel);
        formPanel.add(numberField);


        add(
                formPanel,
                BorderLayout.CENTER
        );
    }


    // =========================
    // CHECK SIX DIGIT NUMBER
    // =========================

    public boolean isNumberValid() {

        String number =
                numberField.getText().trim();

        if (number.length() != 6) {

            return false;
        }


        for (int i = 0;
             i < number.length();
             i++) {

            char ch =
                    number.charAt(i);

            if (ch < '0' || ch > '9') {

                return false;
            }
        }


        return true;
    }


    // =========================
    // GET COMPLETE REGISTRATION
    // =========================

    public String getRegistrationNumber() {

        String area =
                (String)
                        areaComboBox
                                .getSelectedItem();

        String initial =
                (String)
                        initialComboBox
                                .getSelectedItem();

        String number =
                numberField
                        .getText()
                        .trim();


        return area
                + " "
                + initial
                + " "
                + number;
    }


    // =========================
    // CLEAR NUMBER
    // =========================

    public void clearNumber() {

        numberField.setText("");
    }
    // =========================
// SET COMPLETE REGISTRATION
// =========================

    public void setRegistrationNumber(
            String registration) {

        if (registration == null) {
            return;
        }

        registration =
                registration.trim();

        int lastSpace =
                registration.lastIndexOf(" ");

        if (lastSpace == -1) {
            return;
        }

        String number =
                registration.substring(
                        lastSpace + 1
                );

        int secondLastSpace =
                registration.lastIndexOf(
                        " ",
                        lastSpace - 1
                );

        if (secondLastSpace == -1) {
            return;
        }

        String initial =
                registration.substring(
                        secondLastSpace + 1,
                        lastSpace
                );

        String area =
                registration.substring(
                        0,
                        secondLastSpace
                );


        areaComboBox.setSelectedItem(area);

        initialComboBox.setSelectedItem(initial);

        numberField.setText(number);
    }
}
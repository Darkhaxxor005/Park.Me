package smartparking.ui;

import smartparking.manager.ParkingManager;
import smartparking.manager.ParkingRecordManager;
import smartparking.model.ParkingRecord;
import smartparking.model.ParkingSlot;

import javax.swing.*;
import java.awt.*;
import java.time.DateTimeException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class UpdateParkingRecordPanel {

    private JFrame frame;

    private ParkingRecord record;

    private JRadioButton carRadio;
    private JRadioButton truckRadio;
    private JRadioButton busRadio;
    private JRadioButton bikeRadio;

    private RegistrationPanel registrationPanel;

    private JButton inTimeButton;
    private JButton outTimeButton;

    private JComboBox<String> slotComboBox;

    private JTextField billField;

    private LocalDateTime inTime;
    private LocalDateTime outTime;

    private boolean activeVehicle;

    private final DateTimeFormatter formatter =
            DateTimeFormatter.ofPattern(
                    "dd/MM/yyyy HH:mm:ss"
            );


    public UpdateParkingRecordPanel(
            ParkingRecord record,
            Runnable refreshTable
    ) {

        this.record = record;

        /*
         * Out Time = "--"
         * means vehicle is currently parked.
         */
        activeVehicle =
                record.getOutTime()
                        .equals("--");


        // =========================
        // FRAME
        // =========================

        frame = new JFrame(
                "Park.Me | Update Parking Record"
        );

        frame.setSize(
                850,
                650
        );

        frame.setResizable(false);

        frame.setDefaultCloseOperation(
                JFrame.DISPOSE_ON_CLOSE
        );

        frame.setLocationRelativeTo(null);


        // =========================
        // MAIN PANEL
        // =========================

        JPanel mainPanel =
                new JPanel(
                        new BorderLayout(
                                15,
                                15
                        )
                );

        mainPanel.setBorder(
                BorderFactory.createEmptyBorder(
                        15,
                        20,
                        15,
                        20
                )
        );


        // =========================
        // FORM PANEL
        // =========================

        JPanel formPanel =
                new JPanel(
                        new GridBagLayout()
                );

        GridBagConstraints gbc =
                new GridBagConstraints();

        gbc.insets =
                new Insets(
                        8,
                        8,
                        8,
                        8
                );

        gbc.fill =
                GridBagConstraints.HORIZONTAL;


        // ==================================================
        // ROW 1 : REGISTRATION PANEL
        // FULL WIDTH
        // ==================================================

        registrationPanel =
                new RegistrationPanel();

        registrationPanel.setRegistrationNumber(
                record.getRegistration()
        );


        gbc.gridx = 0;
        gbc.gridy = 0;

        gbc.gridwidth = 2;

        gbc.weightx = 1;
        gbc.weighty = 0;

        gbc.fill =
                GridBagConstraints.HORIZONTAL;


        formPanel.add(
                registrationPanel,
                gbc
        );


        // ==================================================
        // ROW 2 : VEHICLE TYPE
        // OLD VEHICLE TYPE SELECTED
        // ==================================================

        JLabel vehicleTypeLabel =
                new JLabel(
                        "Vehicle Type:"
                );

        vehicleTypeLabel.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        14
                )
        );


        JPanel vehicleTypePanel =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.LEFT,
                                12,
                                0
                        )
                );


        carRadio =
                new JRadioButton(
                        "Car"
                );

        truckRadio =
                new JRadioButton(
                        "Truck"
                );

        busRadio =
                new JRadioButton(
                        "Bus"
                );

        bikeRadio =
                new JRadioButton(
                        "Bike"
                );


        ButtonGroup vehicleGroup =
                new ButtonGroup();

        vehicleGroup.add(
                carRadio
        );

        vehicleGroup.add(
                truckRadio
        );

        vehicleGroup.add(
                busRadio
        );

        vehicleGroup.add(
                bikeRadio
        );


        vehicleTypePanel.add(
                carRadio
        );

        vehicleTypePanel.add(
                truckRadio
        );

        vehicleTypePanel.add(
                busRadio
        );

        vehicleTypePanel.add(
                bikeRadio
        );


        selectOldVehicleType();


        gbc.gridx = 0;
        gbc.gridy = 1;

        gbc.gridwidth = 1;

        gbc.weightx = 0;

        gbc.fill =
                GridBagConstraints.HORIZONTAL;


        formPanel.add(
                vehicleTypeLabel,
                gbc
        );


        gbc.gridx = 1;
        gbc.weightx = 1;


        formPanel.add(
                vehicleTypePanel,
                gbc
        );


        // ==================================================
        // ROW 3 : IN TIME
        // ==================================================

        JLabel inTimeLabel =
                new JLabel(
                        "In Time:"
                );

        inTimeLabel.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        14
                )
        );


        inTime =
                LocalDateTime.parse(
                        record.getInTime(),
                        formatter
                );


        inTimeButton =
                new JButton(
                        inTime.format(
                                formatter
                        )
                );

        inTimeButton.setHorizontalAlignment(
                SwingConstants.LEFT
        );


        inTimeButton.addActionListener(
                e -> {

                    String result =
                            showDateTimePopup(
                                    "Edit In Time",
                                    inTime
                            );


                    if (result != null) {

                        inTime =
                                LocalDateTime.parse(
                                        result,
                                        formatter
                                );

                        inTimeButton.setText(
                                result
                        );
                    }
                }
        );


        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 1;
        gbc.weightx = 0;


        formPanel.add(
                inTimeLabel,
                gbc
        );


        gbc.gridx = 1;
        gbc.weightx = 1;


        formPanel.add(
                inTimeButton,
                gbc
        );


        // ==================================================
        // ROW 4 : OUT TIME
        //
        // CHECKED IN:
        //     LOCKED
        //
        // CHECKED OUT:
        //     EDITABLE
        // ==================================================

        JLabel outTimeLabel =
                new JLabel(
                        "Out Time:"
                );

        outTimeLabel.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        14
                )
        );


        if (activeVehicle) {

            /*
             * Vehicle is currently parked.
             * Out Time must remain "--".
             */

            outTimeButton =
                    new JButton(
                            "--"
                    );

            outTimeButton.setEnabled(
                    false
            );

        } else {

            /*
             * Vehicle has already checked out.
             * Out Time can be edited.
             */

            outTime =
                    LocalDateTime.parse(
                            record.getOutTime(),
                            formatter
                    );


            outTimeButton =
                    new JButton(
                            outTime.format(
                                    formatter
                            )
                    );


            outTimeButton.setHorizontalAlignment(
                    SwingConstants.LEFT
            );


            outTimeButton.addActionListener(
                    e -> {

                        String result =
                                showDateTimePopup(
                                        "Edit Out Time",
                                        outTime
                                );


                        if (result != null) {

                            outTime =
                                    LocalDateTime.parse(
                                            result,
                                            formatter
                                    );

                            outTimeButton.setText(
                                    result
                            );
                        }
                    }
            );
        }


        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.weightx = 0;


        formPanel.add(
                outTimeLabel,
                gbc
        );


        gbc.gridx = 1;
        gbc.weightx = 1;


        formPanel.add(
                outTimeButton,
                gbc
        );


        // ==================================================
        // ROW 5 : ALLOCATED SLOT
        //
        // CHECKED IN:
        //     EDITABLE
        //
        // CHECKED OUT:
        //     LOCKED
        // ==================================================

        JLabel slotLabel =
                new JLabel(
                        "Allocated Slot:"
                );

        slotLabel.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        14
                )
        );


        slotComboBox =
                new JComboBox<>();


        if (activeVehicle) {

            /*
             * Currently parked.
             * Slot can be changed.
             */

            slotComboBox.setEnabled(
                    true
            );

            refreshSlots();

        } else {

            /*
             * Already checked out.
             * Slot is locked.
             */

            slotComboBox.addItem(
                    record.getAllocatedSlot()
            );

            slotComboBox.setEnabled(
                    false
            );
        }


        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.weightx = 0;


        formPanel.add(
                slotLabel,
                gbc
        );


        gbc.gridx = 1;
        gbc.weightx = 1;


        formPanel.add(
                slotComboBox,
                gbc
        );


        // ==================================================
        // ROW 6 : BILL
        // ==================================================

        JLabel billLabel =
                new JLabel(
                        "Bill:"
                );

        billLabel.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        14
                )
        );


        billField =
                new JTextField(
                        record.getBill()
                );


        /*
         * Checked-in vehicle:
         * Bill is locked because
         * checkout has not happened yet.
         *
         * Checked-out vehicle:
         * Bill can be edited.
         */

        if (activeVehicle) {

            billField.setEnabled(false);

        } else {

            billField.setEnabled(true);
        }


        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.weightx = 0;


        formPanel.add(
                billLabel,
                gbc
        );


        gbc.gridx = 1;
        gbc.weightx = 1;


        formPanel.add(
                billField,
                gbc
        );

        // ==================================================
        // VEHICLE TYPE BUTTON ACTIONS
        // ==================================================

        carRadio.addActionListener(
                e -> refreshSlots()
        );

        truckRadio.addActionListener(
                e -> refreshSlots()
        );

        busRadio.addActionListener(
                e -> refreshSlots()
        );

        bikeRadio.addActionListener(
                e -> refreshSlots()
        );


        // =========================
        // UPDATE BUTTON
        // =========================

        JButton updateButton =
                new JButton(
                        "Update"
                );

        updateButton.setPreferredSize(
                new Dimension(
                        120,
                        35
                )
        );


        updateButton.addActionListener(
                e -> updateRecord(
                        refreshTable
                )
        );


        JPanel buttonPanel =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.CENTER
                        )
                );

        buttonPanel.add(
                updateButton
        );


        // =========================
        // ADD TO MAIN PANEL
        // =========================

        mainPanel.add(
                formPanel,
                BorderLayout.CENTER
        );

        mainPanel.add(
                buttonPanel,
                BorderLayout.SOUTH
        );


        frame.add(
                mainPanel
        );

        frame.setVisible(
                true
        );
    }


    // ==================================================
    // SELECT OLD VEHICLE TYPE
    // ==================================================

    private void selectOldVehicleType() {

        String type =
                record.getVehicleType();


        switch (type) {

            case "Car":

                carRadio.setSelected(
                        true
                );

                break;


            case "Truck":

                truckRadio.setSelected(
                        true
                );

                break;


            case "Bus":

                busRadio.setSelected(
                        true
                );

                break;


            case "Bike":

                bikeRadio.setSelected(
                        true
                );

                break;
        }
    }


    // ==================================================
    // GET SELECTED VEHICLE TYPE
    // ==================================================

    private String getSelectedVehicleType() {

        if (carRadio.isSelected()) {
            return "Car";
        }


        if (truckRadio.isSelected()) {
            return "Truck";
        }


        if (busRadio.isSelected()) {
            return "Bus";
        }


        return "Bike";
    }


    // ==================================================
    // REFRESH AVAILABLE SLOTS
    // ==================================================

    private void refreshSlots() {

        if (!activeVehicle) {

            return;
        }


        String vehicleType =
                getSelectedVehicleType();


        slotComboBox.removeAllItems();


        ArrayList<ParkingSlot> slots =
                ParkingManager.getSlots(
                        vehicleType
                );


        if (slots == null) {

            return;
        }


        String currentSlot =
                record.getAllocatedSlot();


        /*
         * When vehicle type remains the same,
         * keep its current occupied slot available
         * for selection.
         */

        if (vehicleType.equals(
                record.getVehicleType()
        )) {

            for (ParkingSlot slot :
                    slots) {

                if (slot.getSlotId()
                        .equals(
                                currentSlot
                        )) {

                    slotComboBox.addItem(
                            currentSlot
                    );

                    break;
                }
            }
        }


        /*
         * Add only available slots.
         */

        for (ParkingSlot slot :
                slots) {

            if (!slot.isOccupied()) {

                if (!slot.getSlotId()
                        .equals(
                                currentSlot
                        )) {

                    slotComboBox.addItem(
                            slot.getSlotId()
                    );
                }
            }
        }


        /*
         * No available slot.
         */

        if (slotComboBox.getItemCount()
                == 0) {

            slotComboBox.addItem(
                    "--"
            );
        }
    }


    // ==================================================
    // UPDATE RECORD
    // ==================================================

    private void updateRecord(
            Runnable refreshTable
    ) {

        // =========================
        // REGISTRATION VALIDATION
        // =========================

        if (!registrationPanel
                .isNumberValid()) {

            JOptionPane.showMessageDialog(
                    frame,
                    "Registration number must contain exactly 6 digits.",
                    "Validation Error",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }


        String newRegistration =
                registrationPanel
                        .getRegistrationNumber();


        // =========================
        // DUPLICATE ACTIVE VEHICLE
        // =========================
        //
        // Only checked-in vehicles need
        // duplicate registration checking.
        //
        // A checked-out record is historical,
        // so the same registration may already
        // have another active record.

        if (activeVehicle) {

            for (ParkingRecord other :
                    ParkingRecordManager
                            .getRecords()) {

                if (other != record
                        && other.getRegistration()
                        .equalsIgnoreCase(
                                newRegistration
                        )
                        && other.getOutTime()
                        .equals("--")) {

                    JOptionPane.showMessageDialog(
                            frame,
                            "This vehicle is already parked.",
                            "Validation Error",
                            JOptionPane.WARNING_MESSAGE
                    );

                    return;
                }
            }
        }

        // =========================
        // TIME VALIDATION
        // =========================

        if (!activeVehicle) {

            if (outTime.isBefore(
                    inTime
            )) {

                JOptionPane.showMessageDialog(
                        frame,
                        "Out Time cannot be before In Time.",
                        "Validation Error",
                        JOptionPane.WARNING_MESSAGE
                );

                return;
            }
        }


        // =========================
        // BILL VALIDATION
        // =========================

        String newBill =
                billField
                        .getText()
                        .trim();


        // Checked-in vehicle
        // Bill cannot be edited or validated.
        // Keep the existing bill unchanged.

        if (activeVehicle) {

            newBill = record.getBill();

        } else {

            // Checked-out vehicle
            // Bill can be edited.

            if (newBill.isEmpty()
                    || !isWholeNumber(newBill)) {

                JOptionPane.showMessageDialog(
                        frame,
                        "Bill must contain a whole number.",
                        "Validation Error",
                        JOptionPane.WARNING_MESSAGE
                );

                return;
            }
        }


        // =========================
        // NEW VEHICLE TYPE
        // =========================

        String newVehicleType =
                getSelectedVehicleType();


        // ==================================================
        // CHECKED IN VEHICLE
        // UPDATE LIVE PARKING SLOT
        // ==================================================

        if (activeVehicle) {

            String newSlot =
                    (String)
                            slotComboBox
                                    .getSelectedItem();


            if (newSlot == null
                    || newSlot.equals(
                    "--"
            )) {

                JOptionPane.showMessageDialog(
                        frame,
                        "Please select an available parking slot.",
                        "Validation Error",
                        JOptionPane.WARNING_MESSAGE
                );

                return;
            }


            updateActiveVehicleSlot(
                    newVehicleType,
                    newSlot,
                    newRegistration
            );


            record.setAllocatedSlot(
                    newSlot
            );
        }


        // =========================
        // UPDATE RECORD
        // =========================

        record.setVehicleType(
                newVehicleType
        );


        record.setRegistration(
                newRegistration
        );


        record.setInTime(
                inTime.format(
                        formatter
                )
        );


        /*
         * Only checked-out vehicles
         * can change Out Time.
         *
         * Checked-in vehicles stay "--".
         */

        if (!activeVehicle) {

            record.setOutTime(
                    outTime.format(
                            formatter
                    )
            );
        }


        // =========================
        // UPDATE BILL
        // =========================

        if (!activeVehicle) {

            record.setBill(
                    newBill
            );
        }


        // =========================
        // SAVE
        // =========================

        ParkingManager.saveSlotData();

        ParkingRecordManager.saveRecords();


        // =========================
        // REFRESH TABLE
        // =========================

        refreshTable.run();


        // =========================
        // SUCCESS
        // =========================

        JOptionPane.showMessageDialog(
                frame,
                "Parking record updated successfully.",
                "Success",
                JOptionPane.INFORMATION_MESSAGE
        );


        frame.dispose();
    }


    // ==================================================
    // UPDATE ACTIVE VEHICLE SLOT
    // ==================================================

    private void updateActiveVehicleSlot(
            String newVehicleType,
            String newSlotId,
            String newRegistration
    ) {

        ParkingSlot oldSlot =
                findSlot(
                        record.getVehicleType(),
                        record.getAllocatedSlot()
                );


        ParkingSlot newSlot =
                findSlot(
                        newVehicleType,
                        newSlotId
                );


        if (oldSlot == null
                || newSlot == null) {

            return;
        }


        // =========================
        // SAME SLOT
        // ONLY REGISTRATION CHANGES
        // =========================

        if (oldSlot == newSlot) {

            oldSlot.setVehicleRegistration(
                    newRegistration
            );

            oldSlot.setOccupied(
                    true
            );

            return;
        }


        // =========================
        // RELEASE OLD SLOT
        // =========================

        oldSlot.release();


        // =========================
        // OCCUPY NEW SLOT
        // =========================

        newSlot.occupy(
                newRegistration
        );
    }


    // ==================================================
    // FIND SLOT
    // ==================================================

    private ParkingSlot findSlot(
            String vehicleType,
            String slotId
    ) {

        ArrayList<ParkingSlot> slots =
                ParkingManager.getSlots(
                        vehicleType
                );


        if (slots == null) {

            return null;
        }


        for (ParkingSlot slot :
                slots) {

            if (slot.getSlotId()
                    .equals(
                            slotId
                    )) {

                return slot;
            }
        }


        return null;
    }


    // ==================================================
    // WHOLE NUMBER
    // ==================================================

    private boolean isWholeNumber(
            String value
    ) {

        if (value.isEmpty()) {

            return false;
        }


        for (int i = 0;
             i < value.length();
             i++) {

            char ch =
                    value.charAt(i);


            if (ch < '0'
                    || ch > '9') {

                return false;
            }
        }


        return true;
    }


    // ==================================================
    // DATE + TIME POPUP
    // ==================================================

    private String showDateTimePopup(
            String title,
            LocalDateTime current
    ) {

        JDialog dialog =
                new JDialog(
                        frame,
                        title,
                        true
                );

        dialog.setSize(
                460,
                310
        );

        dialog.setResizable(false);

        dialog.setLocationRelativeTo(
                frame
        );


        // =========================
        // MAIN POPUP PANEL
        // =========================

        JPanel mainPanel =
                new JPanel(
                        new BorderLayout(
                                10,
                                10
                        )
                );

        mainPanel.setBorder(
                BorderFactory.createEmptyBorder(
                        15,
                        15,
                        15,
                        15
                )
        );


        // ==================================================
        // DATE PANEL
        // ==================================================

        JPanel datePanel =
                new JPanel(
                        new GridLayout(
                                2,
                                3,
                                10,
                                7
                        )
                );

        datePanel.setBorder(
                BorderFactory.createTitledBorder(
                        "Date"
                )
        );


        JSpinner daySpinner =
                new JSpinner(
                        new SpinnerNumberModel(
                                current.getDayOfMonth(),
                                1,
                                31,
                                1
                        )
                );


        JSpinner monthSpinner =
                new JSpinner(
                        new SpinnerNumberModel(
                                current.getMonthValue(),
                                1,
                                12,
                                1
                        )
                );


        JSpinner yearSpinner =
                new JSpinner(
                        new SpinnerNumberModel(
                                current.getYear(),
                                2000,
                                2100,
                                1
                        )
                );


        datePanel.add(
                new JLabel(
                        "Day",
                        SwingConstants.CENTER
                )
        );


        datePanel.add(
                new JLabel(
                        "Month",
                        SwingConstants.CENTER
                )
        );


        datePanel.add(
                new JLabel(
                        "Year",
                        SwingConstants.CENTER
                )
        );


        datePanel.add(
                daySpinner
        );


        datePanel.add(
                monthSpinner
        );


        datePanel.add(
                yearSpinner
        );


        // ==================================================
        // TIME PANEL
        // ==================================================

        JPanel timePanel =
                new JPanel(
                        new GridLayout(
                                2,
                                3,
                                10,
                                7
                        )
                );

        timePanel.setBorder(
                BorderFactory.createTitledBorder(
                        "Time"
                )
        );


        JSpinner hourSpinner =
                new JSpinner(
                        new SpinnerNumberModel(
                                current.getHour(),
                                0,
                                23,
                                1
                        )
                );


        JSpinner minuteSpinner =
                new JSpinner(
                        new SpinnerNumberModel(
                                current.getMinute(),
                                0,
                                59,
                                1
                        )
                );


        JSpinner secondSpinner =
                new JSpinner(
                        new SpinnerNumberModel(
                                current.getSecond(),
                                0,
                                59,
                                1
                        )
                );


        timePanel.add(
                new JLabel(
                        "Hour",
                        SwingConstants.CENTER
                )
        );


        timePanel.add(
                new JLabel(
                        "Minute",
                        SwingConstants.CENTER
                )
        );


        timePanel.add(
                new JLabel(
                        "Second",
                        SwingConstants.CENTER
                )
        );


        timePanel.add(
                hourSpinner
        );


        timePanel.add(
                minuteSpinner
        );


        timePanel.add(
                secondSpinner
        );


        // ==================================================
        // BUTTONS
        // ==================================================

        JButton okButton =
                new JButton(
                        "OK"
                );

        JButton cancelButton =
                new JButton(
                        "Cancel"
                );


        JPanel buttonPanel =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.CENTER,
                                10,
                                0
                        )
                );


        buttonPanel.add(
                okButton
        );


        buttonPanel.add(
                cancelButton
        );


        final String[] result =
                {null};


        // ==================================================
        // OK BUTTON
        // ==================================================

        okButton.addActionListener(
                e -> {

                    int day =
                            (Integer)
                                    daySpinner
                                            .getValue();


                    int month =
                            (Integer)
                                    monthSpinner
                                            .getValue();


                    int year =
                            (Integer)
                                    yearSpinner
                                            .getValue();


                    int hour =
                            (Integer)
                                    hourSpinner
                                            .getValue();


                    int minute =
                            (Integer)
                                    minuteSpinner
                                            .getValue();


                    int second =
                            (Integer)
                                    secondSpinner
                                            .getValue();


                    try {

                        LocalDateTime selected =
                                LocalDateTime.of(
                                        year,
                                        month,
                                        day,
                                        hour,
                                        minute,
                                        second
                                );


                        result[0] =
                                selected.format(
                                        formatter
                                );


                        dialog.dispose();

                    } catch (
                            DateTimeException ex
                    ) {

                        JOptionPane.showMessageDialog(
                                dialog,
                                "Invalid date.",
                                "Validation Error",
                                JOptionPane.WARNING_MESSAGE
                        );
                    }
                }
        );


        // ==================================================
        // CANCEL BUTTON
        // ==================================================

        cancelButton.addActionListener(
                e -> dialog.dispose()
        );


        // =========================
        // CENTER
        // =========================

        JPanel centerPanel =
                new JPanel(
                        new GridLayout(
                                2,
                                1,
                                10,
                                10
                        )
                );


        centerPanel.add(
                datePanel
        );


        centerPanel.add(
                timePanel
        );


        // =========================
        // ADD TO DIALOG
        // =========================

        mainPanel.add(
                centerPanel,
                BorderLayout.CENTER
        );


        mainPanel.add(
                buttonPanel,
                BorderLayout.SOUTH
        );


        dialog.add(
                mainPanel
        );


        dialog.setVisible(
                true
        );


        return result[0];
    }
}
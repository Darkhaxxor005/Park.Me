package smartparking.ui;

import smartparking.manager.CashManager;
import smartparking.manager.ParkingManager;
import smartparking.manager.ParkingRecordManager;
import smartparking.model.ParkingFee;
import smartparking.model.ParkingRecord;
import smartparking.model.ParkingSlot;

import javax.swing.*;
import java.awt.*;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterJob;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class OutPanel {

    private ParkingRecord currentRecord;

    private LocalDateTime previewOutTime;

    private int previewBill;

    private String previewOutTimeText;

    private JLabel vehicleTypeValue;
    private JLabel registrationValue;
    private JLabel inTimeValue;
    private JLabel slotValue;
    private JLabel outTimeValue;
    private JLabel billValue;

    private JButton printButton;

    private JFrame frame;

    private final DateTimeFormatter formatter =
            DateTimeFormatter.ofPattern(
                    "dd/MM/yyyy HH:mm:ss"
            );


    public OutPanel() {

        frame =
                new JFrame("Park.Me | Vehicle Out");

        frame.setSize(500, 450);
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
                                10, 10
                        )
                );

        mainPanel.setBorder(
                BorderFactory.createEmptyBorder(
                        15, 20, 15, 20
                )
        );


        // =========================
        // REGISTRATION
        // =========================

        RegistrationPanel registrationPanel =
                new RegistrationPanel();


        // =========================
        // CHECKOUT BUTTON
        // =========================

        JButton checkoutButton =
                new JButton("Checkout");


        JPanel checkoutButtonPanel =
                new JPanel();

        checkoutButtonPanel.add(
                checkoutButton
        );


        JPanel checkoutPanel =
                new JPanel(
                        new BorderLayout(
                                5, 5
                        )
                );

        checkoutPanel.setBorder(
                BorderFactory.createTitledBorder(
                        "Vehicle Checkout"
                )
        );

        checkoutPanel.add(
                registrationPanel,
                BorderLayout.CENTER
        );

        checkoutPanel.add(
                checkoutButtonPanel,
                BorderLayout.SOUTH
        );


        // =========================
        // RECEIPT DETAILS
        // =========================

        JPanel receiptPanel =
                new JPanel(
                        new GridLayout(
                                6, 2, 10, 8
                        )
                );

        receiptPanel.setBorder(
                BorderFactory.createTitledBorder(
                        "Parking Receipt"
                )
        );


        JLabel vehicleTypeLabel =
                new JLabel("Vehicle Type:");

        vehicleTypeValue =
                new JLabel("--");


        JLabel registrationLabel =
                new JLabel("Registration:");

        registrationValue =
                new JLabel("--");


        JLabel inTimeLabel =
                new JLabel("In-Time:");

        inTimeValue =
                new JLabel("--");


        JLabel outTimeLabel =
                new JLabel("Out-Time:");

        outTimeValue =
                new JLabel("--");


        JLabel slotLabel =
                new JLabel("Allocated Slot:");

        slotValue =
                new JLabel("--");


        JLabel billLabel =
                new JLabel("Total Bill:");

        billValue =
                new JLabel("--");


        receiptPanel.add(
                vehicleTypeLabel
        );

        receiptPanel.add(
                vehicleTypeValue
        );


        receiptPanel.add(
                registrationLabel
        );

        receiptPanel.add(
                registrationValue
        );


        receiptPanel.add(
                inTimeLabel
        );

        receiptPanel.add(
                inTimeValue
        );


        receiptPanel.add(
                outTimeLabel
        );

        receiptPanel.add(
                outTimeValue
        );


        receiptPanel.add(
                slotLabel
        );

        receiptPanel.add(
                slotValue
        );


        receiptPanel.add(
                billLabel
        );

        receiptPanel.add(
                billValue
        );


        // =========================
        // PRINT BUTTON
        // =========================

        printButton =
                new JButton("Print");

        printButton.setEnabled(false);


        JPanel printPanel =
                new JPanel();

        printPanel.add(
                printButton
        );


        // =========================
        // CHECKOUT ACTION
        // =========================

        checkoutButton.addActionListener(e -> {

            String registration =
                    registrationPanel
                            .getRegistrationNumber();


            // -------------------------
            // Validate registration
            // -------------------------

            if (!registrationPanel.isNumberValid()) {

                JOptionPane.showMessageDialog(
                        frame,
                        "Registration number must contain exactly 6 digits.",
                        "Validation Error",
                        JOptionPane.WARNING_MESSAGE
                );

                return;
            }


            // -------------------------
            // Find currently parked
            // -------------------------

            ParkingRecord activeRecord =
                    ParkingRecordManager
                            .findActiveByRegistration(
                                    registration
                            );


            if (activeRecord == null) {

                // -------------------------
                // Check already checked out
                // -------------------------

                ParkingRecord oldRecord =
                        ParkingRecordManager
                                .findByRegistration(
                                        registration
                                );


                if (oldRecord != null) {

                    currentRecord = null;

                    printButton.setEnabled(false);

                    JOptionPane.showMessageDialog(
                            frame,
                            "This vehicle has already been checked out.",
                            "Vehicle Already Checked Out",
                            JOptionPane.WARNING_MESSAGE
                    );

                    return;
                }


                // -------------------------
                // Not found
                // -------------------------

                currentRecord = null;

                printButton.setEnabled(false);

                JOptionPane.showMessageDialog(
                        frame,
                        "This vehicle was not found.",
                        "Vehicle Not Found",
                        JOptionPane.WARNING_MESSAGE
                );

                return;
            }


            // =========================
            // CREATE CHECKOUT PREVIEW
            // =========================

            LocalDateTime outTime =
                    LocalDateTime.now();

            String outTimeText =
                    outTime.format(
                            formatter
                    );


            LocalDateTime entryTime;

            try {

                entryTime =
                        LocalDateTime.parse(
                                activeRecord.getInTime(),
                                formatter
                        );

            } catch (Exception ex) {

                JOptionPane.showMessageDialog(
                        frame,
                        "Invalid entry time in parking record.",
                        "Calculation Error",
                        JOptionPane.ERROR_MESSAGE
                );

                return;
            }


            Duration duration =
                    Duration.between(
                            entryTime,
                            outTime
                    );


            if (duration.isNegative()) {

                JOptionPane.showMessageDialog(
                        frame,
                        "Out-time cannot be earlier than in-time.",
                        "Calculation Error",
                        JOptionPane.ERROR_MESSAGE
                );

                return;
            }


            long totalMinutes =
                    duration.toMinutes();

            long parkingHours =
                    totalMinutes / 60;

            long remainingMinutes =
                    totalMinutes % 60;


            // 30 minutes or more = next hour
            if (remainingMinutes >= 30) {

                parkingHours++;
            }


            // =========================
            // GET FEE
            // =========================

            ParkingFee fee =
                    CashManager.getFee(
                            activeRecord
                                    .getVehicleType()
                    );


            if (fee == null) {

                JOptionPane.showMessageDialog(
                        frame,
                        "Parking fee information was not found.",
                        "Calculation Error",
                        JOptionPane.ERROR_MESSAGE
                );

                return;
            }


            int totalBill =
                    fee.getBasicFee()
                            + (int) parkingHours
                            * fee.getFeePerHour();


            // =========================
            // STORE PREVIEW DATA
            // =========================

            currentRecord =
                    activeRecord;

            previewOutTime =
                    outTime;

            previewOutTimeText =
                    outTimeText;

            previewBill =
                    totalBill;


            // =========================
            // SHOW ALL INFORMATION
            // =========================

            vehicleTypeValue.setText(
                    activeRecord
                            .getVehicleType()
            );

            registrationValue.setText(
                    activeRecord
                            .getRegistration()
            );

            inTimeValue.setText(
                    activeRecord
                            .getInTime()
            );

            outTimeValue.setText(
                    outTimeText
            );

            slotValue.setText(
                    activeRecord
                            .getAllocatedSlot()
            );

            billValue.setText(
                    String.valueOf(
                            totalBill
                    )
            );


            // Enable final Print button
            printButton.setEnabled(true);
        });


        // =========================
        // PRINT / FINAL CHECKOUT
        // =========================

        printButton.addActionListener(e -> {

            if (currentRecord == null) {

                return;
            }


            String vehicleType =
                    currentRecord
                            .getVehicleType();

            String registration =
                    currentRecord
                            .getRegistration();

            String inTime =
                    currentRecord
                            .getInTime();

            String allocatedSlot =
                    currentRecord
                            .getAllocatedSlot();


            // =========================
            // USE PREVIEW TIME/BILL
            // =========================

            String outTimeText =
                    previewOutTimeText;

            int totalBill =
                    previewBill;


            // =========================
            // RELEASE SLOT
            // =========================

            ArrayList<ParkingSlot> slots =
                    ParkingManager.getSlots(
                            vehicleType
                    );


            if (slots != null) {

                for (ParkingSlot slot :
                        slots) {

                    if (slot.getSlotId()
                            .equals(
                                    allocatedSlot
                            )) {

                        slot.release();

                        break;
                    }
                }
            }


            // Save slot changes
            ParkingManager.saveSlotData();


            // =========================
            // UPDATE PARKING RECORD
            // =========================

            currentRecord.setOutTime(
                    outTimeText
            );

            currentRecord.setBill(
                    String.valueOf(
                            totalBill
                    )
            );

            currentRecord.setAllocatedSlot(
                    "--"
            );


            // Save parking records
            ParkingRecordManager.saveRecords();


            // =========================
            // RECEIPT TEXT
            // =========================

            String receiptText =
                    "Park.Me Receipt\n\n"
                            + "Vehicle Type: "
                            + vehicleType
                            + "\n"
                            + "Registration: "
                            + registration
                            + "\n"
                            + "In-Time: "
                            + inTime
                            + "\n"
                            + "Out-Time: "
                            + outTimeText
                            + "\n"
                            + "Allocated Slot: --"
                            + "\n"
                            + "Total Bill: "
                            + totalBill;


            // Real printer function.
            // Uncomment the next line to enable actual printing.
            // printReceiptToPrinter(receiptText);


            // =========================
            // SHOW FINAL DATA
            // =========================

            vehicleTypeValue.setText(
                    vehicleType
            );

            registrationValue.setText(
                    registration
            );

            inTimeValue.setText(
                    inTime
            );

            outTimeValue.setText(
                    outTimeText
            );

            slotValue.setText(
                    "--"
            );

            billValue.setText(
                    String.valueOf(
                            totalBill
                    )
            );

            // Prevent printing again
            printButton.setEnabled(false);

            currentRecord = null;

            previewOutTime = null;

            previewOutTimeText = null;

            previewBill = 0;


            // =========================
            // SUCCESS MESSAGE
            // =========================

            JOptionPane.showMessageDialog(
                    frame,
                    "Receipt Printed!",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE
            );
        });


        // =========================
        // ENTER = CHECKOUT
        // =========================

        frame.getRootPane()
                .setDefaultButton(
                        checkoutButton
                );


        // =========================
        // ADD PANELS
        // =========================

        mainPanel.add(
                checkoutPanel,
                BorderLayout.NORTH
        );

        mainPanel.add(
                receiptPanel,
                BorderLayout.CENTER
        );

        mainPanel.add(
                printPanel,
                BorderLayout.SOUTH
        );


        // =========================
        // SHOW WINDOW
        // =========================

        frame.add(mainPanel);

        frame.setVisible(true);
    }


    // =========================
    // REAL PRINTER FUNCTION
    // =========================

    private void printReceiptToPrinter(
            String receiptText) {

        PrinterJob printerJob =
                PrinterJob.getPrinterJob();


        printerJob.setPrintable(
                new Printable() {

                    @Override
                    public int print(
                            Graphics graphics,
                            PageFormat pageFormat,
                            int pageIndex) {

                        if (pageIndex > 0) {

                            return Printable.NO_SUCH_PAGE;
                        }


                        Graphics2D graphics2D =
                                (Graphics2D) graphics;


                        graphics2D.translate(
                                pageFormat
                                        .getImageableX(),
                                pageFormat
                                        .getImageableY()
                        );


                        int y = 20;


                        String[] lines =
                                receiptText.split(
                                        "\\n"
                                );


                        for (String line :
                                lines) {

                            graphics2D.drawString(
                                    line,
                                    0,
                                    y
                            );

                            y += 20;
                        }


                        return Printable.PAGE_EXISTS;
                    }
                }
        );


        if (printerJob.printDialog()) {

            try {

                printerJob.print();

            } catch (Exception e) {

                JOptionPane.showMessageDialog(
                        frame,
                        "Printing failed!",
                        "Printer Error",
                        JOptionPane.ERROR_MESSAGE
                );
            }
        }
    }
}
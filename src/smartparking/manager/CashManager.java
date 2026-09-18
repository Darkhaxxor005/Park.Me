package smartparking.manager;

import smartparking.model.ParkingFee;

import java.util.ArrayList;

public class CashManager {

    private static ArrayList<ParkingFee> fees =
            new ArrayList<>();


    static {
        loadFees();
    }


    // =========================
    // LOAD
    // =========================

    private static void loadFees() {

        ArrayList<String> lines =
                DataStorage.loadFees();

        // First run
        if (lines.isEmpty()) {

            createDefaultFees();

            saveFees();

            return;
        }


        for (String line : lines) {

            String[] parts =
                    line.split("\\|");

            if (parts.length != 3) {
                continue;
            }

            String vehicleType =
                    parts[0];

            if (!isWholeNumber(parts[1]) ||
                    !isWholeNumber(parts[2])) {

                continue;
            }

            int basicFee =
                    Integer.parseInt(parts[1]);

            int feePerHour =
                    Integer.parseInt(parts[2]);


            fees.add(
                    new ParkingFee(
                            vehicleType,
                            basicFee,
                            feePerHour
                    )
            );
        }


        // If the file was invalid or incomplete
        if (fees.size() != 4) {

            fees.clear();

            createDefaultFees();

            saveFees();
        }
    }


    // =========================
    // DEFAULT VALUES
    // =========================

    private static void createDefaultFees() {

        fees.add(
                new ParkingFee(
                        "Car",
                        10,
                        15
                )
        );

        fees.add(
                new ParkingFee(
                        "Truck",
                        20,
                        30
                )
        );

        fees.add(
                new ParkingFee(
                        "Bus",
                        25,
                        20
                )
        );

        fees.add(
                new ParkingFee(
                        "Bike",
                        5,
                        10
                )
        );
    }


    // =========================
    // SAVE
    // =========================

    private static void saveFees() {

        ArrayList<String> lines =
                new ArrayList<>();

        for (ParkingFee fee : fees) {

            lines.add(
                    fee.getVehicleType()
                            + "|"
                            + fee.getBasicFee()
                            + "|"
                            + fee.getFeePerHour()
            );
        }

        DataStorage.saveFees(lines);
    }


    // =========================
    // GET ALL
    // =========================

    public static ArrayList<ParkingFee>
    getFees() {

        return fees;
    }


    // =========================
    // GET BY TYPE
    // =========================

    public static ParkingFee
    getFee(String vehicleType) {

        for (ParkingFee fee : fees) {

            if (fee.getVehicleType()
                    .equals(vehicleType)) {

                return fee;
            }
        }

        return null;
    }


    // =========================
    // UPDATE
    // =========================

    public static void updateFee(
            String vehicleType,
            int basicFee,
            int feePerHour) {

        ParkingFee fee =
                getFee(vehicleType);

        if (fee != null) {

            fee.setBasicFee(basicFee);
            fee.setFeePerHour(feePerHour);

            saveFees();
        }
    }


    // =========================
    // WHOLE NUMBER CHECK
    // =========================

    public static boolean isWholeNumber(
            String value) {

        if (value.isEmpty()) {
            return false;
        }

        for (int i = 0;
             i < value.length();
             i++) {

            char ch =
                    value.charAt(i);

            if (ch < '0' || ch > '9') {
                return false;
            }
        }

        return true;
    }
}
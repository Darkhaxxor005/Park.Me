package smartparking.manager;

import smartparking.model.ParkingRecord;

import java.util.ArrayList;

public class ParkingRecordManager {

    // All parking records
    private static ArrayList<ParkingRecord> records =
            DataStorage.loadParkingRecords();


    // =========================
    // GET ALL RECORDS
    // =========================

    public static ArrayList<ParkingRecord> getRecords() {

        return records;
    }


    // =========================
    // ADD RECORD
    // =========================

    public static void addRecord(
            ParkingRecord record) {

        records.add(record);

        DataStorage.saveParkingRecords(records);
    }


    // =========================
    // FIND RECORD BY REGISTRATION
    // =========================

    public static ParkingRecord findByRegistration(
            String registration) {

        for (ParkingRecord record : records) {

            if (record.getRegistration()
                    .equalsIgnoreCase(registration)) {

                return record;
            }
        }

        return null;
    }


    // =========================
    // FIND CURRENTLY PARKED
    // =========================

    public static ParkingRecord findActiveByRegistration(
            String registration) {

        for (ParkingRecord record : records) {

            if (record.getRegistration()
                    .equalsIgnoreCase(registration)
                    &&
                    record.getOutTime()
                            .equals("--")) {

                return record;
            }
        }

        return null;
    }


    // =========================
    // CHECK IF ALREADY PARKED
    // =========================

    public static boolean isAlreadyParked(
            String registration) {

        ParkingRecord record =
                findActiveByRegistration(
                        registration
                );

        return record != null;
    }


    // =========================
    // SAVE CURRENT RECORDS
    // =========================

    public static void saveRecords() {

        DataStorage.saveParkingRecords(records);
    }
}
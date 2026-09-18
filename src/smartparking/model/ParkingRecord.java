package smartparking.model;

public class ParkingRecord {

    private String vehicleType;
    private String registration;
    private String inTime;
    private String outTime;
    private String allocatedSlot;
    private String bill;


    // Constructor
    public ParkingRecord(
            String vehicleType,
            String registration,
            String inTime,
            String outTime,
            String allocatedSlot,
            String bill) {

        this.vehicleType = vehicleType;
        this.registration = registration;
        this.inTime = inTime;
        this.outTime = outTime;
        this.allocatedSlot = allocatedSlot;
        this.bill = bill;
    }


    // Get vehicle type
    public String getVehicleType() {

        return vehicleType;
    }


    // Set vehicle type
    public void setVehicleType(
            String vehicleType) {

        this.vehicleType = vehicleType;
    }


    // Get registration
    public String getRegistration() {

        return registration;
    }


    // Set registration
    public void setRegistration(
            String registration) {

        this.registration = registration;
    }


    // Get in-time
    public String getInTime() {

        return inTime;
    }


    // Set in-time
    public void setInTime(
            String inTime) {

        this.inTime = inTime;
    }


    // Get out-time
    public String getOutTime() {

        return outTime;
    }


    // Set out-time
    public void setOutTime(
            String outTime) {

        this.outTime = outTime;
    }


    // Get allocated slot
    public String getAllocatedSlot() {

        return allocatedSlot;
    }


    // Set allocated slot
    public void setAllocatedSlot(
            String allocatedSlot) {

        this.allocatedSlot = allocatedSlot;
    }


    // Get bill
    public String getBill() {

        return bill;
    }


    // Set bill
    public void setBill(
            String bill) {

        this.bill = bill;
    }
}
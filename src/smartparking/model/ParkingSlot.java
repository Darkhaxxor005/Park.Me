package smartparking.model;

public class ParkingSlot {

    private String slotId;
    private String vehicleRegistration;
    private boolean occupied;

    public ParkingSlot(String slotId) {

        this.slotId = slotId;
        this.vehicleRegistration = "--";
        this.occupied = false;
    }

    public String getSlotId() {
        return slotId;
    }

    public String getVehicleRegistration() {
        return vehicleRegistration;
    }

    public boolean isOccupied() {
        return occupied;
    }

    public void occupy(String registration) {

        vehicleRegistration = registration;
        occupied = true;
    }

    public void release() {

        vehicleRegistration = "--";
        occupied = false;
    }

    public void setVehicleRegistration(
            String vehicleRegistration) {

        this.vehicleRegistration =
                vehicleRegistration;
    }

    public void setOccupied(boolean occupied) {

        this.occupied = occupied;
    }
}
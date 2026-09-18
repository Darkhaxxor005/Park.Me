package smartparking.model;

public class ParkingFee {

    private String vehicleType;
    private int basicFee;
    private int feePerHour;

    public ParkingFee(
            String vehicleType,
            int basicFee,
            int feePerHour) {

        this.vehicleType = vehicleType;
        this.basicFee = basicFee;
        this.feePerHour = feePerHour;
    }

    public String getVehicleType() {
        return vehicleType;
    }

    public int getBasicFee() {
        return basicFee;
    }

    public int getFeePerHour() {
        return feePerHour;
    }

    public void setBasicFee(int basicFee) {
        this.basicFee = basicFee;
    }

    public void setFeePerHour(int feePerHour) {
        this.feePerHour = feePerHour;
    }
}
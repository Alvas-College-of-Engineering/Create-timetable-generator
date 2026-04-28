package model;

/**
 * Represents a single time period in the school day.
 * Each slot has a number, start time, and end time.
 */
public class TimeSlot {

    private int slotNumber;
    private String startTime;
    private String endTime;

    // Default constructor
    public TimeSlot() {
        this.slotNumber = 1;
        this.startTime = "09:00";
        this.endTime = "10:00";
    }

    // Parameterized constructor
    public TimeSlot(int slotNumber, String startTime, String endTime) {
        if (slotNumber <= 0) {
            throw new IllegalArgumentException("Slot number must be positive.");
        }
        this.slotNumber = slotNumber;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    // Getters
    public int getSlotNumber() {
        return slotNumber;
    }

    public String getStartTime() {
        return startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    // Setters
    public void setSlotNumber(int slotNumber) {
        this.slotNumber = slotNumber;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    /**
     * Returns a formatted label for this time slot, e.g. "09:00-10:00"
     */
    public String display() {
        return startTime + "-" + endTime;
    }

    @Override
    public String toString() {
        return "Slot " + slotNumber + " (" + display() + ")";
    }
}

package model;

/**
 * Represents a classroom or lab in the institution.
 * Used to track room availability and avoid double-booking.
 */
public class ClassRoom {

    private String roomNumber;
    private int capacity;
    private boolean isLab; // true if it's a computer/science lab

    // Default constructor
    public ClassRoom() {
        this.roomNumber = "R101";
        this.capacity = 40;
        this.isLab = false;
    }

    // Parameterized constructor
    public ClassRoom(String roomNumber, int capacity, boolean isLab) {
        this.roomNumber = roomNumber;
        this.capacity = capacity;
        this.isLab = isLab;
    }

    // Getters
    public String getRoomNumber() {
        return roomNumber;
    }

    public int getCapacity() {
        return capacity;
    }

    public boolean isLab() {
        return isLab;
    }

    // Setters
    public void setRoomNumber(String roomNumber) {
        this.roomNumber = roomNumber;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public void setLab(boolean lab) {
        isLab = lab;
    }

    @Override
    public String toString() {
        return roomNumber + (isLab ? " [Lab]" : " [Room]");
    }
}

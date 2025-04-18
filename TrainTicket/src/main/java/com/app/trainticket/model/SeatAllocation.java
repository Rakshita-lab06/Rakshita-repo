package com.app.trainticket.model;

public class SeatAllocation {
    private int seatNumber;
    private String section;

    public SeatAllocation(int seatNumber, String section) {
        this.seatNumber = seatNumber;
        this.section = section;
    }

    public int getSeatNumber() {
        return seatNumber;
    }

    public void setSeatNumber(int seatNumber) {
        this.seatNumber = seatNumber;
    }

    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section;
    }
}

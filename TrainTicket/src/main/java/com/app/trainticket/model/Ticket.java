package com.app.trainticket.model;

public class Ticket {
    private String from;
    private String to;
    private User user;
    private double pricePaid;
    private SeatAllocation seat;

    public Ticket(String from, String to, User user, double pricePaid, SeatAllocation seat) {
        this.from = from;
        this.to = to;
        this.user = user;
        this.pricePaid = pricePaid;
        this.seat = seat;
    }

    public String getFrom() {
        return from;
    }

    public String getTo() {
        return to;
    }

    public User getUser() {
        return user;
    }

    public double getPricePaid() {
        return pricePaid;
    }

    public SeatAllocation getSeat() {
        return seat;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public void setSeat(SeatAllocation seat) {
        this.seat = seat;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setPricePaid(double pricePaid) {
        this.pricePaid = pricePaid;
    }

    public void setTo(String to) {
        this.to = to;
    }
}

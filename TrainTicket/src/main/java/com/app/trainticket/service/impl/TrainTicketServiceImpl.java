package com.app.trainticket.service.impl;

import com.app.trainticket.model.SeatAllocation;
import com.app.trainticket.model.Ticket;
import com.app.trainticket.model.User;
import com.app.trainticket.service.TrainTicketService;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TrainTicketServiceImpl implements TrainTicketService {
    @Override
    public Ticket purchaseTicket(User user, int maxSeatsPerPerson, Map<String, Ticket> ticketStore, Map<String, List<SeatAllocation>> seatMap) {
        String section = seatMap.get("A").size() < maxSeatsPerPerson ? "A" : "B";
        int seatNumber = seatMap.get(section).size() + 1;

        SeatAllocation seat = new SeatAllocation(seatNumber, section);
        Ticket ticket = new Ticket("London", "France", user, 20.0, seat);
        ticketStore.put(user.getEmail(), ticket);
        seatMap.get(section).add(seat);
        return ticket;
    }

    @Override
    public Ticket getTicket(String email, Map<String, Ticket> ticketStore) {
        return ticketStore.get(email);
    }

    @Override
    public List<Map<String, Object>> getUsersBySection(String section, Map<String, Ticket> ticketStore) {
        List<Map<String, Object>> result = new ArrayList<>();
        for(Ticket ticket : ticketStore.values()){
            if(ticket.getSeat().getSection().equals(section)){
                Map<String, Object> entry = new HashMap<>();
                entry.put("user", ticket.getUser());
                entry.put("seat", ticket.getSeat());
                result.add(entry);
            }
        }
        return result;
    }

    @Override
    public Ticket modifySeat(String email, String newSection, Map<String, Ticket> ticketStore, Map<String, List<SeatAllocation>> seatMap) {
        Ticket ticket = ticketStore.get(email);
        List<SeatAllocation> newSectionSeats = seatMap.get(newSection);
        seatMap.get(ticket.getSeat().getSection()).remove(ticket.getSeat());
        SeatAllocation newSeat = new SeatAllocation(newSectionSeats.size()+1, newSection);
        newSectionSeats.add(newSeat);
        ticket.setSeat(newSeat);
        return ticket;
    }

    @Override
    public String removeUser(String email, Map<String, Ticket> ticketStore, Map<String, List<SeatAllocation>> seatMap) {
        Ticket ticket = ticketStore.get(email);
        if(ticket != null){
            seatMap.get(ticket.getSeat().getSection()).remove(ticket.getSeat());
            ticketStore.remove(email);
            return "User removed from the ticket";
        }
        return "User not found";
    }
}

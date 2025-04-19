package com.app.trainticket.service;

import com.app.trainticket.model.SeatAllocation;
import com.app.trainticket.model.Ticket;
import com.app.trainticket.model.User;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

public interface TrainTicketService {

    public Ticket purchaseTicket(User user, int maxSeatsPerPerson, Map<String, Ticket> ticketStore, Map<String, List<SeatAllocation>> seatMap);
    public Ticket getTicket(String email, Map<String, Ticket> ticketStore);
    public List<Map<String, Object>> getUsersBySection(String section, Map<String, Ticket> ticketStore);
    public Ticket modifySeat(String email, String newSection, Map<String, Ticket> ticketStore, Map<String, List<SeatAllocation>> seatMap);
    public String removeUser(String email, Map<String, Ticket> ticketStore, Map<String, List<SeatAllocation>> seatMap);
}

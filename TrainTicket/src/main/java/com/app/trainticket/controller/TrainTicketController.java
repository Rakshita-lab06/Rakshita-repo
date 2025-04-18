package com.app.trainticket.controller;

import com.app.trainticket.model.SeatAllocation;
import com.app.trainticket.model.Ticket;
import com.app.trainticket.model.User;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/tickets")
public class TrainTicketController {
    private final Map<String, Ticket> ticketStore = new HashMap<String, Ticket>();
    private final Map<String, List<SeatAllocation>> seatMap = new HashMap<String, List<SeatAllocation>>(){{
        put("A", new ArrayList<>());
        put("B", new ArrayList<>());
    }};
    private final int maxSeatsPerPerson = 50;

    @PostMapping("/purchase")
    public Ticket purchaseTicket(@RequestBody User user){
        String section = seatMap.get("A").size() < maxSeatsPerPerson ? "A" : "B";
        int seatNumber = seatMap.get(section).size() + 1;

        SeatAllocation seat = new SeatAllocation(seatNumber, section);
        Ticket ticket = new Ticket("London", "France", user, 20.0, seat);
        ticketStore.put(user.getEmail(), ticket);
        seatMap.get(section).add(seat);
        return ticket;
    }

    @GetMapping("/{email}")
    public Ticket getTicket(@PathVariable String email){
        return ticketStore.get(email);
    }

    @GetMapping("/section/{section}")
    public List<Map<String, Object>> getUsersBySection(@PathVariable String section){
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

    @PutMapping("/{email}/seat")
    public Ticket modifySeat(@PathVariable String email, @RequestParam String newSection){
        Ticket ticket = ticketStore.get(email);
        if(ticket == null){
            return null;
        }
        List<SeatAllocation> newSectionSeats = seatMap.get(newSection);
        if(newSectionSeats == null || newSectionSeats.size() >= maxSeatsPerPerson){
            throw new RuntimeException("No available seats in section " + newSection);
        }
        seatMap.get(ticket.getSeat().getSection()).remove(ticket.getSeat());
        SeatAllocation newSeat = new SeatAllocation(newSectionSeats.size()+1, newSection);
        newSectionSeats.add(newSeat);
        ticket.setSeat(newSeat);
        return ticket;
    }

    @DeleteMapping("/{email}")
    public String removeUser(@PathVariable String email){
        Ticket ticket = ticketStore.get(email);
        if(ticket != null){
            seatMap.get(ticket.getSeat().getSection()).remove(ticket.getSeat());
            ticketStore.remove(email);
            return "User removed from the ticket";
        }
        return "User not found";
    }
}

package com.app.trainticket.controller;

import ch.qos.logback.core.net.SyslogOutputStream;
import com.app.trainticket.model.SeatAllocation;
import com.app.trainticket.model.Ticket;
import com.app.trainticket.model.User;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<?> purchaseTicket(@RequestBody User user){
        if(ticketStore.get(user.getEmail()) != null){
            return ResponseEntity.badRequest().body("Ticket already exists");
        }
        String section = seatMap.get("A").size() < maxSeatsPerPerson ? "A" : "B";
        int seatNumber = seatMap.get(section).size() + 1;

        SeatAllocation seat = new SeatAllocation(seatNumber, section);
        Ticket ticket = new Ticket("London", "France", user, 20.0, seat);
        ticketStore.put(user.getEmail(), ticket);
        seatMap.get(section).add(seat);
        return ResponseEntity.ok(ticket);
    }

    @GetMapping("/{email}")
    public ResponseEntity<?> getTicket(@PathVariable String email){
        if(ticketStore.get(email) == null){
            return ResponseEntity.badRequest().body("Ticket not found");
        }
        return ResponseEntity.ok(ticketStore.get(email));
    }

    @GetMapping("/section/{section}")
    public ResponseEntity<?> getUsersBySection(@PathVariable String section){
        List<Map<String, Object>> result = new ArrayList<>();
        System.out.println(seatMap.get(section).isEmpty());
        if(seatMap.get(section).isEmpty()){
            return ResponseEntity.badRequest().body("Section "+section+" does not contain any tickets");
        }
        for(Ticket ticket : ticketStore.values()){
            if(ticket.getSeat().getSection().equals(section)){
                Map<String, Object> entry = new HashMap<>();
                entry.put("user", ticket.getUser());
                entry.put("seat", ticket.getSeat());
                result.add(entry);
            }
        }
        return ResponseEntity.ok(result);
    }

    @PutMapping("/{email}/seat")
    public ResponseEntity<?> modifySeat(@PathVariable String email, @RequestParam String newSection){
        Ticket ticket = ticketStore.get(email);
        if(ticket == null){
            return ResponseEntity.badRequest().body("Ticket not found");
        }
        if(ticket.getSeat().getSection().equals(newSection)){
            return ResponseEntity.badRequest().body("User already exists in section "+newSection);
        }
        List<SeatAllocation> newSectionSeats = seatMap.get(newSection);
        if(newSectionSeats == null || newSectionSeats.size() >= maxSeatsPerPerson){
            return ResponseEntity.badRequest().body("No available seats in section " + newSection);
        }
        seatMap.get(ticket.getSeat().getSection()).remove(ticket.getSeat());
        SeatAllocation newSeat = new SeatAllocation(newSectionSeats.size()+1, newSection);
        newSectionSeats.add(newSeat);
        ticket.setSeat(newSeat);
        return ResponseEntity.ok().body(ticket);
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

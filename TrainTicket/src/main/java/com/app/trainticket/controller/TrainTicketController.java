package com.app.trainticket.controller;

import ch.qos.logback.core.net.SyslogOutputStream;
import com.app.trainticket.model.SeatAllocation;
import com.app.trainticket.model.Ticket;
import com.app.trainticket.model.User;
import com.app.trainticket.service.TrainTicketService;
import com.app.trainticket.service.impl.TrainTicketServiceImpl;
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
        TrainTicketService ticketService = new TrainTicketServiceImpl();
        return ResponseEntity.ok(ticketService.purchaseTicket(user, maxSeatsPerPerson, ticketStore, seatMap));
    }

    @GetMapping("/{email}")
    public ResponseEntity<?> getTicket(@PathVariable String email){
        if(ticketStore.get(email) == null){
            return ResponseEntity.badRequest().body("Ticket not found");
        }
        TrainTicketService ticketService = new TrainTicketServiceImpl();
        return ResponseEntity.ok(ticketService.getTicket(email, ticketStore));
    }

    @GetMapping("/section/{section}")
    public ResponseEntity<?> getUsersBySection(@PathVariable String section){
        if(seatMap.get(section).isEmpty()){
            return ResponseEntity.badRequest().body("Section "+section+" does not contain any tickets");
        }
        TrainTicketService ticketService = new TrainTicketServiceImpl();
        return ResponseEntity.ok(ticketService.getUsersBySection(section, ticketStore));
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
        TrainTicketService ticketService = new TrainTicketServiceImpl();
        return ResponseEntity.ok().body(ticketService.modifySeat(email, newSection, ticketStore, seatMap));
    }

    @DeleteMapping("/{email}")
    public String removeUser(@PathVariable String email){
        TrainTicketService ticketService = new TrainTicketServiceImpl();
        return ticketService.removeUser(email, ticketStore, seatMap);
    }
}

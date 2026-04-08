package com.ticket.backend.controller;

import com.ticket.backend.dto.MessageRequest;
import com.ticket.backend.dto.TicketRequest;
import com.ticket.backend.model.Status;
import com.ticket.backend.model.Ticket;
import com.ticket.backend.service.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tickets")
public class TicketController {

    @Autowired
    private TicketService ticketService;

    // Create ticket - USER and ADMIN can create tickets
    @PostMapping
    public ResponseEntity<Ticket> createTicket(@RequestBody TicketRequest request, Authentication authentication) {
        String email = authentication.getName();
        return ResponseEntity.ok(ticketService.createTicket(email, request));
    }

    // Get all tickets - Only ADMIN
    @GetMapping("/all")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Ticket>> getAllTickets() {
        return ResponseEntity.ok(ticketService.getAllTickets());
    }

    // Get my tickets - USER & ADMIN can view their own
    @GetMapping("/my")
    public ResponseEntity<List<Ticket>> getMyTickets(Authentication authentication) {
        String email = authentication.getName();
        return ResponseEntity.ok(ticketService.getUserTickets(email));
    }
    
    // Get ticket by ID
    @GetMapping("/{id}")
    public ResponseEntity<Ticket> getTicketById(@PathVariable String id) {
        return ResponseEntity.ok(ticketService.getTicketById(id));
    }

    // Reply to ticket
    @PostMapping("/{id}/reply")
    public ResponseEntity<Ticket> replyToTicket(@PathVariable String id, @RequestBody MessageRequest request, Authentication authentication) {
        String email = authentication.getName();
        return ResponseEntity.ok(ticketService.addMessage(id, email, request));
    }

    // Change status - Only ADMIN
    @PatchMapping("/{id}/status")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Ticket> updateTicketStatus(@PathVariable String id, @RequestParam Status status) {
        return ResponseEntity.ok(ticketService.updateTicketStatus(id, status));
    }
}

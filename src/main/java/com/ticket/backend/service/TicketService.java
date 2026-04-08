package com.ticket.backend.service;

import com.ticket.backend.dto.MessageRequest;
import com.ticket.backend.dto.TicketRequest;
import com.ticket.backend.model.*;
import com.ticket.backend.repository.TicketRepository;
import com.ticket.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class TicketService {

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private UserRepository userRepository;

    public Ticket createTicket(String email, TicketRequest request) {
        User user = userRepository.findByEmail(email).orElseThrow();
        Ticket ticket = new Ticket();
        ticket.setUserId(user.getId());
        ticket.setTitle(request.getTitle());
        ticket.setIssueType(request.getIssueType());
        ticket.setPriority(request.getPriority());
        ticket.setStatus(Status.OPEN);
        return ticketRepository.save(ticket);
    }

    public List<Ticket> getUserTickets(String email) {
        User user = userRepository.findByEmail(email).orElseThrow();
        return ticketRepository.findByUserId(user.getId());
    }

    public List<Ticket> getAllTickets() {
        return ticketRepository.findAll();
    }
    
    public Ticket getTicketById(String id) {
        return ticketRepository.findById(id).orElseThrow(() -> new RuntimeException("Ticket not found"));
    }

    public Ticket addMessage(String ticketId, String email, MessageRequest request) {
        Ticket ticket = getTicketById(ticketId);
        
        if (ticket.getStatus() == Status.CLOSED) {
            throw new RuntimeException("Cannot reply to a closed ticket");
        }

        User user = userRepository.findByEmail(email).orElseThrow();
        
        Message message = new Message();
        message.setMessage(request.getMessage());
        message.setSenderRole(user.getRole());
        message.setTimestamp(LocalDateTime.now());
        
        ticket.getMessages().add(message);
        
        // Update ticket status to IN_PROGRESS automatically if it's the first admin reply
        if (user.getRole() == Role.ADMIN && ticket.getStatus() == Status.OPEN) {
            ticket.setStatus(Status.IN_PROGRESS);
        }
        
        return ticketRepository.save(ticket);
    }

    public Ticket updateTicketStatus(String ticketId, Status status) {
        Ticket ticket = getTicketById(ticketId);
        ticket.setStatus(status);
        return ticketRepository.save(ticket);
    }
}

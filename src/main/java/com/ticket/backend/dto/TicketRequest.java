package com.ticket.backend.dto;

import lombok.Data;
import com.ticket.backend.model.IssueType;
import com.ticket.backend.model.Priority;

@Data
public class TicketRequest {
    private String title;
    private IssueType issueType;
    private Priority priority;
}

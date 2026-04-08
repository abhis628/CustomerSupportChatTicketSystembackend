package com.ticket.backend.dto;

import lombok.Data;
import com.ticket.backend.model.Role;

@Data
public class RegisterRequest {
    private String name;
    private String email;
    private String password;
    private Role role;
}

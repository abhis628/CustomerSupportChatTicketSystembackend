package com.ticket.backend.dto;

import lombok.Data;
import lombok.AllArgsConstructor;
import com.ticket.backend.model.Role;

@Data
@AllArgsConstructor
public class AuthResponse {
    private String token;
    private String email;
    private Role role;
    private String userId;
    private String name;
}

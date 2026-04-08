package com.ticket.backend.service;

import com.ticket.backend.config.JwtUtils;
import com.ticket.backend.dto.AuthRequest;
import com.ticket.backend.dto.AuthResponse;
import com.ticket.backend.dto.RegisterRequest;
import com.ticket.backend.model.Role;
import com.ticket.backend.model.User;
import com.ticket.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtils jwtUtils;

    public AuthResponse login(AuthRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));

        String jwt = jwtUtils.generateJwtToken(request.getEmail());

        User user = userRepository.findByEmail(request.getEmail()).orElseThrow();

        return new AuthResponse(jwt, user.getEmail(), user.getRole(), user.getId(), user.getName());
    }

    public void register(RegisterRequest request) {
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new RuntimeException("Error: Email is already in use!");
        }

        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(request.getRole() == null ? Role.USER : request.getRole());

        userRepository.save(user);
    }
}

package com.ws101.deleon.ecommerceapi.controller;

import com.ws101.deleon.ecommerceapi.model.User;
import com.ws101.deleon.ecommerceapi.repository.UserRepository;
import com.ws101.deleon.ecommerceapi.util.JwtUtil;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

/**
 * Controller for authentication endpoints.
 */
@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public AuthController(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@Valid @RequestBody RegisterRequest request) {
        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            return ResponseEntity.badRequest().body("Username already exists");
        }

        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(request.getRole() != null ? request.getRole() : "USER");

        userRepository.save(user);

        return ResponseEntity.status(HttpStatus.CREATED).body("User registered successfully");
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest request) {
        var user = userRepository.findByUsername(request.getUsername());
        
        if (user.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password");
        }

        User userEntity = user.get();
        
        if (!passwordEncoder.matches(request.getPassword(), userEntity.getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password");
        }

        String token = jwtUtil.generateToken(userEntity.getUsername(), userEntity.getRole());
        
        LoginResponse response = new LoginResponse(
            token,
            userEntity.getUsername(),
            userEntity.getRole(),
            "Login successful"
        );

        return ResponseEntity.ok(response);
    }
}
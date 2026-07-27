package org.kamal.taskmanager.controllers;


import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import jakarta.validation.Valid;
import org.kamal.taskmanager.dto.request.LoginRequest;
import org.kamal.taskmanager.dto.response.LoginResponse;
import org.kamal.taskmanager.models.User;
import org.kamal.taskmanager.security.JwtService;
import org.kamal.taskmanager.services.UserService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;
    private final JwtService jwtService;

    public AuthController(UserService userService, JwtService jwtService) {
        this.userService = userService;
        this.jwtService = jwtService;
    }

    @PostMapping("/login")
    @SecurityRequirements()
    public LoginResponse login(@Valid @RequestBody LoginRequest request) {
        User user = userService.login(request.getEmail(), request.getPassword());
        String token = jwtService.generateToken(user.getEmail());
        return new LoginResponse(token);
    }
}

package org.kamal.taskmanager.controllers;


import org.kamal.taskmanager.dto.request.RegisterRequest;
import org.kamal.taskmanager.dto.response.UserResponse;
import org.kamal.taskmanager.models.User;
import org.kamal.taskmanager.services.UserService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public UserResponse register(@RequestBody RegisterRequest request) {
        User user = userService.registerUser(request.getName(), request.getEmail(), request.getPassword());
        return new UserResponse(user.getId(), user.getName(), user.getEmail());
    }

    @GetMapping("/{id}")
    public UserResponse getUser(@PathVariable Long id) {
        User user = userService.getUserById(id);
        return new UserResponse(user.getId(), user.getName(), user.getEmail());
    }

}

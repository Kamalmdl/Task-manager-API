package org.kamal.taskmanager.services;


import org.kamal.taskmanager.exceptions.InvalidCredentialsException;
import org.kamal.taskmanager.exceptions.ResourceAlreadyExistsException;
import org.kamal.taskmanager.exceptions.ResourceNotFoundException;
import org.kamal.taskmanager.models.User;
import org.kamal.taskmanager.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User registerUser(String name, String email, String password) {
        if(userRepository.findByEmail(email).isPresent()) {
            throw new ResourceAlreadyExistsException("Email already exists");
        }
        String hashedPassword = passwordEncoder.encode(password);
        User user = new User();
        user.setName(name);
        user.setEmail(email);
        user.setPassword(hashedPassword);
        return userRepository.save(user);
    }

    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User with email " + email + " does not exist"));
    }

    public User login(String email, String password) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new InvalidCredentialsException("Invalid credentials"));
        if(passwordEncoder.matches(password, user.getPassword())) {
            return user;
        } else {
            throw new InvalidCredentialsException("Invalid credentials");
        }
    }
}

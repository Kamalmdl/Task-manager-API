package org.kamal.taskmanager.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.kamal.taskmanager.exceptions.ResourceAlreadyExistsException;
import org.kamal.taskmanager.models.User;
import org.kamal.taskmanager.repository.UserRepository;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    @Test
    void registerUser_shouldThrowException_whenEmailAlreadyExists() {
        String email = "anya@example.com";
        User existingUser = new User();
        existingUser.setEmail(email);
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(existingUser));

        assertThrows(ResourceAlreadyExistsException.class, () -> {
            userService.registerUser("Anya", email, "qwerty123");
        });
    }

    @Test
    void registerUser_shouldSaveUser_whenEmailIsNew() {
        String email = "123@example.com";
        String password = "qwerty123";
        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());
        when(passwordEncoder.encode(password)).thenReturn("hashedPassword123");
        when(userRepository.save(any(User.class))).thenAnswer(i -> i.getArgument(0));
        User result = userService.registerUser("Ilya", email, password);

        assertEquals(email, result.getEmail());
        assertEquals("hashedPassword123", result.getPassword());
    }
}
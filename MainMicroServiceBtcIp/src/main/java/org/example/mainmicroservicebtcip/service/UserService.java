package org.example.mainmicroservicebtcip.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.mainmicroservicebtcip.model.entity.User;
import org.example.mainmicroservicebtcip.model.enums.Role;
import org.example.mainmicroservicebtcip.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    // Helper method to create and save a user
    private User createUserFromExternalUser(User externalUser, String username, String password, Role role) {
        log.debug("Creating a new user with username: {}", username);

        User user = new User();
        user.setIpAddress(externalUser.getIpAddress());
        user.setRequestTime(externalUser.getRequestTime());
        user.setUsername(username);

        // Encoding password before setting
        String encodedPassword = passwordEncoder.encode(password);
        user.setPassword(encodedPassword);

        // Setting user role (admin or user)
        user.setRole(role);

        log.info("User '{}' created with role: {}", username, role);

        return user;
    }

    // Save method to persist the user to the database
    public void save(User user) {
        log.debug("Saving user: {}", user.getUsername());
        userRepository.save(user);
        log.info("User '{}' saved successfully", user.getUsername());
    }

    // Create admin method
    public void createAdmin(User externalUser, String username, String password) {
        log.debug("Creating admin user: {}", username);
        User adminUser = createUserFromExternalUser(externalUser, username, password, Role.ADMIN);
        save(adminUser);
    }

    // Create regular user method
    public void createUser(User externalUser, String username, String password) {
        log.debug("Creating regular user: {}", username);
        User regularUser = createUserFromExternalUser(externalUser, username, password, Role.USER);
        save(regularUser);
    }
}
package org.example.mainmicroservicebtcip.controller;

import lombok.RequiredArgsConstructor;
import org.example.mainmicroservicebtcip.feign.BTCApiClientFeign;
import org.example.mainmicroservicebtcip.model.UserFromRequest;
import org.example.mainmicroservicebtcip.model.entity.User;
import org.example.mainmicroservicebtcip.service.UserService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/main")
@RequiredArgsConstructor
public class MainController {

    private final UserService userService;
    private final RestTemplate restTemplate;
    private final BTCApiClientFeign btcApiClientFeign;

    @Value("${external.service.url}")
    private String externalServiceUrl;

    // SLF4J Logger
    private static final Logger logger = LoggerFactory.getLogger(MainController.class);

    @GetMapping()
    public String getBtcPrice() {
        logger.info("Fetching BTC price...");
        return btcApiClientFeign.btc();
    }

    @PostMapping("/create/user")
    public ResponseEntity<String> createUser(@RequestBody UserFromRequest userFromRequest) {
        logger.info("Attempting to create user with username: {}", userFromRequest.getUsername());

        // Validate the input
        if (userFromRequest.getUsername().isBlank() || userFromRequest.getPassword().isBlank()) {
            logger.error("Validation failed for user: {}. Username or password is blank.", userFromRequest.getUsername());
            return ResponseEntity.badRequest().body("Username and password must not be blank");
        }

        // Fetch IP address and request time from external service
        User externalUser = getExternalUserData();
        if (externalUser == null) {
            logger.error("Failed to fetch external user data from: {}", externalServiceUrl);
            return ResponseEntity.status(500).body("Failed to retrieve external user data");
        }

        userService.createUser(externalUser, userFromRequest.getUsername(), userFromRequest.getPassword());
        logger.info("User created successfully: {}", userFromRequest.getUsername());
        return ResponseEntity.ok("User created successfully");
    }

    @PostMapping("/create/admin")
    public ResponseEntity<String> createAdmin(@RequestBody UserFromRequest createAdminRequest) {
        logger.info("Attempting to create admin with username: {}", createAdminRequest.getUsername());

        // Validate the input
        if (createAdminRequest.getUsername().isBlank() || createAdminRequest.getPassword().isBlank()) {
            logger.error("Validation failed for admin: {}. Username or password is blank.", createAdminRequest.getUsername());
            return ResponseEntity.badRequest().body("Username and password must not be blank");
        }

        // Fetch IP address and request time from external service
        User externalUser = getExternalUserData();
        if (externalUser == null) {
            logger.error("Failed to fetch external user data from: {}", externalServiceUrl);
            return ResponseEntity.status(500).body("Failed to retrieve external user data");
        }

        userService.createAdmin(externalUser, createAdminRequest.getUsername(), createAdminRequest.getPassword());
        logger.info("Admin created successfully: {}", createAdminRequest.getUsername());
        return ResponseEntity.ok("Admin created successfully");
    }

    // Helper method to fetch user data from external service
    private User getExternalUserData() {
        try {
            return restTemplate.getForObject(externalServiceUrl, User.class);
        } catch (Exception e) {
            logger.error("Error fetching external user data from: {}", externalServiceUrl, e);
            return null;
        }
    }
}
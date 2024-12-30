package org.example.mainmicroservicebtcip.security.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.mainmicroservicebtcip.model.entity.User;
import org.example.mainmicroservicebtcip.repository.UserRepository;
import org.example.mainmicroservicebtcip.security.model.CustomUserDetails;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Slf4j
@RequiredArgsConstructor
@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    private static final String PREFIX = "ROLE_";

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.debug("Attempting to load user by username: {}", username);

        // Retrieve user from repository
        User user = userRepository.findByUsername(username);

        // Check if user exists and log appropriate messages
        if (user == null) {
            log.error("User with username '{}' not found", username);
            throw new UsernameNotFoundException("User not found with username: " + username);
        }

        log.info("User '{}' found. Creating CustomUserDetails...", username);

        // Constructing authorities (roles)
        String role = PREFIX + user.getRole();
        GrantedAuthority authority = new SimpleGrantedAuthority(role);
        CustomUserDetails customUserDetails = new CustomUserDetails(
                user.getUsername(),
                user.getPassword(),
                Collections.singletonList(authority)
        );

        log.debug("CustomUserDetails created for user: {}", username);

        return customUserDetails;
    }
}
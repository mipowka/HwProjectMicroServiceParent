package org.example.mainmicroservicebtcip.security.model;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Slf4j
public class CustomUserDetails implements UserDetails {

    private final String username;
    private final String password;
    private final List<GrantedAuthority> authorities;

    public CustomUserDetails(String username, String password, List<GrantedAuthority> authorities) {
        log.debug("Creating CustomUserDetails for user: {}", username);

        // Initialize fields with validation or transformation if needed
        this.username = username;
        this.password = password;
        this.authorities = Collections.unmodifiableList(authorities); // To avoid modification later

        log.info("CustomUserDetails created for user: {}", username);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        log.debug("Getting authorities for user: {}", username);
        return authorities;
    }

    @Override
    public String getPassword() {
        log.debug("Getting password for user: {}", username);
        return password;
    }

    @Override
    public String getUsername() {
        log.debug("Getting username for user: {}", username);
        return username;
    }
}
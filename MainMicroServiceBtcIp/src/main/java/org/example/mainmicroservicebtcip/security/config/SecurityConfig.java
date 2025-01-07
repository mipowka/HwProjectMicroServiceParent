package org.example.mainmicroservicebtcip.security.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.mainmicroservicebtcip.security.jwt.JwtAuthenticationFilter;
import org.example.mainmicroservicebtcip.security.jwt.JwtTokenProvider;
import org.example.mainmicroservicebtcip.security.service.CustomUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@Slf4j
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtTokenProvider jwtTokenProvider;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        log.debug("Initializing SecurityFilterChain configuration...");

        // Disabling CSRF for APIs, logging the decision
        http.csrf(AbstractHttpConfigurer::disable);
        log.info("CSRF protection disabled for the application.");

        http.addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider), UsernamePasswordAuthenticationFilter.class);

        // Configuring authorization rules
        http.authorizeHttpRequests(authorizeRequests -> {
            authorizeRequests
                    .requestMatchers(HttpMethod.POST, "/main/create/user").hasRole("ADMIN")
                    .requestMatchers(HttpMethod.GET, "/main").hasRole("USER")
                    .requestMatchers(HttpMethod.POST,"/login").permitAll()
                    .requestMatchers(HttpMethod.POST,"/main/create/admin").anonymous()
                    .anyRequest().authenticated();

            log.info("Authorization rules defined: POST '/main/create/user' requires ADMIN role, GET '/main' requires USER role, POST '/main/create/admin' is anonymous.");
        });

//        // Enabling HTTP Basic Authentication
//        http.httpBasic(Customizer.withDefaults());
//        log.info("HTTP Basic Authentication enabled for the application.");

        // Returning the configured HttpSecurity
        log.debug("SecurityFilterChain configuration completed.");
        return http.build();
    }

    @Bean
    public AuthenticationProvider authenticationProvider(CustomUserDetailsService userDetailsService) {
        log.debug("Creating AuthenticationProvider...");

        // Creating DaoAuthenticationProvider with custom user details service and password encoder
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());

        log.info("AuthenticationProvider configured successfully with CustomUserDetailsService.");

        return authProvider;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        log.debug("Creating BCryptPasswordEncoder for password encoding...");
        return new BCryptPasswordEncoder();
    }
}
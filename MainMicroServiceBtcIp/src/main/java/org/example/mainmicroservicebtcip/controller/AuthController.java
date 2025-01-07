package org.example.mainmicroservicebtcip.controller;

import lombok.RequiredArgsConstructor;
import org.example.mainmicroservicebtcip.model.entity.User;
import org.example.mainmicroservicebtcip.repository.UserRepository;
import org.example.mainmicroservicebtcip.security.jwt.JwtTokenProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class); // Создаем логгер

    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/login")
    public String login(@RequestBody User user) {
        logger.info("Login attempt for user: {}", user.getUsername()); // Логируем попытку входа

        // Получаем пользователя из базы данных
        User storedUser = userRepository.findByUsername(user.getUsername());
        if (storedUser == null) {
            logger.warn("User with username {} not found", user.getUsername()); // Логируем предупреждение, если пользователь не найден
            throw new RuntimeException("User not found");
        }

        // Сравниваем пароли
        if (!passwordEncoder.matches(user.getPassword(), storedUser.getPassword())) {
            logger.warn("Invalid credentials for user: {}", user.getUsername()); // Логируем, если пароль неверный
            throw new RuntimeException("Invalid credentials");
        }

        logger.info("User {} successfully authenticated", user.getUsername()); // Логируем успешную аутентификацию


        // Если пароли совпали, генерируем и возвращаем JWT токен
        List<String> roles = List.of("ROLE_USER", "ROLE_ADMIN");
        String token = jwtTokenProvider.generateToken(user.getUsername(), roles);
        logger.info("JWT token generated for user: {}", user.getUsername()); // Логируем успешное создание токена

        return token;
    }
}
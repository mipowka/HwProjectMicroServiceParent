package org.example.saveip.service;

import lombok.RequiredArgsConstructor;
import org.example.saveip.model.User;
import org.example.saveip.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public User save(User user) {
        return userRepository.save(user);
    }

}

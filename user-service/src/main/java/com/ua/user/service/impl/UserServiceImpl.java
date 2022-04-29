package com.ua.user.service.impl;

import com.ua.user.model.User;
import com.ua.user.repository.UserRepository;
import com.ua.user.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public User create(final User user) {
        return userRepository.save(user);
    }

    @Override
    public Long findIdByAccessToken(final String accessToken) {
        return userRepository.findByAccessToken(accessToken)
                .map(User::getId)
                .orElseThrow(() -> new RuntimeException("User does not exist"));
    }
}

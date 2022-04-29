package com.ua.user.config;

import javax.annotation.PostConstruct;

import com.ua.user.model.User;
import com.ua.user.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class InitMockUsers {
    private final UserService userService;

    @PostConstruct
    public void init() {
        final User riddler = new User();
        riddler.setUsername("riddler");
        riddler.setEmail("riddler");
        riddler.setAccessToken("1337-riddler-1337");

        final User joker = new User();
        joker.setUsername("joker");
        joker.setEmail("joker");
        joker.setAccessToken("777-joker-777");

        userService.create(riddler);
        userService.create(joker);
    }
}

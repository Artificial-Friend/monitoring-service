package com.ua.user.controller;

import com.ua.user.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("users/")
@AllArgsConstructor
public class UserController {
    private UserService userService;

    @GetMapping("/get-id")
    public Long getId(@RequestHeader("accessToken") final String accessToken) {
        return userService.findIdByAccessToken(accessToken);
    }
}

package com.ua.user.service;

import com.ua.user.model.User;

public interface UserService {

    User create(User user);

    Long findIdByAccessToken(String accessToken);
}

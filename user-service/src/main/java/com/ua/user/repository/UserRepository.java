package com.ua.user.repository;

import java.util.Optional;

import com.ua.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    @Override
    Optional<User> findById(Long aLong);

    Optional<User> findByEmail(String email);

    Optional<User> findByAccessToken(String accessToken);
}

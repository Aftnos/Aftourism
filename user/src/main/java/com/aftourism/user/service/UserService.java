package com.aftourism.user.service;

import com.aftourism.user.entity.User;
import java.util.Optional;

/**
 * User domain service.
 */
public interface UserService {

    Long register(User user);

    Optional<User> findByUsername(String username);
}

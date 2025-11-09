package com.aftourism.user.service.impl;

import com.aftourism.common.exception.BusinessException;
import com.aftourism.user.entity.User;
import com.aftourism.user.mapper.UserMapper;
import com.aftourism.user.service.UserService;
import java.time.LocalDateTime;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * Implementation of {@link UserService}.
 */
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;

    @Override
    public Long register(User user) {
        Optional<User> existing = userMapper.findByUsername(user.getUsername());
        if (existing.isPresent()) {
            throw new BusinessException("Username already exists");
        }
        user.setIsDeleted(false);
        LocalDateTime now = LocalDateTime.now();
        user.setCreateTime(now);
        user.setUpdateTime(now);
        userMapper.insert(user);
        return user.getId();
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return userMapper.findByUsername(username);
    }
}

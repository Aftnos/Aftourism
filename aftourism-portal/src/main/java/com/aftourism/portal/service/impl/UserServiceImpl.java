package com.aftourism.portal.service.impl;

import com.aftourism.common.exception.BusinessException;
import com.aftourism.common.pojo.ResultCode;
import com.aftourism.portal.dto.UserLoginRequest;
import com.aftourism.portal.dto.UserRegisterRequest;
import com.aftourism.portal.mapper.UserMapper;
import com.aftourism.portal.pojo.User;
import com.aftourism.portal.service.UserService;
import com.aftourism.portal.vo.UserProfileVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.UUID;

/**
 * 用户服务实现。
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;

    @Override
    public Long register(UserRegisterRequest request) {
        User exists = userMapper.findByUsername(request.getUsername());
        if (exists != null) {
            throw new BusinessException(ResultCode.BUSINESS_ERROR.getCode(), "账号已存在");
        }
        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(request.getPassword());
        user.setNickname(request.getNickname());
        user.setEmail(request.getEmail());
        user.setStatus(1);
        userMapper.insert(user);
        log.info("新用户注册成功，ID:{}", user.getId());
        return user.getId();
    }

    @Override
    public UserProfileVO login(UserLoginRequest request) {
        User user = userMapper.findByUsername(request.getUsername());
        if (user == null || !StringUtils.hasText(request.getPassword()) || !request.getPassword().equals(user.getPassword())) {
            throw new BusinessException(ResultCode.UNAUTHORIZED.getCode(), "账号或密码错误");
        }
        String token = UUID.randomUUID().toString();
        return UserProfileVO.builder()
                .userId(user.getId())
                .username(user.getUsername())
                .nickname(user.getNickname())
                .email(user.getEmail())
                .avatar(user.getAvatar())
                .token(token)
                .build();
    }
}

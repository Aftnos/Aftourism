package com.aftourism.portal.service;

import com.aftourism.portal.dto.UserLoginRequest;
import com.aftourism.portal.dto.UserRegisterRequest;
import com.aftourism.portal.vo.UserProfileVO;

/**
 * 用户服务接口。
 */
public interface UserService {

    Long register(UserRegisterRequest request);

    UserProfileVO login(UserLoginRequest request);
}

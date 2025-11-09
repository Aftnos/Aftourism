package com.aftourism.admin.service.impl;

import com.aftourism.admin.dto.AdminLoginRequest;
import com.aftourism.admin.mapper.AdminAccountMapper;
import com.aftourism.admin.pojo.AdminAccount;
import com.aftourism.admin.service.AdminAuthService;
import com.aftourism.admin.vo.AdminLoginVO;
import com.aftourism.common.exception.BusinessException;
import com.aftourism.common.pojo.ResultCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.UUID;

/**
 * 管理员认证服务实现。
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AdminAuthServiceImpl implements AdminAuthService {

    private final AdminAccountMapper adminAccountMapper;

    @Override
    public AdminLoginVO login(AdminLoginRequest request) {
        AdminAccount account = adminAccountMapper.findByUsername(request.getUsername());
        if (account == null || account.getIsDeleted() != null && account.getIsDeleted() == 1) {
            throw new BusinessException(ResultCode.UNAUTHORIZED.getCode(), "管理员不存在或已被禁用");
        }
        if (!StringUtils.hasText(request.getPassword()) || !request.getPassword().equals(account.getPassword())) {
            throw new BusinessException(ResultCode.UNAUTHORIZED.getCode(), "账号或密码错误");
        }
        String token = UUID.randomUUID().toString();
        log.info("管理员:{} 登录成功，生成令牌:{}", account.getUsername(), token);
        return AdminLoginVO.builder()
                .adminId(account.getId())
                .username(account.getUsername())
                .realName(account.getRealName())
                .token(token)
                .build();
    }

    @Override
    public void logout(Long adminId) {
        log.info("管理员:{} 登出系统", adminId);
    }
}

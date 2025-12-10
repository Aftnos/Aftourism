package aftnos.aftourismserver.auth.service.impl;

import aftnos.aftourismserver.auth.dto.RegisterRequest;
import aftnos.aftourismserver.auth.mapper.UserMapper;
import aftnos.aftourismserver.auth.pojo.User;
import aftnos.aftourismserver.auth.service.PortalAuthService;
import aftnos.aftourismserver.common.exception.BusinessException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;

/**
 * 门户用户认证服务实现
 */
@Service
public class PortalAuthServiceImpl implements PortalAuthService {

    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    public PortalAuthServiceImpl(UserMapper userMapper, PasswordEncoder passwordEncoder) {
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void register(RegisterRequest request) {
        User existing = userMapper.findByUsername(request.getUsername());
        if (existing != null) {
            throw new BusinessException("用户名已存在");
        }

        // 记录当前时间，保持创建/更新时间一致
        LocalDateTime now = LocalDateTime.now();

        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        // 昵称为空时默认使用用户名，保证数据完整
        user.setNickname(StringUtils.hasText(request.getNickname()) ? request.getNickname().trim() : request.getUsername());
        user.setGender("未知");
        user.setPhone(request.getPhone());
        user.setEmail(request.getEmail());
        user.setAvatar(request.getAvatar());
        user.setRemark(request.getRemark());
        user.setStatus(1);
        user.setRoleCode("PORTAL_USER");
        user.setIsDeleted(0);
        user.setCreateTime(now);
        user.setUpdateTime(now);
        userMapper.insert(user);
    }

}

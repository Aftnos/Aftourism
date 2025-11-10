package aftnos.aftourismserver.auth.service.impl;

import aftnos.aftourismserver.auth.dto.LoginRequest;
import aftnos.aftourismserver.auth.dto.LoginResponse;
import aftnos.aftourismserver.auth.dto.RegisterRequest;
import aftnos.aftourismserver.auth.mapper.UserMapper;
import aftnos.aftourismserver.auth.pojo.User;
import aftnos.aftourismserver.auth.service.AuthService;
import aftnos.aftourismserver.common.exception.BusinessException;
import aftnos.aftourismserver.common.util.JwtUtils;
import org.springframework.util.StringUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class AuthServiceImpl implements AuthService {

    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;

    public AuthServiceImpl(UserMapper userMapper, PasswordEncoder passwordEncoder, JwtUtils jwtUtils) {
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtils = jwtUtils;
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
        user.setPhone(request.getPhone());
        user.setEmail(request.getEmail());
        user.setAvatar(request.getAvatar());
        user.setRemark(request.getRemark());
        user.setStatus(1);
        user.setIsDeleted(0);
        user.setCreateTime(now);
        user.setUpdateTime(now);
        userMapper.insert(user);
    }

    @Override
    public LoginResponse login(LoginRequest request) {
        User user = userMapper.findByUsername(request.getUsername());
        if (user == null) {
            throw new BusinessException("用户名或密码错误");
        }

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new BusinessException("用户名或密码错误");
        }

        // 逻辑删除或禁用的账号不允许登录
        if (user.getIsDeleted() != null && user.getIsDeleted() == 1) {
            throw new BusinessException("账号已被删除");
        }

        if (user.getStatus() != null && user.getStatus() == 0) {
            throw new BusinessException("账号已被禁用");
        }

        String token = jwtUtils.generateToken(user.getId());
        return LoginResponse.builder()
                .userId(user.getId())
                .username(user.getUsername())
                .nickname(user.getNickname())
                .avatar(user.getAvatar())
                .phone(user.getPhone())
                .email(user.getEmail())
                .status(user.getStatus())
                .token(token)
                .expiresAt(jwtUtils.calculateExpiryInstant())
                .build();
    }
}

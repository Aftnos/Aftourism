package aftnos.aftourismserver.admin.service.impl;

import aftnos.aftourismserver.admin.dto.PortalUserPageQuery;
import aftnos.aftourismserver.admin.dto.PortalUserUpdateRequest;
import aftnos.aftourismserver.admin.service.PortalUserManageService;
import aftnos.aftourismserver.admin.vo.PortalUserVO;
import aftnos.aftourismserver.auth.mapper.UserMapper;
import aftnos.aftourismserver.auth.pojo.User;
import aftnos.aftourismserver.common.exception.BusinessException;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 门户用户后台管理服务实现。
 */
@Service
public class PortalUserManageServiceImpl implements PortalUserManageService {

    private final UserMapper userMapper;

    public PortalUserManageServiceImpl(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    @Override
    public PageInfo<PortalUserVO> page(PortalUserPageQuery query) {
        PageHelper.startPage(query.getCurrent(), query.getSize());
        List<User> users = userMapper.search(query.getUsername(), query.getNickname(), query.getStatus());
        List<PortalUserVO> list = users.stream()
                .map(this::toVO)
                .collect(Collectors.toList());
        return new PageInfo<>(list);
    }

    @Override
    public void update(Long id, PortalUserUpdateRequest request) {
        User user = userMapper.findById(id);
        if (user == null || (user.getIsDeleted() != null && user.getIsDeleted() == 1)) {
            throw new BusinessException("用户不存在");
        }
        if (StringUtils.hasText(request.getRoleCode())) {
            userMapper.updateRole(id, request.getRoleCode().trim().toUpperCase());
        }
        if (request.getStatus() != null) {
            userMapper.updateStatus(id, request.getStatus());
        }
    }

    private PortalUserVO toVO(User user) {
        PortalUserVO vo = new PortalUserVO();
        vo.setId(user.getId());
        vo.setUsername(user.getUsername());
        vo.setNickname(user.getNickname());
        vo.setPhone(user.getPhone());
        vo.setEmail(user.getEmail());
        vo.setRoleCode(user.getRoleCode());
        vo.setStatus(user.getStatus());
        vo.setCreateTime(user.getCreateTime());
        vo.setUpdateTime(user.getUpdateTime());
        return vo;
    }
}

package aftnos.aftourismserver.portal.service.impl;

import aftnos.aftourismserver.admin.mapper.ActivityMapper;
import aftnos.aftourismserver.admin.pojo.Activity;
import aftnos.aftourismserver.auth.mapper.UserMapper;
import aftnos.aftourismserver.auth.pojo.User;
import aftnos.aftourismserver.common.exception.BusinessException;
import aftnos.aftourismserver.portal.dto.ActivityCommentCreateDTO;
import aftnos.aftourismserver.portal.dto.ActivityCommentPageQuery;
import aftnos.aftourismserver.portal.mapper.ActivityCommentMapper;
import aftnos.aftourismserver.portal.pojo.ActivityComment;
import aftnos.aftourismserver.portal.service.ActivityCommentService;
import aftnos.aftourismserver.portal.vo.ActivityCommentVO;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 活动留言业务实现
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ActivityCommentServiceImpl implements ActivityCommentService {

    private final ActivityMapper activityMapper;
    private final ActivityCommentMapper activityCommentMapper;
    private final UserMapper userMapper;

    @Override
    public Long addComment(Long activityId, ActivityCommentCreateDTO dto, Long userId) {
        log.info("【门户-活动留言】新增留言，活动ID={}，用户ID={}，parentId={}", activityId, userId, dto.getParentId());
        Activity activity = activityMapper.selectById(activityId);
        if (activity == null) {
            throw new BusinessException("活动不存在或已下架");
        }

        Long parentId = dto.getParentId();
        if (parentId != null) {
            ActivityComment parent = activityCommentMapper.selectById(parentId);
            if (parent == null || parent.getIsDeleted() != null && parent.getIsDeleted() == 1) {
                throw new BusinessException("父级留言不存在或已被删除");
            }
            if (!Objects.equals(parent.getActivityId(), activityId)) {
                throw new BusinessException("父级留言不属于当前活动");
            }
        }

        ActivityComment comment = new ActivityComment();
        comment.setActivityId(activityId);
        comment.setUserId(userId);
        comment.setContent(dto.getContent());
        comment.setParentId(parentId);
        comment.setLikeCount(0);
        comment.setIsDeleted(0);
        LocalDateTime now = LocalDateTime.now();
        comment.setCreateTime(now);
        comment.setUpdateTime(now);
        activityCommentMapper.insert(comment);
        log.info("【门户-活动留言】新增留言成功，生成ID={}", comment.getId());
        return comment.getId();
    }

    @Override
    public PageInfo<ActivityCommentVO> pageComments(Long activityId, ActivityCommentPageQuery query) {
        log.info("【门户-活动留言】分页查询留言，活动ID={}，parentId={}，页码={}，每页={}",
                activityId, query.getParentId(), query.getPageNum(), query.getPageSize());
        PageHelper.startPage(query.getPageNum(), query.getPageSize());
        List<ActivityCommentVO> list = activityCommentMapper.pageList(activityId, query.getParentId());
        PageInfo<ActivityCommentVO> pageInfo = new PageInfo<>(list);

        enrichUserInfo(list);
        return pageInfo;
    }

    /**
     * 为留言补充用户昵称和头像信息
     */
    private void enrichUserInfo(List<ActivityCommentVO> list) {
        if (list.isEmpty()) {
            return;
        }
        Set<Long> userIds = list.stream()
                .map(ActivityCommentVO::getUserId)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
        if (userIds.isEmpty()) {
            return;
        }
        List<User> users = userMapper.findByIds(userIds.stream().collect(Collectors.toList()));
        Map<Long, User> userMap = users == null ? Collections.emptyMap()
                : users.stream().collect(Collectors.toMap(User::getId, u -> u, (a, b) -> a));
        for (ActivityCommentVO vo : list) {
            User user = userMap.get(vo.getUserId());
            if (user != null) {
                vo.setUserNickname(user.getNickname());
                vo.setUserAvatar(user.getAvatar());
            } else {
                vo.setUserNickname("用户已注销");
                vo.setUserAvatar(null);
            }
        }
    }
}

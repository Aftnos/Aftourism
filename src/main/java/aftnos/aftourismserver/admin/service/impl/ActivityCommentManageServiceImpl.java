package aftnos.aftourismserver.admin.service.impl;

import aftnos.aftourismserver.admin.dto.ActivityCommentManageDTO;
import aftnos.aftourismserver.admin.dto.ActivityCommentManagePageQuery;
import aftnos.aftourismserver.admin.mapper.ActivityMapper;
import aftnos.aftourismserver.admin.pojo.Activity;
import aftnos.aftourismserver.admin.service.ActivityCommentManageService;
import aftnos.aftourismserver.admin.vo.ActivityCommentDetailVO;
import aftnos.aftourismserver.auth.mapper.UserMapper;
import aftnos.aftourismserver.auth.pojo.User;
import aftnos.aftourismserver.common.exception.BusinessException;
import aftnos.aftourismserver.portal.mapper.ActivityCommentMapper;
import aftnos.aftourismserver.portal.pojo.ActivityComment;
import aftnos.aftourismserver.portal.vo.ActivityCommentVO;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 活动留言后台管理实现
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ActivityCommentManageServiceImpl implements ActivityCommentManageService {

    private final ActivityMapper activityMapper;
    private final ActivityCommentMapper activityCommentMapper;
    private final UserMapper userMapper;

    @Override
    public PageInfo<ActivityCommentVO> pageComments(Long activityId, ActivityCommentManagePageQuery query) {
        Activity activity = activityMapper.selectById(activityId);
        if (activity == null || (activity.getIsDeleted() != null && activity.getIsDeleted() == 1)) {
            throw new BusinessException("活动不存在或已被删除");
        }
        int pageNum = query.getCurrent() == null ? 1 : query.getCurrent();
        int pageSize = query.getSize() == null ? 10 : query.getSize();
        PageHelper.startPage(pageNum, pageSize);
        List<ActivityCommentVO> list = activityCommentMapper.pageList(activityId, query.getParentId());
        PageInfo<ActivityCommentVO> pageInfo = new PageInfo<>(list);
        enrichUserInfo(list);
        return pageInfo;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long createComment(Long activityId, ActivityCommentManageDTO dto) {
        Activity activity = getValidActivity(activityId);
        User user = getValidUser(dto.getUserId());

        Long parentId = dto.getParentId();
        if (parentId != null) {
            ActivityComment parent = getValidComment(parentId);
            if (!Objects.equals(parent.getActivityId(), activityId)) {
                throw new BusinessException("父级留言不属于当前活动");
            }
        }

        ActivityComment comment = new ActivityComment();
        comment.setActivityId(activity.getId());
        comment.setUserId(user.getId());
        comment.setContent(dto.getContent());
        comment.setParentId(parentId);
        comment.setLikeCount(0);
        comment.setIsDeleted(0);
        LocalDateTime now = LocalDateTime.now();
        comment.setCreateTime(now);
        comment.setUpdateTime(now);
        activityCommentMapper.insert(comment);
        log.info("【后台-活动留言管理】新增留言成功，活动ID={}，留言ID={}，父级ID={}", activityId, comment.getId(), parentId);
        return comment.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateComment(Long commentId, ActivityCommentManageDTO dto) {
        ActivityComment existing = getValidComment(commentId);
        getValidActivity(existing.getActivityId());
        User user = getValidUser(dto.getUserId());

        Long parentId = dto.getParentId();
        if (parentId != null) {
            if (Objects.equals(parentId, commentId)) {
                throw new BusinessException("父级留言不能选择自身");
            }
            ActivityComment parent = getValidComment(parentId);
            if (!Objects.equals(parent.getActivityId(), existing.getActivityId())) {
                throw new BusinessException("父级留言不属于当前活动");
            }
            List<Long> descendantIds = new ArrayList<>();
            collectChildren(commentId, descendantIds);
            if (descendantIds.contains(parentId)) {
                throw new BusinessException("父级留言不能设置为自己的子楼层");
            }
        }

        LocalDateTime now = LocalDateTime.now();
        activityCommentMapper.updateForManage(commentId, dto.getContent(), parentId, user.getId(), now);
        log.info("【后台-活动留言管理】更新留言成功，留言ID={}，新父级ID={}，关联用户ID={}", commentId, parentId, user.getId());
    }

    @Override
    public ActivityCommentDetailVO commentDetail(Long commentId) {
        ActivityCommentVO main = activityCommentMapper.selectVOById(commentId);
        if (main == null) {
            throw new BusinessException("留言不存在或已删除");
        }
        List<ActivityCommentVO> replies = activityCommentMapper.listByParentId(commentId);
        List<ActivityCommentVO> all = new ArrayList<>();
        all.add(main);
        all.addAll(replies);
        enrichUserInfo(all);
        main.setChildCount(replies.size());

        ActivityCommentDetailVO detail = new ActivityCommentDetailVO();
        detail.setComment(main);
        detail.setReplies(replies);
        return detail;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteComment(Long commentId) {
        ActivityComment comment = activityCommentMapper.selectById(commentId);
        if (comment == null || comment.getIsDeleted() != null && comment.getIsDeleted() == 1) {
            throw new BusinessException("留言不存在或已删除");
        }
        List<Long> ids = new ArrayList<>();
        ids.add(commentId);
        collectChildren(commentId, ids);
        LocalDateTime now = LocalDateTime.now();
        for (Long id : ids) {
            activityCommentMapper.markDeleted(id, now);
        }
        log.info("【后台-活动留言管理】已删除留言{}条，主留言ID={}", ids.size(), commentId);
    }

    private void collectChildren(Long parentId, List<Long> container) {
        Deque<Long> stack = new ArrayDeque<>();
        stack.push(parentId);
        Set<Long> visited = new HashSet<>();
        visited.add(parentId);
        while (!stack.isEmpty()) {
            Long current = stack.pop();
            List<Long> childIds = activityCommentMapper.selectIdsByParentId(current);
            if (childIds == null || childIds.isEmpty()) {
                continue;
            }
            for (Long childId : childIds) {
                if (visited.add(childId)) {
                    container.add(childId);
                    stack.push(childId);
                }
            }
        }
    }

    private Activity getValidActivity(Long activityId) {
        Activity activity = activityMapper.selectById(activityId);
        if (activity == null || (activity.getIsDeleted() != null && activity.getIsDeleted() == 1)) {
            throw new BusinessException("活动不存在或已被删除");
        }
        return activity;
    }

    private ActivityComment getValidComment(Long commentId) {
        ActivityComment comment = activityCommentMapper.selectById(commentId);
        if (comment == null || comment.getIsDeleted() != null && comment.getIsDeleted() == 1) {
            throw new BusinessException("留言不存在或已删除");
        }
        return comment;
    }

    private User getValidUser(Long userId) {
        User user = userMapper.findById(userId);
        if (user == null || (user.getIsDeleted() != null && user.getIsDeleted() == 1)) {
            throw new BusinessException("留言用户不存在或已被删除");
        }
        if (user.getStatus() != null && user.getStatus() == 0) {
            throw new BusinessException("留言用户已被禁用");
        }
        return user;
    }

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
        List<User> users = userMapper.findByIds(new ArrayList<>(userIds));
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

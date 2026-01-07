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
    public PageInfo<ActivityCommentVO> pageAllComments(ActivityCommentManagePageQuery query) {
        int pageNum = query.getCurrent() == null ? 1 : query.getCurrent();
        int pageSize = query.getSize() == null ? 10 : query.getSize();
        PageHelper.startPage(pageNum, pageSize);
        List<ActivityCommentVO> list = activityCommentMapper.pageList(null, query.getParentId());
        PageInfo<ActivityCommentVO> pageInfo = new PageInfo<>(list);
        enrichUserInfo(list);
        return pageInfo;
    }

    @Override
    public PageInfo<ActivityCommentVO> pageCommentsByActivity(Long activityId, ActivityCommentManagePageQuery query) {
        // 1. 校验活动有效性，避免查询已删除数据
        getValidActivity(activityId);

        // 2. 分页查询，支持通过 parentId 筛选父级/子级留言
        int pageNum = query.getCurrent() == null ? 1 : query.getCurrent();
        int pageSize = query.getSize() == null ? 10 : query.getSize();
        PageHelper.startPage(pageNum, pageSize);
        List<ActivityCommentVO> list = activityCommentMapper.pageList(activityId, query.getParentId());
        PageInfo<ActivityCommentVO> pageInfo = new PageInfo<>(list);

        // 3. 补充用户昵称/头像，方便后台直接展示
        enrichUserInfo(list);
        return pageInfo;
    }

    @Override
    public List<ActivityCommentVO> listChildren(Long parentId) {
        // 1. 确认父级留言存在且未删除
        getValidComment(parentId);

        // 2. 查询所有子留言并填充用户信息
        List<ActivityCommentVO> children = activityCommentMapper.listByParentId(parentId);
        enrichUserInfo(children);
        return children;
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
        ActivityComment comment = getValidComment(commentId);
        List<Long> ids = new ArrayList<>();
        ids.add(commentId);
        collectChildren(commentId, ids);
        LocalDateTime now = LocalDateTime.now();
        for (Long id : ids) {
            activityCommentMapper.markDeleted(id, now);
        }
        log.info("【后台-活动留言管理】已删除留言{}条，主留言ID={}", ids.size(), commentId);
    }

    /**
     * 递归收集子留言 ID，避免循环依赖导致死循环。
     *
     * @param parentId  当前节点 ID
     * @param container 收集容器
     */
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

    /**
     * 校验活动是否存在且未删除。
     */
    private Activity getValidActivity(Long activityId) {
        Activity activity = activityMapper.selectById(activityId);
        if (activity == null || (activity.getIsDeleted() != null && activity.getIsDeleted() == 1)) {
            throw new BusinessException("活动不存在或已被删除");
        }
        return activity;
    }

    /**
     * 校验评论是否有效。
     */
    private ActivityComment getValidComment(Long commentId) {
        ActivityComment comment = activityCommentMapper.selectById(commentId);
        if (comment == null || comment.getIsDeleted() != null && comment.getIsDeleted() == 1) {
            throw new BusinessException("留言不存在或已删除");
        }
        return comment;
    }

    /**
     * 校验留言用户是否有效且未被禁用。
     */
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

    /**
     * 批量补充用户昵称、头像信息。
     */
    private void enrichUserInfo(List<ActivityCommentVO> list) {
        if (list.isEmpty()) {
            return;
        }
        Set<Long> userIds = list.stream()
                .flatMap(vo -> {
                    List<Long> ids = new ArrayList<>();
                    if (vo.getUserId() != null) {
                        ids.add(vo.getUserId());
                    }
                    if (vo.getMentionUserId() != null) {
                        ids.add(vo.getMentionUserId());
                    }
                    return ids.stream();
                })
                .collect(Collectors.toSet());
        if (userIds.isEmpty()) {
            return;
        }
        List<User> users = userMapper.findByIds(new ArrayList<>(userIds));
        var userMap = users == null ? Collections.<Long, User>emptyMap()
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
            if (vo.getMentionUserId() != null) {
                User mentionUser = userMap.get(vo.getMentionUserId());
                if (mentionUser != null) {
                    vo.setMentionUserNickname(mentionUser.getNickname());
                    vo.setMentionUserAvatar(mentionUser.getAvatar());
                } else {
                    vo.setMentionUserNickname("用户已注销");
                }
            }
        }
    }
}

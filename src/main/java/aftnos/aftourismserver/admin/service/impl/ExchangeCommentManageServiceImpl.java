package aftnos.aftourismserver.admin.service.impl;

import aftnos.aftourismserver.admin.dto.ExchangeCommentUpdateDTO;
import aftnos.aftourismserver.admin.service.ExchangeCommentManageService;
import aftnos.aftourismserver.auth.mapper.UserMapper;
import aftnos.aftourismserver.auth.pojo.User;
import aftnos.aftourismserver.common.exception.BusinessException;
import aftnos.aftourismserver.portal.dto.ExchangeCommentPageQuery;
import aftnos.aftourismserver.portal.mapper.ExchangeCommentMapper;
import aftnos.aftourismserver.portal.pojo.ExchangeComment;
import aftnos.aftourismserver.portal.vo.ExchangeCommentVO;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 交流评论后台管理实现
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ExchangeCommentManageServiceImpl implements ExchangeCommentManageService {

    private final ExchangeCommentMapper exchangeCommentMapper;
    private final UserMapper userMapper;

    @Override
    public PageInfo<ExchangeCommentVO> page(Long articleId, ExchangeCommentPageQuery query) {
        PageHelper.startPage(query.getCurrent(), query.getSize());
        List<ExchangeCommentVO> list = exchangeCommentMapper.pageList(articleId, query.getParentId());
        PageInfo<ExchangeCommentVO> pageInfo = new PageInfo<>(list);
        if (query.getParentId() == null && !list.isEmpty()) {
            List<Long> parentIds = list.stream().map(ExchangeCommentVO::getId).toList();
            List<ExchangeCommentVO> children = exchangeCommentMapper.listByParentIds(parentIds);
            Map<Long, List<ExchangeCommentVO>> childMap = children.stream()
                    .collect(Collectors.groupingBy(ExchangeCommentVO::getParentId, LinkedHashMap::new, Collectors.toList()));
            for (ExchangeCommentVO vo : list) {
                vo.setChildren(childMap.getOrDefault(vo.getId(), Collections.emptyList()));
            }
            enrichUserInfo(flattenWithChildren(list));
        } else {
            enrichUserInfo(list);
        }
        return pageInfo;
    }

    @Override
    public void update(Long commentId, ExchangeCommentUpdateDTO dto) {
        ExchangeComment comment = exchangeCommentMapper.selectById(commentId);
        if (comment == null || (comment.getIsDeleted() != null && comment.getIsDeleted() == 1)) {
            throw new BusinessException("评论不存在或已删除");
        }
        exchangeCommentMapper.updateContent(commentId, dto.getContent(), LocalDateTime.now());
    }

    @Override
    public void delete(Long commentId) {
        ExchangeComment comment = exchangeCommentMapper.selectById(commentId);
        if (comment == null || (comment.getIsDeleted() != null && comment.getIsDeleted() == 1)) {
            throw new BusinessException("评论不存在或已删除");
        }
        exchangeCommentMapper.markDeleted(commentId, LocalDateTime.now());
    }

    private void enrichUserInfo(List<ExchangeCommentVO> list) {
        if (list == null || list.isEmpty()) {
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
        List<User> users = userMapper.findByIds(userIds.stream().toList());
        Map<Long, User> userMap = users == null ? Collections.emptyMap()
                : users.stream().collect(Collectors.toMap(User::getId, u -> u, (a, b) -> a));
        for (ExchangeCommentVO vo : list) {
            User user = userMap.get(vo.getUserId());
            if (user != null) {
                vo.setUserNickname(user.getNickname());
                vo.setUserAvatar(user.getAvatar());
            } else {
                vo.setUserNickname("用户已注销");
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

    private List<ExchangeCommentVO> flattenWithChildren(List<ExchangeCommentVO> parents) {
        List<ExchangeCommentVO> all = new ArrayList<>();
        for (ExchangeCommentVO parent : parents) {
            all.add(parent);
            if (parent.getChildren() != null && !parent.getChildren().isEmpty()) {
                all.addAll(parent.getChildren());
            }
        }
        return all;
    }
}

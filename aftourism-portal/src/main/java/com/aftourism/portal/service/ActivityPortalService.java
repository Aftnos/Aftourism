package com.aftourism.portal.service;

import com.aftourism.portal.dto.ActivityApplyRequest;
import com.aftourism.portal.dto.CommentRequest;
import com.aftourism.portal.vo.ActivityDetailVO;
import com.aftourism.portal.vo.ActivityVO;

import java.util.List;

/**
 * 门户活动服务。
 */
public interface ActivityPortalService {

    List<ActivityVO> listActivities();

    ActivityDetailVO getDetail(Long id);

    Long apply(ActivityApplyRequest request, Long userId);

    Long comment(CommentRequest request, Long userId);
}

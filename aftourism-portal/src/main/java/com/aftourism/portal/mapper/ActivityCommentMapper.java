package com.aftourism.portal.mapper;

import com.aftourism.portal.pojo.ActivityComment;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 活动留言 Mapper。
 */
@Mapper
public interface ActivityCommentMapper {

    void insert(ActivityComment comment);

    List<ActivityComment> selectByActivity(@Param("activityId") Long activityId);
}

package com.aftourism.portal.mapper;

import com.aftourism.portal.pojo.Activity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 活动 Mapper。
 */
@Mapper
public interface ActivityMapper {

    List<Activity> selectOnlineList();

    Activity selectById(@Param("id") Long id);

    void insert(Activity activity);
}

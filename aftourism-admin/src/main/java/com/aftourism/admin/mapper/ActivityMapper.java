package com.aftourism.admin.mapper;

import com.aftourism.admin.pojo.Activity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 活动数据访问层。
 */
@Mapper
public interface ActivityMapper {

    List<Activity> selectPendingList();

    void updateStatus(@Param("activity") Activity activity);
}

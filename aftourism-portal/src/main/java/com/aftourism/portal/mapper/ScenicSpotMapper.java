package com.aftourism.portal.mapper;

import com.aftourism.portal.pojo.ScenicSpot;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 景区 Mapper。
 */
@Mapper
public interface ScenicSpotMapper {

    List<ScenicSpot> selectAll();
}

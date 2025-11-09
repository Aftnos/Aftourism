package com.aftourism.portal.mapper;

import com.aftourism.portal.pojo.Venue;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 场馆 Mapper。
 */
@Mapper
public interface VenueMapper {

    List<Venue> selectAll();

    Venue selectById(@Param("id") Long id);
}

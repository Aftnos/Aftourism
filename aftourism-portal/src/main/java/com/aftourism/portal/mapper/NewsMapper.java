package com.aftourism.portal.mapper;

import com.aftourism.portal.pojo.News;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 新闻 Mapper。
 */
@Mapper
public interface NewsMapper {

    List<News> selectLatest(int limit);
}

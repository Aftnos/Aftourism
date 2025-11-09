package com.aftourism.admin.mapper;

import com.aftourism.admin.pojo.News;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 新闻管理 Mapper。
 */
@Mapper
public interface NewsMapper {

    void insert(News news);

    void update(@Param("news") News news);

    void delete(@Param("id") Long id);

    News selectById(@Param("id") Long id);

    List<News> selectAll();
}

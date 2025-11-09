package com.aftourism.common.news;

import java.util.List;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

/**
 * Mapper for CRUD operations on the news table.
 */
@Mapper
public interface NewsMapper {

    @Insert("INSERT INTO news(title, content, publish_time, author_id, is_deleted, create_time, update_time) "
            + "VALUES(#{title}, #{content}, #{publishTime}, #{authorId}, 0, NOW(), NOW())")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(News news);

    @Update("UPDATE news SET title=#{title}, content=#{content}, publish_time=#{publishTime}, update_time=NOW() "
            + "WHERE id=#{id} AND is_deleted=0")
    int update(News news);

    @Update("UPDATE news SET is_deleted=1, update_time=NOW() WHERE id=#{id} AND is_deleted=0")
    int logicDelete(@Param("id") Long id);

    @Select("SELECT id, title, content, publish_time, author_id, is_deleted, create_time, update_time "
            + "FROM news WHERE id=#{id} AND is_deleted=0")
    News selectById(@Param("id") Long id);

    @Select("SELECT id, title, content, publish_time, author_id, is_deleted, create_time, update_time "
            + "FROM news WHERE is_deleted=0 ORDER BY publish_time DESC, create_time DESC")
    List<News> selectAll();
}

package aftnos.aftourismserver.admin.mapper;

import aftnos.aftourismserver.admin.dto.NewsPageQuery;
import aftnos.aftourismserver.admin.pojo.News;
import aftnos.aftourismserver.admin.vo.NewsVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface NewsMapper {

    int insert(News news);

    int update(News news);

    News selectById(@Param("id") Long id);

    List<NewsVO> pageList(NewsPageQuery query);

    int logicalDelete(@Param("id") Long id, @Param("updateTime") LocalDateTime updateTime);
}

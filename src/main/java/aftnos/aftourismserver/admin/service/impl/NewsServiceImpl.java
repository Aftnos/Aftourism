package aftnos.aftourismserver.admin.service.impl;

import aftnos.aftourismserver.admin.dto.NewsDTO;
import aftnos.aftourismserver.admin.dto.NewsPageQuery;
import aftnos.aftourismserver.admin.enums.NewsStatusEnum;
import aftnos.aftourismserver.admin.mapper.NewsMapper;
import aftnos.aftourismserver.admin.pojo.News;
import aftnos.aftourismserver.admin.service.NewsService;
import aftnos.aftourismserver.admin.vo.NewsVO;
import aftnos.aftourismserver.common.exception.BusinessException;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class NewsServiceImpl implements NewsService {

    private final NewsMapper newsMapper;

    @Override
    public Long createNews(NewsDTO newsDTO) {
        log.info("【新增新闻】开始处理，标题={}", newsDTO.getTitle());
        validateStatus(newsDTO.getStatus());
        LocalDateTime now = LocalDateTime.now();
        News news = new News();
        BeanUtils.copyProperties(newsDTO, news);
        news.setIsDeleted(0);
        news.setCreateTime(now);
        news.setUpdateTime(now);
        int rows = newsMapper.insert(news);
        log.info("【新增新闻】写入完成，影响行数={}，新闻ID={}", rows, news.getId());
        return news.getId();
    }

    @Override
    public void updateNews(Long id, NewsDTO newsDTO) {
        log.info("【修改新闻】开始处理，新闻ID={}", id);
        News dbNews = newsMapper.selectById(id);
        if (dbNews == null) {
            log.warn("【修改新闻】目标新闻不存在或已删除，新闻ID={}", id);
            throw new BusinessException("新闻不存在或已被删除");
        }
        validateStatus(newsDTO.getStatus());
        News updateNews = new News();
        BeanUtils.copyProperties(newsDTO, updateNews);
        updateNews.setId(id);
        updateNews.setUpdateTime(LocalDateTime.now());
        int rows = newsMapper.update(updateNews);
        if (rows == 0) {
            log.warn("【修改新闻】更新失败，新闻ID={}", id);
            throw new BusinessException("新闻更新失败，请稍后重试");
        }
        log.info("【修改新闻】处理完成，影响行数={}，新闻ID={}", rows, id);
    }

    @Override
    public void deleteNews(Long id) {
        log.info("【删除新闻】开始处理，新闻ID={}", id);
        News dbNews = newsMapper.selectById(id);
        if (dbNews == null) {
            log.warn("【删除新闻】目标新闻不存在或已删除，新闻ID={}", id);
            throw new BusinessException("新闻不存在或已被删除");
        }
        int rows = newsMapper.logicalDelete(id, LocalDateTime.now());
        if (rows == 0) {
            log.warn("【删除新闻】删除失败，新闻ID={}", id);
            throw new BusinessException("新闻删除失败，请稍后重试");
        }
        log.info("【删除新闻】处理完成，影响行数={}，新闻ID={}", rows, id);
    }

    @Override
    public PageInfo<NewsVO> pageNews(NewsPageQuery query) {
        log.info("【分页查询新闻】开始处理，页码={}，每页条数={}", query.getPageNum(), query.getPageSize());
        PageHelper.startPage(query.getPageNum(), query.getPageSize());
        List<NewsVO> list = newsMapper.pageList(query);
        list.forEach(item -> item.setStatusText(NewsStatusEnum.getTextByCode(item.getStatus())));
        PageInfo<NewsVO> pageInfo = new PageInfo<>(list);
        log.info("【分页查询新闻】查询完成，记录总数={}", pageInfo.getTotal());
        return pageInfo;
    }

    private void validateStatus(Integer status) {
        if (!NewsStatusEnum.isValid(status)) {
            log.warn("【新闻状态校验】状态值不合法: {}", status);
            throw new BusinessException("新闻状态不合法");
        }
    }
}

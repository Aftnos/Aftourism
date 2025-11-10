package aftnos.aftourismserver.admin.service;

import aftnos.aftourismserver.admin.dto.NewsDTO;
import aftnos.aftourismserver.admin.mapper.NewsMapper;
import aftnos.aftourismserver.admin.pojo.News;
import aftnos.aftourismserver.admin.service.impl.NewsServiceImpl;
import aftnos.aftourismserver.common.exception.BusinessException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NewsServiceImplTest {

    @Mock
    private NewsMapper newsMapper;

    @InjectMocks
    private NewsServiceImpl newsService;

    @Test
    void createNews_success() {
        NewsDTO dto = buildNewsDTO();
        when(newsMapper.insert(any())).thenAnswer(invocation -> {
            News news = invocation.getArgument(0);
            news.setId(1L);
            return 1;
        });

        Long newsId = newsService.createNews(dto);

        ArgumentCaptor<News> captor = ArgumentCaptor.forClass(News.class);
        verify(newsMapper).insert(captor.capture());
        News saved = captor.getValue();
        assertThat(saved.getIsDeleted()).isZero();
        assertThat(saved.getCreateTime()).isNotNull();
        assertThat(saved.getUpdateTime()).isNotNull();
        assertThat(newsId).isEqualTo(1L);
    }

    @Test
    void updateNews_notFoundShouldThrowException() {
        NewsDTO dto = buildNewsDTO();
        when(newsMapper.selectById(1L)).thenReturn(null);

        assertThrows(BusinessException.class, () -> newsService.updateNews(1L, dto));
        verify(newsMapper, never()).update(any());
    }

    private NewsDTO buildNewsDTO() {
        NewsDTO dto = new NewsDTO();
        dto.setTitle("测试新闻");
        dto.setSummary("这是一条测试新闻摘要");
        dto.setContent("这是一条测试新闻的完整内容");
        dto.setCoverImage("https://example.com/cover.png");
        dto.setStatus(1);
        dto.setPublishTime(LocalDateTime.now());
        return dto;
    }
    private NewsDTO buildNewsDTO1() {
        NewsDTO dto = new NewsDTO();
        dto.setTitle("测试新闻");
        dto.setSummary("这是一条测试新闻摘要");
        dto.setContent("这是一条测试新闻的完整内容");
        dto.setCoverImage("https://example.com/cover.png");
        dto.setStatus(2);
        dto.setPublishTime(LocalDateTime.now());
        return dto;
    }
}

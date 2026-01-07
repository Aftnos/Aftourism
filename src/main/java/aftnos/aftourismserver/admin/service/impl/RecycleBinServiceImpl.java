package aftnos.aftourismserver.admin.service.impl;

import aftnos.aftourismserver.admin.dto.RecycleQueryDTO;
import aftnos.aftourismserver.admin.enums.RecycleType;
import aftnos.aftourismserver.admin.mapper.ActivityMapper;
import aftnos.aftourismserver.admin.mapper.NewsMapper;
import aftnos.aftourismserver.admin.mapper.NoticeMapper;
import aftnos.aftourismserver.admin.mapper.ScenicSpotMapper;
import aftnos.aftourismserver.admin.mapper.VenueMapper;
import aftnos.aftourismserver.admin.service.RecycleBinService;
import aftnos.aftourismserver.admin.vo.RecycleItemVO;
import aftnos.aftourismserver.common.exception.BusinessException;
import aftnos.aftourismserver.portal.mapper.ActivityCommentMapper;
import aftnos.aftourismserver.portal.mapper.ExchangeArticleMapper;
import aftnos.aftourismserver.portal.mapper.ExchangeCommentMapper;
import aftnos.aftourismserver.portal.mapper.MessageFeedbackCommentMapper;
import aftnos.aftourismserver.portal.mapper.MessageFeedbackMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

/**
 * 回收站业务实现
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class RecycleBinServiceImpl implements RecycleBinService {

    private final NewsMapper newsMapper;
    private final NoticeMapper noticeMapper;
    private final ScenicSpotMapper scenicSpotMapper;
    private final VenueMapper venueMapper;
    private final ActivityMapper activityMapper;
    private final ExchangeArticleMapper exchangeArticleMapper;
    private final ExchangeCommentMapper exchangeCommentMapper;
    private final ActivityCommentMapper activityCommentMapper;
    private final MessageFeedbackMapper messageFeedbackMapper;
    private final MessageFeedbackCommentMapper messageFeedbackCommentMapper;

    @Override
    public PageInfo<RecycleItemVO> pageDeletedItems(RecycleQueryDTO dto) {
        log.info("【回收站-分页查询】开始处理，类型={}，页码={}，每页条数={}", dto.getType(), dto.getCurrent(), dto.getSize());
        RecycleType type = dto.getType();
        int pageNum = dto.getCurrent() != null ? dto.getCurrent() : 1;
        int pageSize = dto.getSize() != null ? dto.getSize() : 10;

        if (type == null) {
            List<RecycleItemVO> all = queryDeletedListAll(dto);
            all.sort(Comparator.comparing(RecycleItemVO::getDeletedTime, Comparator.nullsLast(Comparator.naturalOrder()))
                .reversed());
            int total = all.size();
            int fromIndex = Math.max((pageNum - 1) * pageSize, 0);
            int toIndex = Math.min(fromIndex + pageSize, total);
            List<RecycleItemVO> pageList = fromIndex >= total ? List.of() : all.subList(fromIndex, toIndex);
            PageInfo<RecycleItemVO> pageInfo = new PageInfo<>(pageList);
            pageInfo.setTotal(total);
            pageInfo.setPageNum(pageNum);
            pageInfo.setPageSize(pageSize);
            pageInfo.setSize(pageList.size());
            log.info("【回收站-分页查询】处理完成（所有类型），记录总数={}", total);
            return pageInfo;
        }

        PageHelper.startPage(pageNum, pageSize);
        List<RecycleItemVO> list = queryDeletedList(type, dto);
        list.forEach(item -> item.setType(type));
        PageInfo<RecycleItemVO> pageInfo = new PageInfo<>(list);
        log.info("【回收站-分页查询】处理完成，记录总数={}", pageInfo.getTotal());
        return pageInfo;
    }

    @Override
    public void restoreItem(RecycleType type, Long id) {
        log.info("【回收站-恢复】开始处理，类型={}，ID={}", type, id);
        int rows = doRestore(type, id);
        if (rows == 0) {
            log.warn("【回收站-恢复】目标记录不存在或未删除，类型={}，ID={}", type, id);
            throw new BusinessException("目标记录不存在或已恢复");
        }
        log.info("【回收站-恢复】处理完成，影响行数={}，类型={}，ID={}", rows, type, id);
    }

    @Override
    public void forceDeleteItem(RecycleType type, Long id) {
        log.info("【回收站-彻底删除】开始处理，类型={}，ID={}", type, id);
        int rows = doForceDelete(type, id);
        if (rows == 0) {
            log.warn("【回收站-彻底删除】目标记录不存在或未处于回收站，类型={}，ID={}", type, id);
            throw new BusinessException("目标记录不存在或已被删除");
        }
        log.info("【回收站-彻底删除】处理完成，影响行数={}，类型={}，ID={}", rows, type, id);
    }

    private List<RecycleItemVO> queryDeletedList(RecycleType type, RecycleQueryDTO dto) {
        return switch (type) {
            case NEWS -> newsMapper.selectDeletedList(dto.getKeyword(), dto.getStartTime(), dto.getEndTime());
            case NOTICE -> noticeMapper.selectDeletedList(dto.getKeyword(), dto.getStartTime(), dto.getEndTime());
            case SCENIC -> scenicSpotMapper.selectDeletedList(dto.getKeyword(), dto.getStartTime(), dto.getEndTime());
            case VENUE -> venueMapper.selectDeletedList(dto.getKeyword(), dto.getStartTime(), dto.getEndTime());
            case ACTIVITY -> activityMapper.selectDeletedList(dto.getKeyword(), dto.getStartTime(), dto.getEndTime());
            case EXCHANGE_ARTICLE -> exchangeArticleMapper.selectDeletedList(dto.getKeyword(), dto.getStartTime(), dto.getEndTime());
            case EXCHANGE_COMMENT -> exchangeCommentMapper.selectDeletedList(dto.getKeyword(), dto.getStartTime(), dto.getEndTime());
            case ACTIVITY_COMMENT -> activityCommentMapper.selectDeletedList(dto.getKeyword(), dto.getStartTime(), dto.getEndTime());
            case FEEDBACK -> messageFeedbackMapper.selectDeletedList(dto.getKeyword(), dto.getStartTime(), dto.getEndTime());
            case FEEDBACK_COMMENT -> messageFeedbackCommentMapper.selectDeletedList(dto.getKeyword(), dto.getStartTime(), dto.getEndTime());
        };
    }

    private List<RecycleItemVO> queryDeletedListAll(RecycleQueryDTO dto) {
        List<RecycleItemVO> list = new java.util.ArrayList<>();
        List<RecycleItemVO> news = newsMapper.selectDeletedList(dto.getKeyword(), dto.getStartTime(), dto.getEndTime());
        news.forEach(item -> item.setType(RecycleType.NEWS));
        list.addAll(news);

        List<RecycleItemVO> notice = noticeMapper.selectDeletedList(dto.getKeyword(), dto.getStartTime(), dto.getEndTime());
        notice.forEach(item -> item.setType(RecycleType.NOTICE));
        list.addAll(notice);

        List<RecycleItemVO> scenic = scenicSpotMapper.selectDeletedList(dto.getKeyword(), dto.getStartTime(), dto.getEndTime());
        scenic.forEach(item -> item.setType(RecycleType.SCENIC));
        list.addAll(scenic);

        List<RecycleItemVO> venue = venueMapper.selectDeletedList(dto.getKeyword(), dto.getStartTime(), dto.getEndTime());
        venue.forEach(item -> item.setType(RecycleType.VENUE));
        list.addAll(venue);

        List<RecycleItemVO> activity = activityMapper.selectDeletedList(dto.getKeyword(), dto.getStartTime(), dto.getEndTime());
        activity.forEach(item -> item.setType(RecycleType.ACTIVITY));
        list.addAll(activity);

        List<RecycleItemVO> exchangeArticle = exchangeArticleMapper.selectDeletedList(dto.getKeyword(), dto.getStartTime(), dto.getEndTime());
        exchangeArticle.forEach(item -> item.setType(RecycleType.EXCHANGE_ARTICLE));
        list.addAll(exchangeArticle);

        List<RecycleItemVO> exchangeComment = exchangeCommentMapper.selectDeletedList(dto.getKeyword(), dto.getStartTime(), dto.getEndTime());
        exchangeComment.forEach(item -> item.setType(RecycleType.EXCHANGE_COMMENT));
        list.addAll(exchangeComment);

        List<RecycleItemVO> activityComment = activityCommentMapper.selectDeletedList(dto.getKeyword(), dto.getStartTime(), dto.getEndTime());
        activityComment.forEach(item -> item.setType(RecycleType.ACTIVITY_COMMENT));
        list.addAll(activityComment);

        List<RecycleItemVO> feedback = messageFeedbackMapper.selectDeletedList(dto.getKeyword(), dto.getStartTime(), dto.getEndTime());
        feedback.forEach(item -> item.setType(RecycleType.FEEDBACK));
        list.addAll(feedback);

        List<RecycleItemVO> feedbackComment = messageFeedbackCommentMapper.selectDeletedList(dto.getKeyword(), dto.getStartTime(), dto.getEndTime());
        feedbackComment.forEach(item -> item.setType(RecycleType.FEEDBACK_COMMENT));
        list.addAll(feedbackComment);

        return list;
    }

    private int doRestore(RecycleType type, Long id) {
        LocalDateTime now = LocalDateTime.now();
        return switch (type) {
            case NEWS -> newsMapper.restoreById(id, now);
            case NOTICE -> noticeMapper.restoreById(id, now);
            case SCENIC -> scenicSpotMapper.restoreById(id, now);
            case VENUE -> venueMapper.restoreById(id, now);
            case ACTIVITY -> activityMapper.restoreById(id, now);
            case EXCHANGE_ARTICLE -> exchangeArticleMapper.restoreById(id, now);
            case EXCHANGE_COMMENT -> exchangeCommentMapper.restoreById(id, now);
            case ACTIVITY_COMMENT -> activityCommentMapper.restoreById(id, now);
            case FEEDBACK -> messageFeedbackMapper.restoreById(id, now);
            case FEEDBACK_COMMENT -> messageFeedbackCommentMapper.restoreById(id, now);
        };
    }

    private int doForceDelete(RecycleType type, Long id) {
        return switch (type) {
            case NEWS -> newsMapper.forceDeleteById(id);
            case NOTICE -> noticeMapper.forceDeleteById(id);
            case SCENIC -> scenicSpotMapper.forceDeleteById(id);
            case VENUE -> venueMapper.forceDeleteById(id);
            case ACTIVITY -> activityMapper.forceDeleteById(id);
            case EXCHANGE_ARTICLE -> exchangeArticleMapper.forceDeleteById(id);
            case EXCHANGE_COMMENT -> exchangeCommentMapper.forceDeleteById(id);
            case ACTIVITY_COMMENT -> activityCommentMapper.forceDeleteById(id);
            case FEEDBACK -> messageFeedbackMapper.forceDeleteById(id);
            case FEEDBACK_COMMENT -> messageFeedbackCommentMapper.forceDeleteById(id);
        };
    }
}

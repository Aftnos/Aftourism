package aftnos.aftourismserver.admin.service.impl;

import aftnos.aftourismserver.admin.dto.ExchangeArticleAuditDTO;
import aftnos.aftourismserver.admin.dto.ExchangeArticleManagePageQuery;
import aftnos.aftourismserver.admin.service.ExchangeArticleManageService;
import aftnos.aftourismserver.admin.vo.ExchangeArticleManageVO;
import aftnos.aftourismserver.common.exception.BusinessException;
import aftnos.aftourismserver.portal.enums.ExchangeArticleStatusEnum;
import aftnos.aftourismserver.portal.enums.PortalNotificationTypeEnum;
import aftnos.aftourismserver.portal.mapper.ExchangeArticleMapper;
import aftnos.aftourismserver.portal.pojo.ExchangeArticle;
import aftnos.aftourismserver.portal.service.PortalNotificationService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 交流文章后台管理实现
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ExchangeArticleManageServiceImpl implements ExchangeArticleManageService {

    private final ExchangeArticleMapper exchangeArticleMapper;
    private final PortalNotificationService portalNotificationService;

    @Override
    public PageInfo<ExchangeArticleManageVO> page(ExchangeArticleManagePageQuery query) {
        PageHelper.startPage(query.getCurrent(), query.getSize());
        List<ExchangeArticleManageVO> list = exchangeArticleMapper.pageListForManage(query.getStatus(), query.getKeyword());
        fillStatusText(list);
        return new PageInfo<>(list);
    }

    @Override
    public ExchangeArticleManageVO detail(Long id) {
        ExchangeArticleManageVO vo = exchangeArticleMapper.selectManageById(id);
        if (vo == null) {
            throw new BusinessException("交流文章不存在或已删除");
        }
        fillStatusText(List.of(vo));
        return vo;
    }

    @Override
    public void audit(Long id, ExchangeArticleAuditDTO dto) {
        ExchangeArticle article = exchangeArticleMapper.selectById(id);
        if (article == null || (article.getIsDeleted() != null && article.getIsDeleted() == 1)) {
            throw new BusinessException("交流文章不存在或已删除");
        }
        ExchangeArticleStatusEnum statusEnum = ExchangeArticleStatusEnum.fromCode(dto.getStatus());
        exchangeArticleMapper.updateStatus(id, statusEnum.getCode(), dto.getAuditRemark(), LocalDateTime.now());
        String content = statusEnum == ExchangeArticleStatusEnum.APPROVED
                ? "你的交流文章已通过审核"
                : "你的交流文章未通过审核";
        portalNotificationService.createNotification(
                article.getUserId(),
                PortalNotificationTypeEnum.AUDIT.getCode(),
                "交流文章审核结果",
                content,
                "ARTICLE",
                id
        );
    }

    private void fillStatusText(List<ExchangeArticleManageVO> list) {
        if (list == null || list.isEmpty()) {
            return;
        }
        for (ExchangeArticleManageVO vo : list) {
            ExchangeArticleStatusEnum statusEnum = ExchangeArticleStatusEnum.fromCode(vo.getStatus());
            vo.setStatusText(statusEnum.getText());
        }
    }
}

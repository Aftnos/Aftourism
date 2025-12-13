package aftnos.aftourismserver.admin.service.impl;

import aftnos.aftourismserver.admin.dto.HomeBannerDTO;
import aftnos.aftourismserver.admin.dto.HomeContentSaveDTO;
import aftnos.aftourismserver.admin.mapper.HomeContentMapper;
import aftnos.aftourismserver.admin.pojo.HomeBanner;
import aftnos.aftourismserver.admin.pojo.HomeIntro;
import aftnos.aftourismserver.admin.service.HomeContentService;
import aftnos.aftourismserver.admin.vo.HomeContentAdminVO;
import aftnos.aftourismserver.common.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 后台首页内容管理实现。
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class HomeContentServiceImpl implements HomeContentService {

    private final HomeContentMapper homeContentMapper;

    @Override
    public HomeContentAdminVO queryContent() {
        log.info("【后台-查询首页配置】开始读取轮播与简介信息");
        HomeContentAdminVO vo = new HomeContentAdminVO();
        vo.setIntro(homeContentMapper.selectIntro());
        vo.setBanners(homeContentMapper.selectAllBanners());
        return vo;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveContent(HomeContentSaveDTO dto) {
        log.info("【后台-保存首页配置】开始保存文旅简介与{}条轮播图", dto.getBanners() == null ? 0 : dto.getBanners().size());
        LocalDateTime now = LocalDateTime.now();

        // 处理简介信息：有则更新，无则新增
        HomeIntro intro = homeContentMapper.selectIntroEntity();
        if (intro == null) {
            intro = new HomeIntro();
            intro.setCreateTime(now);
            intro.setUpdateTime(now);
            intro.setTitle(dto.getTitle());
            intro.setContent(dto.getContent());
            intro.setCoverUrl(dto.getCoverUrl());
            int rows = homeContentMapper.insertIntro(intro);
            if (rows == 0) {
                throw new BusinessException("保存简介失败");
            }
        } else {
            intro.setTitle(dto.getTitle());
            intro.setContent(dto.getContent());
            intro.setCoverUrl(dto.getCoverUrl());
            intro.setUpdateTime(now);
            int rows = homeContentMapper.updateIntro(intro);
            if (rows == 0) {
                throw new BusinessException("更新简介失败");
            }
        }

        // 处理轮播：先逻辑删除旧数据，再批量插入最新数据
        homeContentMapper.logicalDeleteAllBanners(now);
        if (!CollectionUtils.isEmpty(dto.getBanners())) {
            List<HomeBanner> bannerEntities = new ArrayList<>();
            for (HomeBannerDTO bannerDTO : dto.getBanners()) {
                HomeBanner banner = new HomeBanner();
                banner.setTitle(bannerDTO.getTitle());
                banner.setImageUrl(bannerDTO.getImageUrl());
                banner.setLinkUrl(bannerDTO.getLinkUrl());
                banner.setSort(bannerDTO.getSort() == null ? 0 : bannerDTO.getSort());
                banner.setIsEnabled(Boolean.TRUE.equals(bannerDTO.getEnabled()) ? 1 : 0);
                banner.setIsDeleted(0);
                banner.setCreateTime(now);
                banner.setUpdateTime(now);
                bannerEntities.add(banner);
            }
            int inserted = homeContentMapper.batchInsertBanners(bannerEntities);
            if (inserted != bannerEntities.size()) {
                log.warn("【后台-保存首页配置】轮播插入数量与期望不一致，期望={}，实际={}", bannerEntities.size(), inserted);
            }
        }
    }
}

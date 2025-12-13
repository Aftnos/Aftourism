package aftnos.aftourismserver.admin.service.impl;

import aftnos.aftourismserver.admin.dto.HomeBannerDTO;
import aftnos.aftourismserver.admin.dto.HomeContentSaveDTO;
import aftnos.aftourismserver.admin.dto.HomeScenicDTO;
import aftnos.aftourismserver.admin.mapper.HomeContentMapper;
import aftnos.aftourismserver.admin.pojo.HomeBanner;
import aftnos.aftourismserver.admin.pojo.HomeIntro;
import aftnos.aftourismserver.admin.pojo.HomeScenic;
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
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

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
        HomeIntro intro = homeContentMapper.selectIntroEntity();
        vo.setIntro(homeContentMapper.selectIntro());
        vo.setBanners(homeContentMapper.selectAllBanners());
        vo.setScenics(homeContentMapper.selectAllScenics());
        vo.setScenicLimit(intro == null ? null : intro.getScenicLimit());
        return vo;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveContent(HomeContentSaveDTO dto) {
        log.info("【后台-保存首页配置】开始保存文旅简介与{}条轮播图", dto.getBanners() == null ? 0 : dto.getBanners().size());
        LocalDateTime now = LocalDateTime.now();
        String coverType = dto.getCoverType();
        if (coverType == null || coverType.isBlank()) {
            coverType = "IMAGE";
        }

        Integer scenicLimit = dto.getScenicLimit();
        if (scenicLimit == null || scenicLimit <= 0) {
            scenicLimit = 6;
        }

        // 处理简介信息：有则更新，无则新增
        HomeIntro intro = homeContentMapper.selectIntroEntity();
        if (intro == null) {
            intro = new HomeIntro();
            intro.setCreateTime(now);
            intro.setUpdateTime(now);
            intro.setTitle(dto.getTitle());
            intro.setContent(dto.getContent());
            intro.setCoverUrl(dto.getCoverUrl());
            intro.setCoverType(coverType);
            intro.setScenicLimit(scenicLimit);
            int rows = homeContentMapper.insertIntro(intro);
            if (rows == 0) {
                throw new BusinessException("保存简介失败");
            }
        } else {
            intro.setTitle(dto.getTitle());
            intro.setContent(dto.getContent());
            intro.setCoverUrl(dto.getCoverUrl());
            intro.setCoverType(coverType);
            intro.setScenicLimit(scenicLimit);
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

        // 处理风景配置：逻辑删除旧数据，批量插入新数据
        homeContentMapper.logicalDeleteAllScenics(now);
        if (!CollectionUtils.isEmpty(dto.getScenics())) {
            List<HomeScenic> scenicEntities = new ArrayList<>();
            Set<Long> dedup = new HashSet<>();
            for (HomeScenicDTO scenicDTO : dto.getScenics()) {
                if (scenicDTO == null || scenicDTO.getScenicId() == null) {
                    continue;
                }
                if (!dedup.add(scenicDTO.getScenicId())) {
                    log.warn("【后台-保存首页配置】检测到重复的景区ID={}，已自动去重", scenicDTO.getScenicId());
                    continue;
                }
                HomeScenic scenic = new HomeScenic();
                scenic.setScenicId(scenicDTO.getScenicId());
                scenic.setSort(Objects.requireNonNullElse(scenicDTO.getSort(), 0));
                scenic.setIsEnabled(Boolean.TRUE.equals(scenicDTO.getEnabled()) ? 1 : 0);
                scenic.setIsDeleted(0);
                scenic.setCreateTime(now);
                scenic.setUpdateTime(now);
                scenicEntities.add(scenic);
            }
            if (scenicEntities.size() > scenicLimit) {
                scenicEntities = scenicEntities.subList(0, scenicLimit);
            }
            if (!scenicEntities.isEmpty()) {
                int inserted = homeContentMapper.batchInsertScenics(scenicEntities);
                if (inserted != scenicEntities.size()) {
                    log.warn("【后台-保存首页配置】风景插入数量与期望不一致，期望={}，实际={}", scenicEntities.size(), inserted);
                }
            }
        }
    }
}

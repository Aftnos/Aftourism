package aftnos.aftourismserver.portal.service.impl;

import aftnos.aftourismserver.admin.mapper.HomeContentMapper;
import aftnos.aftourismserver.admin.pojo.HomeIntro;
import aftnos.aftourismserver.portal.service.HomePortalService;
import aftnos.aftourismserver.portal.vo.HomePortalVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 门户首页展示实现层。
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class HomePortalServiceImpl implements HomePortalService {

    private final HomeContentMapper homeContentMapper;

    @Override
    public HomePortalVO loadHomeContent() {
        log.info("【门户-首页展示】开始聚合轮播与简介信息");
        HomePortalVO vo = new HomePortalVO();
        HomeIntro introEntity = homeContentMapper.selectIntroEntity();
        Integer scenicLimit = introEntity == null ? null : introEntity.getScenicLimit();
        if (scenicLimit == null || scenicLimit <= 0) {
            scenicLimit = 6;
        }
        vo.setBanners(homeContentMapper.selectEnabledBanners());
        vo.setIntro(homeContentMapper.selectPortalIntro());
        vo.setScenics(homeContentMapper.selectEnabledScenics(scenicLimit));
        vo.setScenicLimit(scenicLimit);
        return vo;
    }
}

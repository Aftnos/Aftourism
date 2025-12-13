package aftnos.aftourismserver.portal.service.impl;

import aftnos.aftourismserver.admin.mapper.HomeContentMapper;
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
        vo.setBanners(homeContentMapper.selectEnabledBanners());
        vo.setIntro(homeContentMapper.selectPortalIntro());
        return vo;
    }
}

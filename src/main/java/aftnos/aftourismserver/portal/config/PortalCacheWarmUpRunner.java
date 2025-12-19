package aftnos.aftourismserver.portal.config;

import aftnos.aftourismserver.portal.service.HomePortalService;
import aftnos.aftourismserver.portal.vo.HomePortalVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * 服务启动后预热首页缓存，保障首屏数据快速响应。
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class PortalCacheWarmUpRunner implements ApplicationRunner {

    private final HomePortalService homePortalService;

    @Override
    public void run(ApplicationArguments args) {
        try {
            HomePortalVO homePortalVO = homePortalService.loadHomeContent();
            int bannerSize = Optional.ofNullable(homePortalVO.getBanners()).map(list -> list.size()).orElse(0);
            int scenicSize = Optional.ofNullable(homePortalVO.getScenics()).map(list -> list.size()).orElse(0);
            log.info("【门户缓存预热】首页数据预热完成，轮播={} 条，景区卡片={} 条", bannerSize, scenicSize);
        } catch (Exception ex) {
            log.error("【门户缓存预热】监控告警：Redis 不可用或预热失败，已自动降级到数据库直查", ex);
        }
    }
}

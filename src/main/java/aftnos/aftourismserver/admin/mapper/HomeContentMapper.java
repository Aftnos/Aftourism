package aftnos.aftourismserver.admin.mapper;

import aftnos.aftourismserver.admin.pojo.HomeBanner;
import aftnos.aftourismserver.admin.pojo.HomeIntro;
import aftnos.aftourismserver.admin.vo.HomeBannerAdminVO;
import aftnos.aftourismserver.admin.vo.HomeIntroAdminVO;
import aftnos.aftourismserver.portal.vo.HomeBannerPortalVO;
import aftnos.aftourismserver.portal.vo.HomeIntroPortalVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 首页内容配置 Mapper。
 */
@Mapper
public interface HomeContentMapper {

    HomeIntroAdminVO selectIntro();

    HomeIntroPortalVO selectPortalIntro();

    HomeIntro selectIntroEntity();

    int insertIntro(HomeIntro intro);

    int updateIntro(HomeIntro intro);

    List<HomeBannerAdminVO> selectAllBanners();

    List<HomeBannerPortalVO> selectEnabledBanners();

    int logicalDeleteAllBanners(@Param("updateTime") LocalDateTime updateTime);

    int batchInsertBanners(@Param("banners") List<HomeBanner> banners);
}

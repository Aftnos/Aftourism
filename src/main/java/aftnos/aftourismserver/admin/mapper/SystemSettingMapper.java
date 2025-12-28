package aftnos.aftourismserver.admin.mapper;

import aftnos.aftourismserver.admin.pojo.SystemSetting;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 系统配置 Mapper。
 */
@Mapper
public interface SystemSettingMapper {

    /**
     * 根据配置键查询配置。
     *
     * @param settingKey 配置键
     * @return 配置实体
     */
    SystemSetting findByKey(@Param("settingKey") String settingKey);

    /**
     * 新增或更新配置。
     *
     * @param setting 配置实体
     * @return 影响行数
     */
    int upsert(SystemSetting setting);
}

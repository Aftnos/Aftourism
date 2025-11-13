package aftnos.aftourismserver.admin.service;

import aftnos.aftourismserver.admin.dto.RecycleQueryDTO;
import aftnos.aftourismserver.admin.enums.RecycleType;
import aftnos.aftourismserver.admin.vo.RecycleItemVO;
import com.github.pagehelper.PageInfo;

/**
 * 回收站业务接口
 */
public interface RecycleBinService {

    /**
     * 分页查询已删除数据
     */
    PageInfo<RecycleItemVO> pageDeletedItems(RecycleQueryDTO dto);

    /**
     * 恢复被删除的数据
     */
    void restoreItem(RecycleType type, Long id);

    /**
     * 彻底删除记录
     */
    void forceDeleteItem(RecycleType type, Long id);
}

package aftnos.aftourismserver.admin.service;

import aftnos.aftourismserver.admin.dto.AdminAccountCreateRequest;
import aftnos.aftourismserver.admin.dto.AdminAccountPageQuery;
import aftnos.aftourismserver.admin.dto.AdminAccountUpdateRequest;
import aftnos.aftourismserver.admin.vo.AdminAccountVO;
import com.github.pagehelper.PageInfo;

/**
 * 管理员账号管理服务。
 */
public interface AdminAccountService {

    /** 分页查询管理员列表 */
    PageInfo<AdminAccountVO> page(AdminAccountPageQuery query);

    /** 创建管理员 */
    Long create(AdminAccountCreateRequest request);

    /** 更新管理员信息 */
    void update(Long id, AdminAccountUpdateRequest request);

    /** 查询详情 */
    AdminAccountVO detail(Long id);

    /** 逻辑删除管理员 */
    void delete(Long id);
}

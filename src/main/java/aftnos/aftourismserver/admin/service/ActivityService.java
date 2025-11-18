package aftnos.aftourismserver.admin.service;

/**
 * 活动后台业务接口
 */
public interface ActivityService {

    /** 审核通过 */
    void approve(Long id);

    /** 审核驳回 */
    void reject(Long id, String reason);

    /** 上线活动 */
    void online(Long id);

    /** 下线活动 */
    void offline(Long id);

    /** 分页查询审核活动列表 */
    com.github.pagehelper.PageInfo<aftnos.aftourismserver.admin.vo.ActivityAuditItemVO> pageAudit(aftnos.aftourismserver.admin.dto.ActivityAuditPageQuery query);

    /** 查询审核详情 */
    aftnos.aftourismserver.admin.vo.ActivityAuditDetailVO detail(Long id);
}

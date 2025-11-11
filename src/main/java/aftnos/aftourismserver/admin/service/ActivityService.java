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
}

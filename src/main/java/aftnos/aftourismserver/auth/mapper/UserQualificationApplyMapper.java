package aftnos.aftourismserver.auth.mapper;

import aftnos.aftourismserver.auth.pojo.UserQualificationApply;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import aftnos.aftourismserver.admin.vo.QualificationApplyManageVO;

/**
 * 门户用户资质申请 Mapper。
 */
@Mapper
public interface UserQualificationApplyMapper {

    int insert(UserQualificationApply apply);

    UserQualificationApply findLatestByUserId(@Param("userId") Long userId);

    UserQualificationApply findById(@Param("id") Long id);

    List<UserQualificationApply> page(@Param("userName") String userName,
                                      @Param("status") Integer status);

    List<QualificationApplyManageVO> pageManage(@Param("userName") String userName,
                                                @Param("status") Integer status);

    QualificationApplyManageVO findManageDetail(@Param("id") Long id);

    int updateStatus(@Param("id") Long id,
                     @Param("status") Integer status,
                     @Param("auditRemark") String auditRemark);
}

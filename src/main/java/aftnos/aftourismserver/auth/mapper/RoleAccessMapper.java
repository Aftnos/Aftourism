package aftnos.aftourismserver.auth.mapper;

import aftnos.aftourismserver.auth.pojo.RoleAccess;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Collection;
import java.util.List;

/**
 * 角色权限关系表 Mapper。
 */
@Mapper
public interface RoleAccessMapper {

    /**
     * 根据角色编码集合查询权限列表。
     */
    List<RoleAccess> findByRoleCodes(@Param("roleCodes") Collection<String> roleCodes);

    /**
     * 查询指定角色的所有权限。
     */
    List<RoleAccess> findByRoleCode(@Param("roleCode") String roleCode);

    /**
     * 查询系统已存在的全部角色编码。
     */
    List<String> findAllRoleCodes();

    /**
     * 查询所有角色权限记录，便于聚合角色元数据。
     */
    List<RoleAccess> findAll();

    /**
     * 批量新增角色权限。
     */
    int batchInsert(@Param("records") List<RoleAccess> records);

    /**
     * 删除指定角色的全部权限定义。
     */
    int deleteByRoleCode(@Param("roleCode") String roleCode);
}

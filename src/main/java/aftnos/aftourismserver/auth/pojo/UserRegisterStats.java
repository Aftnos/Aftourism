package aftnos.aftourismserver.auth.pojo;

import lombok.Data;

import java.time.LocalDate;

/**
 * 用户注册统计实体，用于按天汇总新增用户数量
 */
@Data
public class UserRegisterStats {

    /** 统计日期 */
    private LocalDate statDate;

    /** 当日新增用户数量 */
    private Long total;
}

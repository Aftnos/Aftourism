package com.aftourism.portal.vo;

import lombok.Builder;
import lombok.Data;

/**
 * 用户信息返回对象。
 */
@Data
@Builder
public class UserProfileVO {

    private Long userId;
    private String username;
    private String nickname;
    private String email;
    private String avatar;
    private String token;
}

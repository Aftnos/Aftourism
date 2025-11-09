package com.aftourism.portal.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 景区展示对象。
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ScenicSpotVO {

    private Long id;
    private String name;
    private String imageUrl;
    private String level;
    private String address;
    private String intro;
}

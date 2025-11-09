package com.aftourism.portal.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 场馆展示对象。
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VenueVO {

    private Long id;
    private String name;
    private String imageUrl;
    private String category;
    private String address;
    private Integer isFree;
}

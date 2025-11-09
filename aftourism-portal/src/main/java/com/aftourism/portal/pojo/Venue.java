package com.aftourism.portal.pojo;

import com.aftourism.common.pojo.BaseEntity;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 场馆实体，对应 t_venue 表。
 */
@Data
public class Venue extends BaseEntity {

    private String name;
    private String imageUrl;
    private String category;
    private Integer isFree;
    private BigDecimal ticketPrice;
    private String address;
    private String openTime;
    private String description;
    private String phone;
    private String website;
    private Double longitude;
    private Double latitude;
    private Integer sort;
}

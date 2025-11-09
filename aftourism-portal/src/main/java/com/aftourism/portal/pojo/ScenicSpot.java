package com.aftourism.portal.pojo;

import com.aftourism.common.pojo.BaseEntity;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 景区实体，对应 t_scenic_spot 表。
 */
@Data
public class ScenicSpot extends BaseEntity {

    private String name;
    private String imageUrl;
    private String level;
    private BigDecimal ticketPrice;
    private String address;
    private String openTime;
    private String intro;
    private String phone;
    private String website;
    private Double longitude;
    private Double latitude;
    private Integer sort;
}

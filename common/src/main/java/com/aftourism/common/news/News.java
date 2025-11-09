package com.aftourism.common.news;

import com.aftourism.common.model.BaseEntity;
import java.time.LocalDateTime;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * News entity representing news table records.
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class News extends BaseEntity {

    private String title;
    private String content;
    private LocalDateTime publishTime;
    private Long authorId;
}

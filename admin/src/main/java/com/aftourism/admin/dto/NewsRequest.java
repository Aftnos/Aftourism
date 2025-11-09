package com.aftourism.admin.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;
import lombok.Data;

/**
 * DTO used for creating and updating news entries.
 */
@Data
public class NewsRequest {

    @NotBlank
    @Size(max = 255)
    private String title;

    @NotBlank
    private String content;

    private LocalDateTime publishTime;

    private Long authorId;
}

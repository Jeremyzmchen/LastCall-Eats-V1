package com.lastcalleats.review.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;

@Data
public class CreatePostRequest {

    @NotBlank(message = "Post content cannot be blank")
    @Size(max = 1000, message = "Post content cannot exceed 1000 characters")
    private String content;

    private Long merchantId; // optional

    /** Image URLs uploaded to MinIO beforehand; max 9. */
    @Size(max = 9, message = "Cannot attach more than 9 images")
    private List<String> imageUrls;
}

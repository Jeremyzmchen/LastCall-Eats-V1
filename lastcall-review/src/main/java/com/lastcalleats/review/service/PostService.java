package com.lastcalleats.review.service;

import com.lastcalleats.review.dto.CreatePostRequest;
import com.lastcalleats.review.dto.PostResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PostService {

    PostResponse createPost(Long userId, CreatePostRequest request);

    Page<PostResponse> listAllPosts(Pageable pageable);

    Page<PostResponse> listPostsByUser(Long userId, Pageable pageable);

    Page<PostResponse> listPostsByMerchant(Long merchantId, Pageable pageable);

    PostResponse getPost(Long postId);

    /** Only the post owner may delete. */
    void deletePost(Long userId, Long postId);
}

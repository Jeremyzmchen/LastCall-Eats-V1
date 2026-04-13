package com.lastcalleats.review.controller;

import com.lastcalleats.common.response.ApiResponse;
import com.lastcalleats.common.response.PageResult;
import com.lastcalleats.review.dto.CreatePostRequest;
import com.lastcalleats.review.dto.PostResponse;
import com.lastcalleats.review.service.PostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

/**
 * 处理社区帖子模块的 HTTP 请求。
 * 提供发帖、查询广场、查询个人/商家帖子、帖子详情和删帖接口。
 */
@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;
    // 发帖
    @PostMapping
    public ApiResponse<PostResponse> createPost(
            @AuthenticationPrincipal Long userId,
            @Valid @RequestBody CreatePostRequest request) {
        return ApiResponse.success(postService.createPost(userId, request));
    }
    // 获取所有帖子
    @GetMapping
    public ApiResponse<PageResult<PostResponse>> listAllPosts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<PostResponse> result = postService.listAllPosts(pageable);
        return ApiResponse.success(PageResult.of(result));
    }
    // 获取用户帖子
    @GetMapping("/user/{userId}")
    public ApiResponse<PageResult<PostResponse>> listPostsByUser(
            @PathVariable Long userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<PostResponse> result = postService.listPostsByUser(userId, pageable);
        return ApiResponse.success(PageResult.of(result));
    }
    // 获取商家帖子
    @GetMapping("/merchant/{merchantId}")
    public ApiResponse<PageResult<PostResponse>> listPostsByMerchant(
            @PathVariable Long merchantId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<PostResponse> result = postService.listPostsByMerchant(merchantId, pageable);
        return ApiResponse.success(PageResult.of(result));
    }
    // 获取特定顶帖子
    @GetMapping("/{postId}")
    public ApiResponse<PostResponse> getPost(
            @PathVariable Long postId) {
        return ApiResponse.success(postService.getPost(postId));
    }
    // 删帖
    @DeleteMapping("/{postId}")
    public ApiResponse<Void> deletePost(
            @AuthenticationPrincipal Long userId,
            @PathVariable Long postId) {
        postService.deletePost(userId, postId);
        return ApiResponse.success();
    }
}

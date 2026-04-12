package com.lastcalleats.review.service;

import com.lastcalleats.review.dto.CreatePostRequest;
import com.lastcalleats.review.dto.PostResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * 帖子服务接口，定义社区帖子模块的所有业务操作。
 * Controller 只依赖此接口，不感知具体实现，便于测试和扩展。
 */
public interface PostService {

    /**
     * 发帖
     *
     * @param userId  当前登录用户的 ID
     * @param request 帖子内容、可选商家 ID 和图片列表
     * @return 创建成功后的帖子详情
     */
    PostResponse createPost(Long userId, CreatePostRequest request);

    /**
     * 返回全部可见帖子，按时间倒序分页。
     *
     * @param pageable 分页参数
     * @return 帖子分页结果
     */
    Page<PostResponse> listAllPosts(Pageable pageable);

    /**
     * 获取某用户发布的帖子列表，用于个人主页展示。
     *
     * @param userId   目标用户 ID
     * @param pageable 分页参数
     * @return 帖子分页结果
     */
    Page<PostResponse> listPostsByUser(Long userId, Pageable pageable);

    /**
     * 获取某商家下的帖子列表，用于商家主页展示。
     *
     * @param merchantId 目标商家 ID
     * @param pageable   分页参数
     * @return 帖子分页结果
     */
    Page<PostResponse> listPostsByMerchant(Long merchantId, Pageable pageable);

    /**
     * 获取单条帖子详情。
     *
     * @param postId 帖子 ID
     * @return 帖子详情
     */
    PostResponse getPost(Long postId);

    /**
     * 删除帖子，只有帖子作者本人可以删除。
     *
     * @param userId 当前登录用户的 ID
     * @param postId 待删除的帖子 ID
     */
    void deletePost(Long userId, Long postId);
}

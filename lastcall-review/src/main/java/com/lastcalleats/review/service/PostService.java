package com.lastcalleats.review.service;

import com.lastcalleats.review.dto.CreatePostRequest;
import com.lastcalleats.review.dto.PostResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service contract for community post operations.
 * Posts are user-generated content attached optionally to a merchant; they
 * support likes and comments maintained by separate services.
 */
public interface PostService {

    /**
     * Creates a new post on behalf of the given user and persists it.
     *
     * @param userId  the authenticated author
     * @param request post content and optional merchant reference
     * @return the persisted post with resolved nickname and merchant name
     */
    PostResponse createPost(Long userId, CreatePostRequest request);

    /**
     * Returns a paginated list of all visible posts, newest first.
     *
     * @param pageable pagination and sorting parameters
     * @return a page of post responses
     */
    Page<PostResponse> listAllPosts(Pageable pageable);

    /**
     * Returns visible posts authored by the specified user, newest first.
     *
     * @param userId   the target user's ID
     * @param pageable pagination parameters
     * @return a page of the user's posts
     */
    Page<PostResponse> listPostsByUser(Long userId, Pageable pageable);

    /**
     * Returns visible posts tagged to the specified merchant, newest first.
     *
     * @param merchantId the merchant's ID
     * @param pageable   pagination parameters
     * @return a page of posts for that merchant
     */
    Page<PostResponse> listPostsByMerchant(Long merchantId, Pageable pageable);

    /**
     * Fetches a single visible post by its ID.
     *
     * @param postId the post's primary key
     * @return the matching post response
     * @throws com.lastcalleats.common.exception.BusinessException with
     *         {@code POST_NOT_FOUND} if the post does not exist or is hidden
     */
    PostResponse getPost(Long postId);

    /**
     * Deletes the specified post. Only the post owner may delete.
     *
     * @param userId the authenticated requester
     * @param postId the post to remove
     * @throws com.lastcalleats.common.exception.BusinessException with
     *         {@code POST_FORBIDDEN} if the requester is not the owner
     */
    void deletePost(Long userId, Long postId);
}

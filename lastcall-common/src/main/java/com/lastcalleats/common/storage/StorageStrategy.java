package com.lastcalleats.common.storage;

import org.springframework.web.multipart.MultipartFile;

/**
 * 文件存储策略接口（Strategy Pattern）。
 * 定义文件上传的统一规范，不同的实现类代表不同的存储方式。
 * 目前 V1 只有 LocalStorageStrategy（本地存储），
 * 未来可以扩展 MinioStorageStrategy（对象存储）、S3StorageStrategy（云存储）等。
 */
public interface StorageStrategy {

    /**
     * 上传文件，返回可访问的 URL。
     *
     * @param file   前端上传的文件
     * @param folder 存储的子目录（如 "avatars"、"covers"）
     * @return 文件的访问 URL
     */
    String upload(MultipartFile file, String folder);
}

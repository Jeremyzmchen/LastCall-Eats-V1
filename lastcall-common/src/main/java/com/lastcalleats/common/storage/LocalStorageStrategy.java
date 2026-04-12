package com.lastcalleats.common.storage;

import com.lastcalleats.common.exception.BusinessException;
import com.lastcalleats.common.exception.ErrorCode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

/**
 * 本地文件存储策略，将文件保存到服务器本地磁盘。
 * 这是 StorageStrategy 的一种实现（Strategy Pattern）。
 * V1 使用本地存储，未来可替换为 MinIO 或 S3 而无需改动业务代码。
 */
@Component
public class LocalStorageStrategy implements StorageStrategy {

    /**
     * 本地存储根目录，从 application.yml 读取，默认为 ./uploads
     */
    @Value("${storage.local.base-path:./uploads}")
    private String basePath;

    /**
     * 文件访问的 URL 前缀，从 application.yml 读取，默认为 /uploads
     */
    @Value("${storage.local.url-prefix:/uploads}")
    private String urlPrefix;

    @Override
    public String upload(MultipartFile file, String folder) {
        // 1. 生成唯一文件名，避免重名覆盖
        String originalFilename = file.getOriginalFilename();
        String extension = "";
        if (originalFilename != null && originalFilename.contains(".")) {
            extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        }
        String newFilename = UUID.randomUUID() + extension;

        // 2. 拼接完整的存储路径：basePath/folder/文件名
        Path targetDir = Paths.get(basePath, folder);
        Path targetFile = targetDir.resolve(newFilename);

        try {
            // 3. 如果目录不存在就创建
            Files.createDirectories(targetDir);
            // 4. 把文件内容写入磁盘
            file.transferTo(targetFile.toFile());
        } catch (IOException e) {
            throw new BusinessException(ErrorCode.INTERNAL_ERROR, "文件上传失败");
        }

        // 5. 返回访问 URL：/uploads/folder/文件名
        return urlPrefix + "/" + folder + "/" + newFilename;
    }
}

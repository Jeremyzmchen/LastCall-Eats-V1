package com.lastcalleats.common.storage;

import org.springframework.web.multipart.MultipartFile;

/** Storage strategy interface. Implementations: local disk, MinIO, S3, etc. */
public interface StorageStrategy {

    /** Uploads a file to the given sub-folder and returns its access URL. */
    String upload(MultipartFile file, String folder);
}

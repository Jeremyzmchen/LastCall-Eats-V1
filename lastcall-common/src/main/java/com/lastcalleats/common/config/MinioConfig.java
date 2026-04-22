package com.lastcalleats.common.config;

import io.minio.MinioClient;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Binds MinIO connection properties from {@code app.minio.*} and exposes a
 * configured {@link MinioClient} bean for file upload operations.
 * Currently declared but not injected by active code; the project uses
 * {@link com.lastcalleats.common.storage.LocalStorageStrategy} in development.
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "app.minio")
public class MinioConfig {

    private String endpoint;
    private String accessKey;
    private String secretKey;
    private String bucketName;

    @Bean
    public MinioClient minioClient() {
        return MinioClient.builder()
                .endpoint(endpoint)
                .credentials(accessKey, secretKey)
                .build();
    }
}

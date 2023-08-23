package com.hg.minio.config;

import com.hg.minio.common.MinioProp;
import io.minio.MinioClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * minio client核心配置
 *
 * @author hougen
 * @since 2023/08/23 20:57
 */
@Configuration
@EnableConfigurationProperties(MinioProp.class)
public class MinioConfig {

    private MinioProp minioProp;

    @Autowired
    public void setMinioProp(MinioProp minioProp) {
        this.minioProp = minioProp;
    }

    /**
     * 获取 MinioClient
     *
     * @return {@link MinioClient}
     */
    @Bean
    public MinioClient minioClient() {
        return MinioClient.builder()
                .credentials(minioProp.getAccessKey(), minioProp.getSecretKey())
                .endpoint(minioProp.getEndpoint())
                .build();
    }
}

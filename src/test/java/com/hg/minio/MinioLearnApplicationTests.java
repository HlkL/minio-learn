package com.hg.minio;

import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.errors.*;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@Slf4j
@SpringBootTest
class MinioLearnApplicationTests {
    private MinioClient minioClient;

    @Autowired
    public void setMinioClient(MinioClient minioClient) {
        this.minioClient = minioClient;
    }

    @Test
    void contextLoads() {
    }

    @Test
    void minioLocalFileUploadTest() throws Exception {
        String bucket = "learn";
        String url = "http://43.139.96.22:9001";

        String filename = "60fb3a499a7b55f1658f2c4c72cc2bc1.jpeg";
        String filePath = "/Users/hougen/Pictures/60fb3a499a7b55f1658f2c4c72cc2bc1.jpeg";
        FileInputStream inputStream = new FileInputStream(filePath);

        // 上传文件
        PutObjectArgs objectArgs = PutObjectArgs.builder()
                .object(filename)
                // 内容类型 https://blog.csdn.net/qq_36551991/article/details/109499487
                .contentType("image/jpeg")
                .bucket(bucket)
                .stream(inputStream, inputStream.available(), -1)
                .build();
        minioClient.putObject(objectArgs);

        // 访问路径
        log.info(url + "/" + bucket + "/" + filename);
    }

}

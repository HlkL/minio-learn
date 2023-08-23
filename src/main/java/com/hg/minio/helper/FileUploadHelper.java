package com.hg.minio.helper;

import com.hg.minio.common.MinioProp;
import io.minio.*;
import io.minio.errors.*;
import io.minio.http.Method;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Optional;

/**
 * 文件上传
 *
 * @author hougen
 * @since 2023/08/23 21:23
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class FileUploadHelper {
    /**
     * 文件后缀分割符
     */
    private final String DELIMITER = ".";


    private final MinioClient minioClient;
    private final MinioProp minioProp;

    public String fileUpload(MultipartFile file) {
        if (file.isEmpty()) {
            return "";
        }
        try {
            String filename = Optional.ofNullable(file.getOriginalFilename())
                    .map(originalFilename ->
                            minioProp.getBucket() + System.currentTimeMillis()
                                    + originalFilename.substring(originalFilename.lastIndexOf(DELIMITER))
                    ).orElseThrow();

            PutObjectArgs objectArgs = buildPutObjectArgs(file, filename);
            minioClient.putObject(objectArgs);
            return this.generateFileRequestUrl(minioProp.getBucket(), filename, Method.GET, null);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return "";
    }

    /**
     * 生成文件访问url
     *
     * @param bucketName 桶名称
     * @param objectName 文件名称
     * @param method     请求方式
     * @param expires    失效时间（以秒为单位），默认是7天，不得大于七天
     * @return 文件请求url
     */
    private String generateFileRequestUrl(String bucketName, String objectName, Method method, Integer expires) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        BucketExistsArgs bucketArgs = BucketExistsArgs.builder().bucket(bucketName).build();
        boolean bucketExists = minioClient.bucketExists(bucketArgs);
        if (bucketExists) {
            try {
                GetPresignedObjectUrlArgs urlArgs = GetPresignedObjectUrlArgs.builder()
                        .method(method)
                        .bucket(bucketName)
                        .object(objectName)
                        .expiry(Optional.ofNullable(expires).orElse(604800))
                        .build();
                return minioClient.getPresignedObjectUrl(urlArgs);
            } catch (Exception e) {
                log.error(e.getMessage());
            }
        }

        return "";
    }


    private PutObjectArgs buildPutObjectArgs(MultipartFile file, String filename) throws IOException {
        InputStream inputStream = file.getInputStream();
        return PutObjectArgs.builder()
                .object(filename)
                .bucket(minioProp.getBucket())
                .contentType(file.getContentType())
                .stream(inputStream, inputStream.available(), -1)
                .build();
    }
}

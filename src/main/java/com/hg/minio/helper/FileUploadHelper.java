package com.hg.minio.helper;

import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;
import com.hg.minio.common.MinioProp;
import com.hg.minio.common.MinioStorageClient;
import com.hg.minio.model.bo.MergeMultipartFileBO;
import com.hg.minio.model.bo.MultipartUploadIdBO;
import com.hg.minio.model.bo.PartListBO;
import io.minio.*;
import io.minio.http.Method;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

/**
 * 文件上传
 *
 * @author hougen
 * @since 2023/08/23 21:23
 */
@Slf4j
@Component
@Data
@SuppressWarnings("unused")
@RequiredArgsConstructor
public class FileUploadHelper {
    /**
     * 文件后缀分割符
     */
    private final String DELIMITER = ".";
    private final MinioClient minioClient;
    private final MinioProp minioProp;
    private final MinioStorageClient minioStorageClient;

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
            return this.getRequestUrl(minioProp.getBucket(), filename, Method.GET);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return "";
    }

    public String getRequestUrl(String bucketName, String objectName, Method method, Map<String, String> map) {

        return this.generateFileRequestUrl(bucketName, objectName, method,
                null, map, null);
    }


    public String getRequestUrl(String bucketName, String objectName, Method method) {
        return this.getRequestUrl(bucketName, objectName, method, null);
    }

    /**
     * 生成文件访问url
     *
     * @param bucketName         桶名称
     * @param objectName         文件名称
     * @param method             请求方式
     * @param expires            失效时间（以秒为单位），默认是7天，不得大于七天
     * @param queryParamMap      查询参数
     * @param queryParamMultiMap 查询参数
     * @return 文件请求url
     */
    @SneakyThrows
    private String generateFileRequestUrl(String bucketName, String objectName,
                                          Method method, Integer expires,
                                          Map<String, String> queryParamMap,
                                          Multimap<String, String> queryParamMultiMap) {

        final int defaultExpires = 604800;
        BucketExistsArgs bucketArgs = BucketExistsArgs.builder().bucket(bucketName).build();
        boolean bucketExists = minioClient.bucketExists(bucketArgs);
        if (bucketExists) {
            try {
                GetPresignedObjectUrlArgs urlArgs = GetPresignedObjectUrlArgs.builder()
                        .method(method)
                        .bucket(bucketName)
                        .object(objectName)
                        .expiry(Optional.ofNullable(expires).filter(ex -> ex >= defaultExpires).orElse(defaultExpires))
                        .extraQueryParams(Optional.ofNullable(queryParamMap)
                                .filter(map -> map.size() > 0)
                                .orElse(Collections.emptyMap()))
                        .extraHeaders(Optional.ofNullable(queryParamMultiMap)
                                .filter(map -> map.size() > 0)
                                .orElse(Multimaps.forMap(Collections.emptyMap())))
                        .build();
                return minioClient.getPresignedObjectUrl(urlArgs);
            } catch (Exception e) {
                log.error(e.getMessage());
            }
        }

        return "";
    }


    /**
     * 获取分片上传id
     *
     * @param req {@link MultipartUploadIdBO}
     * @return uploadId
     */
    @SneakyThrows
    public String generateUploadId(MultipartUploadIdBO req) {
        CompletableFuture<CreateMultipartUploadResponse> future = minioStorageClient
                .createMultipartUploadAsync(
                        req.getBucketName(), req.getRegion(),
                        req.getObjectName(), req.getHeaders(), req.getExtraQueryParams()
                );
        CreateMultipartUploadResponse response = future.get();
        return response.result().uploadId();
    }


    /**
     * 合并分片
     *
     * @param req {@link MergeMultipartFileBO}
     * @return {@link ObjectWriteResponse}
     */
    @SneakyThrows
    public ObjectWriteResponse mergeMultipartFile(MergeMultipartFileBO req) {
        CompletableFuture<ObjectWriteResponse> future = minioStorageClient
                .completeMultipartUploadAsync(
                        req.getBucketName(), req.getRegion(), req.getObjectName(),
                        req.getUploadId(), req.getParts(), req.getExtraHeaders(), req.getExtraQueryParams()
                );

        return future.get();
    }


    /**
     * 分片列表
     *
     * @param req {@link PartListBO}
     * @return {@link ListPartsResponse}
     */
    @SneakyThrows
    public ListPartsResponse partList(PartListBO req) {
        return minioStorageClient.listPartsAsync(
                req.getBucketName(), req.getRegion(), req.getObjectName(), req.getMaxParts(),
                req.getPartNumberMarker(), req.getUploadId(), req.getExtraHeaders(),
                req.getExtraQueryParams()
        ).get();
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

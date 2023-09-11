package com.hg.minio.common;

import com.google.common.collect.Multimap;
import io.minio.CreateMultipartUploadResponse;
import io.minio.ListPartsResponse;
import io.minio.MinioAsyncClient;
import io.minio.ObjectWriteResponse;
import io.minio.messages.Part;
import lombok.SneakyThrows;

import java.util.concurrent.CompletableFuture;

/**
 * 分片上传minio client
 *
 * @author hougen
 * @since 2023/08/24 23:22
 */
public class MinioStorageClient extends MinioAsyncClient {


    public MinioStorageClient(MinioAsyncClient client) {
        super(client);
    }

    /**
     * 创建分片上传请求
     *
     * @param bucketName       存储桶
     * @param region           区域
     * @param objectName       对象名
     * @param headers          消息头
     * @param extraQueryParams 额外查询参数
     */
    @SneakyThrows
    @Override
    public CompletableFuture<CreateMultipartUploadResponse> createMultipartUploadAsync(String bucketName,
                                                                                       String region,
                                                                                       String objectName,
                                                                                       Multimap<String, String> headers,
                                                                                       Multimap<String, String> extraQueryParams) {

        return super.createMultipartUploadAsync(bucketName, region, objectName, headers, extraQueryParams);
    }


    /**
     * 完成分片上传，执行合并文件
     *
     * @param bucketName       存储桶
     * @param region           区域
     * @param objectName       对象名
     * @param uploadId         上传ID
     * @param parts            分片
     * @param extraHeaders     额外消息头
     * @param extraQueryParams 额外查询参数
     */
    @Override
    @SneakyThrows
    public CompletableFuture<ObjectWriteResponse> completeMultipartUploadAsync(String bucketName,
                                                                               String region,
                                                                               String objectName,
                                                                               String uploadId,
                                                                               Part[] parts,
                                                                               Multimap<String, String> extraHeaders,
                                                                               Multimap<String, String> extraQueryParams) {

        return super.completeMultipartUploadAsync(bucketName, region, objectName, uploadId, parts, extraHeaders, extraQueryParams);
    }

    /**
     * 查询分片数据
     *
     * @param bucketName       存储桶
     * @param region           区域
     * @param objectName       对象名
     * @param uploadId         上传ID
     * @param extraHeaders     额外消息头
     * @param extraQueryParams 额外查询参数
     */
    @Override
    @SneakyThrows
    public CompletableFuture<ListPartsResponse> listPartsAsync(String bucketName, String region,
                                                               String objectName, Integer maxParts,
                                                               Integer partNumberMarker,
                                                               String uploadId,
                                                               Multimap<String, String> extraHeaders,
                                                               Multimap<String, String> extraQueryParams) {

        return super.listPartsAsync(bucketName, region, objectName, maxParts, partNumberMarker, uploadId, extraHeaders, extraQueryParams);
    }

}

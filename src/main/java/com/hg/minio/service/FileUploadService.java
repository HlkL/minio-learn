package com.hg.minio.service;

import com.hg.minio.model.request.CompleteMultipartUploadRequest;
import com.hg.minio.model.request.MultipartUploadCreateRequest;
import com.hg.minio.model.response.FileUploadResponse;
import com.hg.minio.model.response.MultipartUploadCreateResponse;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author hougen
 * @since 2023/08/27 21:01
 */
public interface FileUploadService {

    /**
     * 文件上传
     *
     * @param file file
     * @return url
     */
    String upload(MultipartFile file);

    /**
     * 创建分片上传
     *
     * @param request {@link MultipartUploadCreateRequest}
     * @return {@link MultipartUploadCreateResponse}
     */
    MultipartUploadCreateResponse createMultipartUpload(MultipartUploadCreateRequest request);

    /**
     * 完成文件分片上传
     *
     * @param request req
     * @return resp
     */
    FileUploadResponse completeMultipartUpload(CompleteMultipartUploadRequest request);
}

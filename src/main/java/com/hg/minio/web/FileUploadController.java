package com.hg.minio.web;

import com.hg.minio.model.request.CompleteMultipartUploadRequest;
import com.hg.minio.model.request.MultipartUploadCreateRequest;
import com.hg.minio.model.response.FileUploadResponse;
import com.hg.minio.model.response.MultipartUploadCreateResponse;
import com.hg.minio.service.FileUploadService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author hougen
 * @since 2023/08/23 21:09
 */
@RequestMapping("/file")
@RestController
@CrossOrigin
@RequiredArgsConstructor
public class FileUploadController {
    private final FileUploadService fileUploadService;

    /**
     * 文件上传
     *
     * @param file 文件
     * @return 文件访问链接
     */
    @PostMapping
    public String fileUpload(@RequestPart("file") MultipartFile file) {
        return fileUploadService.upload(file);
    }


    /**
     * 创建文件分片
     *
     * @param request req
     * @return res
     */
    @PostMapping("/create")
    public MultipartUploadCreateResponse createMultipartUpload(@RequestBody MultipartUploadCreateRequest request) {
        return fileUploadService.createMultipartUpload(request);
    }

    @PostMapping("/complete")
    public FileUploadResponse completeFileUpload(@RequestBody CompleteMultipartUploadRequest request) {
        return fileUploadService.completeMultipartUpload(request);
    }
}

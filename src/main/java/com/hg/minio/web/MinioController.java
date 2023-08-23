package com.hg.minio.web;

import com.hg.minio.helper.FileUploadHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author hougen
 * @since 2023/08/23 21:09
 */
@RequestMapping("/minio")
@RestController
@RequiredArgsConstructor
public class MinioController {
    private final FileUploadHelper fileUploadHelper;

    /**
     * 文件上传
     *
     * @param file 文件
     * @return 文件访问链接
     */
    @PostMapping
    public String multipartFileUpload(@RequestPart("file") MultipartFile file) {
        return fileUploadHelper.fileUpload(file);
    }

}

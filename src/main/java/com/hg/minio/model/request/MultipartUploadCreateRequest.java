package com.hg.minio.model.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

/**
 * 创建分片上传请求
 *
 * @author hougen
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MultipartUploadCreateRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = 2898257474959116046L;
    /**
     * 文件名称
     */
    private String fileName;

    /**
     * 分片个数
     */
    private Integer chunkSize;

}
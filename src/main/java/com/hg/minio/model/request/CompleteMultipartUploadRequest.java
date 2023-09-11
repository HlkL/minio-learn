package com.hg.minio.model.request;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * @author hougen
 */
@Data
public class CompleteMultipartUploadRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = 1393628734965818939L;
    /**
     * 文件名称
     */
    private String fileName;

    /**
     * 上传编号
     */
    private String uploadId;

    /**
     * 分片数量
     */
    private Integer chunkSize;


    /**
     * 文件大小
     */
    private Integer fileSize;

    /**
     * 文件类型
     */
    private String contentType;

    /**
     * 密码
     */
    private String pass;

    /**
     * 超时时间
     */
    private Integer expire;

    /**
     * 最大下载数
     */
    private Integer maxGetCount;


}
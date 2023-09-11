package com.hg.minio.model.bo;

import com.google.common.collect.Multimap;
import io.minio.messages.Part;
import lombok.Builder;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 合并上传文件
 *
 * @author hougen
 * @since 2023/08/27 20:26
 */
@Data
@Builder
public class MergeMultipartFileBO implements Serializable {

    @Serial
    private static final long serialVersionUID = -3029500053571925647L;

    private String bucketName;
    private String region;
    private String objectName;
    private String uploadId;
    private Part[] parts;
    private Multimap<String, String> extraHeaders;
    private Multimap<String, String> extraQueryParams;
}

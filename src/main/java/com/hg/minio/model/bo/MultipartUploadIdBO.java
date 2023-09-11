package com.hg.minio.model.bo;

import com.google.common.collect.Multimap;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

/**
 * 分片上传id获取请求vo
 *
 * @author hougen
 * @since 2023/08/27 20:16
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MultipartUploadIdBO implements Serializable {

    @Serial
    private static final long serialVersionUID = 7774842953615653795L;

    private String bucketName;
    private String region;
    private String objectName;
    private Multimap<String, String> headers;
    private Multimap<String, String> extraQueryParams;
}

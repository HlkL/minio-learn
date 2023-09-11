package com.hg.minio.model.bo;

import com.google.common.collect.Multimap;
import lombok.Builder;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 分片列表
 *
 * @author hougen
 * @since 2023/08/27 20:37
 */
@Data
@Builder
public class PartListBO implements Serializable {
    @Serial
    private static final long serialVersionUID = -4400669539031272777L;

    private String bucketName;
    private String region;
    private String objectName;
    private Integer maxParts;
    private Integer partNumberMarker;
    private String uploadId;
    private Multimap<String, String> extraHeaders;
    private Multimap<String, String> extraQueryParams;
}

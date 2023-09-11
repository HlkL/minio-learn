package com.hg.minio.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 分片上传创建响应类
 *
 * @author hougen
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MultipartUploadCreateResponse {

    /**
     * 上传编号
     */
    private String uploadId;

    /**
     * 分片信息
     */
    private List<UploadCreateItem> chunks;


    @Data
    @Builder
    public static class UploadCreateItem {
        /**
         * 分片编号
         */
        private Integer partNumber;

        /**
         * 上传地址
         */
        private String uploadUrl;
    }

}
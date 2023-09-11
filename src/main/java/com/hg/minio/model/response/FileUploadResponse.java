package com.hg.minio.model.response;

import lombok.Builder;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * @author hougen
 */
@Data
@Builder
public class FileUploadResponse implements Serializable {

    @Serial
    private static final long serialVersionUID = -5874500763017789508L;
    private String realName;

    private String uploadName;

    private String url;

    private long size;

    private String bucket;


}
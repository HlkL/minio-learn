package com.hg.minio.service.impl;

import com.hg.minio.helper.FileUploadHelper;
import com.hg.minio.model.bo.MergeMultipartFileBO;
import com.hg.minio.model.bo.MultipartUploadIdBO;
import com.hg.minio.model.bo.PartListBO;
import com.hg.minio.model.request.CompleteMultipartUploadRequest;
import com.hg.minio.model.request.MultipartUploadCreateRequest;
import com.hg.minio.model.response.FileUploadResponse;
import com.hg.minio.model.response.MultipartUploadCreateResponse;
import com.hg.minio.service.FileUploadService;
import io.minio.ListPartsResponse;
import io.minio.ObjectWriteResponse;
import io.minio.http.Method;
import io.minio.messages.Part;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author hougen
 * @since 2023/08/27 21:01
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class FileUploadServiceImpl implements FileUploadService {
    private final FileUploadHelper fileUploadHelper;


    @Override
    public String upload(MultipartFile file) {
        return fileUploadHelper.fileUpload(file);
    }

    @Override
    public MultipartUploadCreateResponse createMultipartUpload(MultipartUploadCreateRequest request) {
        MultipartUploadIdBO bo = MultipartUploadIdBO.builder()
                .bucketName(fileUploadHelper.getMinioProp().getBucket())
                .objectName(request.getFileName())
                .build();

        final String uploadId = fileUploadHelper.generateUploadId(bo);
        MultipartUploadCreateResponse response = new MultipartUploadCreateResponse();
        response.setUploadId(uploadId);

        Map<String, String> reqParams = new HashMap<>();
        reqParams.put("uploadId", uploadId);
        List<MultipartUploadCreateResponse.UploadCreateItem> list = new ArrayList<>();
        for (int i = 0; i <= request.getChunkSize(); i++) {
            reqParams.put("partNumber", String.valueOf(i));
            // url
            String requestUrl = fileUploadHelper.getRequestUrl(fileUploadHelper.getMinioProp().getBucket(),
                    request.getFileName(), Method.PUT, reqParams);
            MultipartUploadCreateResponse.UploadCreateItem item = MultipartUploadCreateResponse
                    .UploadCreateItem.builder()
                    .partNumber(i)
                    .uploadUrl(requestUrl)
                    .build();

            list.add(item);
        }
        response.setChunks(list);

        return response;
    }

    @Override
    public FileUploadResponse completeMultipartUpload(CompleteMultipartUploadRequest request) {
        PartListBO bo = PartListBO.builder()
                .bucketName(fileUploadHelper.getMinioProp().getBucket())
                .objectName(request.getFileName())
                .uploadId(request.getUploadId())
                .maxParts(request.getChunkSize() + 10)
                .partNumberMarker(0)
                .build();

        final ListPartsResponse listParts = fileUploadHelper.partList(bo);
        MergeMultipartFileBO multipartFileBO = MergeMultipartFileBO.builder()
                .bucketName(fileUploadHelper.getMinioProp().getBucket())
                .objectName(request.getFileName())
                .uploadId(request.getUploadId())
                .parts(listParts.result().partList().toArray(new Part[]{}))
                .build();

        final ObjectWriteResponse writeResponse = fileUploadHelper.mergeMultipartFile(multipartFileBO);
        return FileUploadResponse.builder()
                .url(fileUploadHelper.getMinioProp().getEndpoint() + "/"
                        + fileUploadHelper.getMinioProp().getBucket() + "/"
                        + request.getFileName())
                .build();

    }
}

package com.dp.ishare.entry;

import com.dp.ishare.constants.ResponseMsg;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

public class UploadResponse {
    private String fileDownloadUri;
    private String extractCode;
    private Long size;

    public UploadResponse(String fileDownloadUri, String extractCode, Long size) {
        this.fileDownloadUri = fileDownloadUri;
        this.extractCode = extractCode;
        this.size = size;
    }

    public String getFileDownloadUri() {
        return fileDownloadUri;
    }

    public void setFileDownloadUri(String fileDownloadUri) {
        this.fileDownloadUri = fileDownloadUri;
    }

    public String getExtractCode() {
        return extractCode;
    }

    public void setExtractCode(String extractCode) {
        this.extractCode = extractCode;
    }

    public Long getSize() {
        return size;
    }

    public void setSize(Long size) {
        this.size = size;
    }

    @Override
    public String toString() {
        return "{" +
                "fileDownloadUri='" + fileDownloadUri + '\'' +
                ", extractCode='" + extractCode + '\'' +
                ", size=" + size +
                '}';
    }
}

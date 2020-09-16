package com.dp.ishare.entry;

import com.dp.ishare.constants.ResponseMsg;

public class UploadResponse {
    private Integer respCode;
    private String respMsg;
    private String fileDownloadUri;
    private String extractCode;
    private Long size;

    public UploadResponse(ResponseMsg msg, String fileDownloadUri, String extractCode, Long size) {
        this.respCode = msg.getCode();
        this.respMsg = msg.getMsg();
        this.fileDownloadUri = fileDownloadUri;
        this.extractCode = extractCode;
        this.size = size;
    }

    public UploadResponse (ResponseMsg msg) {
        this.respCode = msg.getCode();
        this.respMsg = msg.getMsg();
        this.fileDownloadUri = null;
        this.extractCode = null;
        this.size = null;
    }

    public Integer getRespCode() {
        return respCode;
    }

    public void setRespCode(Integer respCode) {
        this.respCode = respCode;
    }

    public String getRespMsg() {
        return respMsg;
    }

    public void setRespMsg(String respMsg) {
        this.respMsg = respMsg;
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
}

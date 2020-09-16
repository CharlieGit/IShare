package com.dp.ishare.entry;

import org.springframework.data.annotation.PersistenceConstructor;

import java.util.Date;

public class FileInfo {
    private String fileId;
    private String fileName;
    private String fileType;
    private Long fileSize;
    private String userId;
    private Date uploadDate;
    private Date expireDate;
    private String encryptCode;

    @PersistenceConstructor
    public FileInfo(String fileId, String fileName, String fileType, Long fileSize, String userId, Date uploadDate, Date expireDate, String encryptCode) {
        this.fileId = fileId;
        this.fileName = fileName;
        this.fileType = fileType;
        this.fileSize = fileSize;
        this.userId = userId;
        this.uploadDate = uploadDate;
        this.expireDate = expireDate;
        this.encryptCode = encryptCode;
    }

    public String getFileId() {
        return fileId;
    }

    public void setFileId(String fileId) {
        this.fileId = fileId;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public Long getFileSize() {
        return fileSize;
    }

    public void setFileSize(Long fileSize) {
        this.fileSize = fileSize;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Date getUploadDate() {
        return uploadDate;
    }

    public void setUploadDate(Date uploadDate) {
        this.uploadDate = uploadDate;
    }

    public Date getExpireDate() {
        return expireDate;
    }

    public void setExpireDate(Date expireDate) {
        this.expireDate = expireDate;
    }

    public String getEncryptCode() {
        return encryptCode;
    }

    public void setEncryptCode(String encryptCode) {
        this.encryptCode = encryptCode;
    }
}

package com.dp.ishare.entry;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.util.Date;
@Table("file_info")
public class FileInfo {
    @Id
    @Column("fileId")
    private String fileId;

    @Column("fileName")
    private String fileName;

    @Column("fileType")
    private String fileType;

    @Column("fileSize")
    private Long fileSize;

    @Column("userId")
    private String userId;

    @Column("uploadDate")
    private Date uploadDate;

    @Column("expireDate")
    private Date expireDate;

    @Column("encryptCode")
    private String encryptCode;

    public FileInfo() {
    }

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

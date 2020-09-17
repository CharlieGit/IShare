package com.dp.ishare.entry;

import java.util.Date;

public class User {
    private String userId;
    private String remoteAddress;
    private Date lastLogin;
    private String sendFileName;
    private String sendFileUrl;
    private String sendFileSize;

    public User(String userId, String remoteAddress, Date lastLogin, String sendFileName, String sendFileUrl, String sendFileSize) {
        this.userId = userId;
        this.remoteAddress = remoteAddress;
        this.lastLogin = lastLogin;
        this.sendFileName = sendFileName;
        this.sendFileUrl = sendFileUrl;
        this.sendFileSize = sendFileSize;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getRemoteAddress() {
        return remoteAddress;
    }

    public void setRemoteAddress(String remoteAddress) {
        this.remoteAddress = remoteAddress;
    }

    public Date getLastLogin() {
        return lastLogin;
    }

    public void setLastLogin(Date lastLogin) {
        this.lastLogin = lastLogin;
    }

    public String getSendFileName() {
        return sendFileName;
    }

    public void setSendFileName(String sendFileName) {
        this.sendFileName = sendFileName;
    }

    public String getSendFileUrl() {
        return sendFileUrl;
    }

    public void setSendFileUrl(String sendFileUrl) {
        this.sendFileUrl = sendFileUrl;
    }

    public String getSendFileSize() {
        return sendFileSize;
    }

    public void setSendFileSize(String sendFileSize) {
        this.sendFileSize = sendFileSize;
    }
}

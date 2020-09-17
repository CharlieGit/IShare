package com.dp.ishare.entry;

public class ApiResult<T> {
    private Integer respCode;
    private String respMsg;
    private T data;

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

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "ApiResult{" +
                "respCode=" + respCode +
                ", respMsg='" + respMsg + '\'' +
                ", data=" + data.toString() +
                '}';
    }
}

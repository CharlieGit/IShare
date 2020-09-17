package com.dp.ishare.constants;

public enum ResponseMsg {
    SUCCESS(1000, "success"),
    COMMON_ERROR(1001, "Unknown error, please try again"),
    MISSING_PARAMETER(1002, "missing parameter"),
    FILE_SIZE_LIMIT(1003, "exceeded the size limit");

    Integer code;
    String msg;

    ResponseMsg(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public Integer getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}

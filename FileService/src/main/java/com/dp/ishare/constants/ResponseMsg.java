package com.dp.ishare.constants;

public enum ResponseMsg {
    SUCCESS(100, "success"),
    COMMON_ERROR(101, "Unknown error, please try again"),
    MISSING_PARAMETER(102, "missing parameter"),
    FILE_SIZE_LIMIT(103, "exceeded the size limit");

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

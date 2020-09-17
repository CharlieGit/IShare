package com.dp.ishare.entry;

import com.dp.ishare.constants.ResponseMsg;

public class ResponseBuilder {
    public static <T> ApiResult<T> success(T t, ResponseMsg msg) {
        ApiResult<T> response = new ApiResult<T>();
        response.setRespCode(msg.getCode());
        response.setRespMsg(msg.getMsg());
        response.setData(t);
        return response;
    }

    public static <T> ApiResult<T> successNoData(ResponseMsg msg) {
        ApiResult<T> response = new ApiResult<T>();
        response.setRespCode(msg.getCode());
        response.setRespMsg(msg.getMsg());
        return response;
    }

    public static <T> ApiResult<T> fail(ResponseMsg msg) {
        ApiResult<T> response = new ApiResult<T>();
        response.setRespCode(msg.getCode());
        response.setRespMsg(msg.getMsg());
        return response;
    }
}

package com.jexhsu.usercenter.utils;

import com.jexhsu.usercenter.common.BaseResponse;
import com.jexhsu.usercenter.common.ErrorCode;

public class ResultUtils {
    /**
     * 成功
     *
     * @param data
     * @param <T>
     * @return
     */
    public static <T> BaseResponse<T> success(T data) {
        return new BaseResponse<>(0, data, "success");
    }

    /**
     * 失败
     *
     * @param errorCode
     * @return
     */
    @SuppressWarnings("rawtypes")
    public static BaseResponse error(ErrorCode errorCode) {
        return new BaseResponse<>(errorCode);
    }

    /**
     * 失败
     *
     * @param code
     * @param message
     * @param description
     * @return
     */
    @SuppressWarnings("rawtypes")
    public static BaseResponse error(int code, String message, String description) {
        return new BaseResponse<>(code, null, message, description);
    }

    /**
     * 失败 - 消息和描述
     *
     * @param errorCode
     * @param message
     * @param description
     * @return
     */
    @SuppressWarnings("rawtypes")
    public static BaseResponse error(ErrorCode errorCode, String message, String description) {
        return new BaseResponse<>(errorCode.getCode(), null, message, description);
    }

    /**
     * 失败 - 描述无消息
     *
     * @param errorCode
     * @param description
     * @return
     */
    @SuppressWarnings("rawtypes")
    public static BaseResponse error(ErrorCode errorCode, String description) {
        return new BaseResponse<>(errorCode.getCode(), null, errorCode.getMessage(), description);
    }
}
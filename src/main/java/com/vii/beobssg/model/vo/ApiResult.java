package com.vii.beobssg.model.vo;

import org.springframework.http.HttpStatus;

import java.io.Serializable;

/**
 * @author Luthfi Aryarizki
 * @date 2025/11/24
 * @description Global Result
 */
public class ApiResult<T> implements Serializable {

    private Integer code;
    private T data;
    private String msg;

    public ApiResult(Integer code, T data, String msg) {
        this.code = code;
        this.data = data;
        this.msg = msg;
    }

    public ApiResult() {}

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public static <T> ApiResult<T> success(T data) {
        return new ApiResult<>(HttpStatus.OK.value(), data, "Success");
    }

    public static <T> ApiResult<T> error(String msg) {
        return new ApiResult<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), null, msg);
    }

}


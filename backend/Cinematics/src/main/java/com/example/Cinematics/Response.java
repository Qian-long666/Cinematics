package com.example.Cinematics;

import lombok.Data;

/*
统一响应类（Unified Response Class）是 RESTful API 设计中用于规范前后端数据交互格式的标准封装结构，
核心目的是定义一套统一的接口契约，确保所有 API 响应在结构上保持一致，从而提升系统的可维护性、可扩展性和前端调用的友好性。
 */
@Data
public class Response<T> {

    private T data;
    private boolean success;  //是否成功标志
    private String errorMsg;  //错误信息

    //成功时，盒子装数据
    public static <K> Response<K> newSuccess(K data){
        Response<K> response = new Response<>();
        response.setData(data);
        response.setSuccess(true);
        return response;
    }

    //失败时，盒子不装数据，只装错误信息
    public static Response<Void> newFail(String errorMsg){
        Response<Void> response = new Response<>();
        response.setErrorMsg(errorMsg);
        response.setSuccess(false);
        return response;
    }
}

package com.hgt.project.common.util;

import lombok.Data;

/**
 * 自定义Rest接口统一返回值对象
 * @author karl xavier
 * @version 0.1
 * @see "write something..."
*/
@Data
public class Response {

    private boolean success;

    private String message;

    private Object body;

    public Response() {

    }

    public Response(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    public Response(boolean success, String message, Object body) {
        this.success = success;
        this.message = message;
        this.body = body;
    }
}

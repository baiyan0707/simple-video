package com.simple.commons.exception;

import lombok.Getter;

/**
 * @author bai
 * @Description 用来装载异常实体类
 * @Date 2020/3/11 11:00 PM
 */
public class GlobalException extends RuntimeException{

    private static final long serialVersionUID = 8939148148322273820L;

    @Getter
    private final SimpleVideoErrorCode errorCode;

    public GlobalException(SimpleVideoErrorCode errorCode) {
        this(errorCode,errorCode.getErrorDesc());
    }

    public GlobalException(SimpleVideoErrorCode errorCode,String message){
        super(message);
        this.errorCode = errorCode;
    }
}

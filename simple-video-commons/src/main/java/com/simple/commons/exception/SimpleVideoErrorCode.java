package com.simple.commons.exception;

import lombok.Getter;

/**
 * @author bai
 */
@Getter
public enum SimpleVideoErrorCode {

    /** 200 --- 成功 */
    SIMPLEVIDEO_ERROR_CODE_200("200", "成功"),
    /** 401--- token失效*/
    SIMPLEVIDEO_ERROR_CODE_401("401", "token失效"),
    /** 000001 --- 请求参数非法！ */
    SIMPLEVIDEO_ERROR_CODE_000001("000001", "请求参数非法！"),
    /** 000002 --- 非法操作！ */
    SIMPLEVIDEO_ERROR_CODE_000002("000002","非法操作！"),
    /** 000003 --- 获取请求参数异常！*/
    SIMPLEVIDEO_ERROR_CODE_000003("000003","获取请求参数异常！"),
    /** 000004 --- 验证码错误*/
    SIMPLEVIDEO_ERROR_CODE_000004("000004","验证码错误！"),

    /** AA0001 --- 用户添加异常 **/
    SIMPLEVIDEO_ERROR_CODE_AA0001("AA0001","用户添加异常!"),

    /***********未知错误 ***********/
    SIMPLEVIDEO_ERROR_CODE_999999("999999", "服务器开小差了,请稍后重试！"),
    ;

    private String errorCode;
    private String errorDesc;

    SimpleVideoErrorCode(String errorCode, String errorDesc){
        this.errorCode = errorCode;
        this.errorDesc = errorDesc;
    }

    public static SimpleVideoErrorCode transferByErrorCode(String errorCode){
        for (SimpleVideoErrorCode value : SimpleVideoErrorCode.values()) {
            if(value.errorCode.equalsIgnoreCase(errorCode)){
                return value;
            }
        }
        return SimpleVideoErrorCode.SIMPLEVIDEO_ERROR_CODE_999999;
    }
}

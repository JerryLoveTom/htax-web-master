package com.htax.common.exception;

public enum ErrorCodeEnum {
    SUCCESS("success",200),
    IMPORT_EXCEL_ERROR("导入excel失败",1001),
    IMPORT_EXCEL_DATA_NULL("excel数据为空",1002),
    UNKNOWN_ERROR("unknown error",9999);

    private int code; // 错误代码
    private String msg; // 消息

    ErrorCodeEnum(String msg , int code){
        this.msg = msg;
        this.code = code;
    }

    public  int code(){
        return code;
    }
    public String msg(){
        return msg;
    }
    public static ErrorCodeEnum getErrorCode(int code){
        for (ErrorCodeEnum it : ErrorCodeEnum.values()){
            if (it.code == code){
                return it;
            }
        }
        return UNKNOWN_ERROR;
    }
}

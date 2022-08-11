package com.htax.common.webscoket;

public class WsMessage {

    private String code;

    private String dataType;

    private String data;

    public WsMessage() {
        super();
    }

    public WsMessage(String code, String data) {
        this.code = code;
        this.data = data;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}

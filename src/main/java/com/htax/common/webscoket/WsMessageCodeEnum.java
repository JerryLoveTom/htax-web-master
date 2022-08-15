package com.htax.common.webscoket;

/**
 * websocket消息类别
 */
public enum WsMessageCodeEnum {
    WORKFLOWSTATUS("workFlowStatus"); // 工作流状态
    private String code;

    WsMessageCodeEnum(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}

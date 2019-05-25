package com.em.model.enums;

public enum MsgSignFLagEnum {
    unsign(0, "未签收"),
    signed(1, "已签收");

    public final Integer type;
    public final String content;

    MsgSignFLagEnum(Integer type, String content) {
        this.type = type;
        this.content = content;
    }
}

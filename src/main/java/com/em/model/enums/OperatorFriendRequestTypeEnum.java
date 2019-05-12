package com.em.model.enums;

public enum OperatorFriendRequestTypeEnum {

    IGNORE(0,"忽略"),
    PASS(1,"通过");


    public final String msg;
    public final Integer type;

    OperatorFriendRequestTypeEnum(Integer type,String msg){
        this.type=type;
        this.msg = msg;
    }

    public String getMsg() {
        return msg;
    }

    public Integer getType() {
        return type;
    }

    public static String getMsgByType(Integer type){
        for (OperatorFriendRequestTypeEnum ofrtEnum : OperatorFriendRequestTypeEnum.values()) {
            if(ofrtEnum.type.equals(type)){
                return ofrtEnum.msg;
            }
        }
        return null;
    }
}

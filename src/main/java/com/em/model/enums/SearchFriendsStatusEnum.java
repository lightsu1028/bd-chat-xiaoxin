package com.em.model.enums;

public enum SearchFriendsStatusEnum {

    SUCCESS(0,"OK"),
    USER_NOT_EXIST(1,"无此用户..."),
    NOT_YOURSELF(2,"不能添加你自己..."),
    ALREADY_FRIENDS(3,"该用户已经是你好友...");

    public final Integer status;
    public final String msg;

     SearchFriendsStatusEnum(Integer status, String msg) {
        this.status = status;
        this.msg = msg;
    }

    public Integer getStatus() {
        return status;
    }

    public String getMsg() {
        return msg;
    }

    public static  String getMsgByKey(Integer status){
         if(status.equals(SUCCESS.status)){
             return SUCCESS.msg;
         }else if(status.equals(USER_NOT_EXIST.status)){
             return USER_NOT_EXIST.msg;
         }else if(status.equals(NOT_YOURSELF.status)){
             return NOT_YOURSELF.msg;
         }else if(status.equals(ALREADY_FRIENDS.status)){
             return ALREADY_FRIENDS.msg;
         }
         return null;
    }
}

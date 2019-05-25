package com.em.model;


import com.em.model.vo.ChatMsg;

import java.io.Serializable;

public class DataContent implements Serializable{

    private Integer action; //动作类型
    private com.em.model.vo.ChatMsg chatMsg; //用户的聊天内容entity
    private String extand; //扩展字段

    public Integer getAction() {
        return action;
    }

    public void setAction(Integer action) {
        this.action = action;
    }

    public ChatMsg getChatMsg() {
        return chatMsg;
    }

    public void setChatMsg(ChatMsg chatMsg) {
        this.chatMsg = chatMsg;
    }

    public String getExtand() {
        return extand;
    }

    public void setExtand(String extand) {
        this.extand = extand;
    }
}

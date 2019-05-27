package com.em.service;

import com.em.model.ChatMessage;
import com.em.model.vo.ChatMsg;

import java.util.List;

public interface ChatMessageService {
    String saveMsg(ChatMsg msg);

    void updateMsgIsSigned(String msgId);

    List<ChatMessage> getUnReadMessage(String receiveId);
}

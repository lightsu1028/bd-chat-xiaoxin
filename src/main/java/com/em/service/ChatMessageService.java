package com.em.service;

import com.em.model.vo.ChatMsg;

public interface ChatMessageService {
    String saveMsg(ChatMsg msg);

    void updateMsgIsSigned(String msgId);
}

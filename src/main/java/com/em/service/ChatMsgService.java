package com.em.service;

import com.em.model.ChatMsg;

public interface ChatMsgService {
    String saveMsg(ChatMsg msg);

    void updateMsgIsSigned(String msgId);
}

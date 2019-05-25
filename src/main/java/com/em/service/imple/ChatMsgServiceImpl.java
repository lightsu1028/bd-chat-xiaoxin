package com.em.service.imple;

import com.em.dao.ChatMsgMapper;
import com.em.model.ChatMsg;
import com.em.model.enums.MsgSignFLagEnum;
import com.em.service.ChatMsgService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.UUID;

@Service("chatMsgService")
public class ChatMsgServiceImpl implements ChatMsgService {

    @Autowired
    private ChatMsgMapper msgMapper;

    public String saveMsg(ChatMsg msg){
        String msgId = UUID.randomUUID().toString();

        ChatMsg params = new ChatMsg();
        params.setId(msgId);
        params.setCreateTime(new Date());
        params.setMsg(msg.getMsg());
        params.setAcceptUserId(msg.getAcceptUserId());
        params.setSendUserId(msg.getSendUserId());
        params.setSignFlag(MsgSignFLagEnum.unsign.type);

        msgMapper.insert(params);

        return msgId;
    }

    public void updateMsgIsSigned(String msgId){
        ChatMsg chatMsg = new ChatMsg();
        chatMsg.setId(msgId);
        chatMsg.setSignFlag(MsgSignFLagEnum.signed.type);

        msgMapper.updateByPrimaryKey(chatMsg);
    }
}

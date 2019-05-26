package com.em.service.imple;

import com.em.dao.ChatMessageMapper;
import com.em.model.ChatMessage;
import com.em.model.ChatMessageExample;
import com.em.model.enums.MsgSignFLagEnum;
import com.em.model.vo.ChatMsg;
import com.em.service.ChatMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.UUID;

@Service("chatMessageService")
public class ChatMessageServiceImpl implements ChatMessageService {

    @Autowired
    private ChatMessageMapper messageMapper;

    @Override
    public String saveMsg(ChatMsg msg) {
        ChatMessage params = new ChatMessage();

        String msgId = UUID.randomUUID().toString();
        params.setAcceptUserId(msg.getReceiverId());
        params.setCreateTime(new Date());
        params.setId(msgId);
        params.setSendUserId(msg.getSenderId());
        params.setMsg(msg.getMsg());
        params.setSignFlag(MsgSignFLagEnum.unsign.type);

        messageMapper.insert(params);
        return msgId;
    }

    @Override
    public void updateMsgIsSigned(String msgId) {
//        ChatMessage msg = new ChatMessage();
//        msg.setId(msgId);
//        msg.setSignFlag(MsgSignFLagEnum.signed.type);
//
//        messageMapper.updateByPrimaryKey(msg);

        ChatMessageExample msgExample = new ChatMessageExample();
        ChatMessageExample.Criteria criteria = msgExample.createCriteria();
        criteria.andIdEqualTo(msgId);

        ChatMessage msg = new ChatMessage();
        msg.setSignFlag(MsgSignFLagEnum.signed.type);

        messageMapper.updateByExampleSelective(msg,msgExample);
    }


}

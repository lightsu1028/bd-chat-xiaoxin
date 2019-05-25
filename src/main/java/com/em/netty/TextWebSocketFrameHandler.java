package com.em.netty;

import com.alibaba.fastjson.JSON;
import com.em.model.ChatMsg;
import com.em.model.DataContent;
import com.em.model.enums.MsgActionEnum;
import com.em.service.ChatMsgService;
import com.em.utils.SpringContextUtil;
import com.em.utils.UserChannelRel;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.util.concurrent.GlobalEventExecutor;
import org.apache.commons.lang3.StringUtils;

import java.time.LocalDateTime;
import java.util.Arrays;

/**
 * @author Baikang Lu
 * @date 2019/4/21
 */
public class TextWebSocketFrameHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {

    private static ChannelGroup users = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg) throws Exception {
        System.out.println("服务端收到消息："+msg.text());
//        ctx.channel().writeAndFlush(new TextWebSocketFrame("服务器端时间" + LocalDateTime.now()));

        Channel currentChannel = ctx.channel();
        //1.获取客户端发来的消息
        String text = msg.text();
//        DataContent dc =(DataContent) JSON.parse(text);
        DataContent dc = JSON.parseObject(text,DataContent.class);
        ChatMsg chatMsg = dc.getChatMsg();
        Integer action = dc.getAction();
        if(action.equals(MsgActionEnum.CONNECT.type) ){
            //2.当websocket第一次open的时候，初始化channel，关联userid
            String sendUserId = chatMsg.getSendUserId();
            UserChannelRel.put(sendUserId,currentChannel);

            //测试
            //打印channelgroup channel id
           users.stream().forEach(c-> System.out.println(c.id().asLongText()));
            UserChannelRel.output();
        }else if(MsgActionEnum.CHAT.type.equals(action)){
            //保存聊天信息记录
            ChatMsgService msgService = SpringContextUtil.getBean("chatMsgService");
            String msgId = msgService.saveMsg(chatMsg);
            chatMsg.setId(msgId);

            Channel receiveChannel = UserChannelRel.get(chatMsg.getAcceptUserId());
            if(receiveChannel==null){
                //todo 推送
            }else{
                //receiveChannel不为空的时候,从channelGroup去查找对应的channel是否存在
                Channel channel = users.find(receiveChannel.id());
                if(channel==null){
                    //todo 推送
                }else{
                    //用户在线
                    channel.writeAndFlush(new TextWebSocketFrame(JSON.toJSONString(chatMsg)));
                }
            }
        }else if(MsgActionEnum.SIGNED.type.equals(action)){
            ChatMsgService msgService = SpringContextUtil.getBean("chatMsgService");

            //签收消息类型，修改数据库中具体的对应消息的签收状态【已签收】
            String msgIds = dc.getExtand();

            //批量签收
            Arrays.stream(msgIds.split(",")).filter(StringUtils::isNotBlank)
                    .forEach(s->msgService.updateMsgIsSigned(s));


        }else if(MsgActionEnum.KEEPALIVE.type.equals(action)){

        }


        //3.
    }

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        System.out.println("handlerAdded:"+ctx.channel().id().asLongText());
        users.add(ctx.channel());
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        System.out.println("handlerremoved:"+ctx.channel().id().asLongText());
        users.remove(ctx.channel());
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        System.out.println("异常发生");
        ctx.channel().close();
    }
}

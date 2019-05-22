package com.em.netty;

import com.alibaba.fastjson.JSON;
import com.em.model.ChatMsg;
import com.em.model.DataContent;
import com.em.model.enums.MsgActionEnum;
import com.em.utils.UserChannelRel;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.util.concurrent.GlobalEventExecutor;

import java.time.LocalDateTime;

/**
 * @author Baikang Lu
 * @date 2019/4/21
 */
public class TextWebSocketFrameHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {

    private static ChannelGroup users = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg) throws Exception {
        System.out.println("服务端收到消息："+msg.text());
        ctx.channel().writeAndFlush(new TextWebSocketFrame("服务器端时间" + LocalDateTime.now()));

        //1.获取客户端发来的消息
        String text = msg.text();
        DataContent dc =(DataContent) JSON.parse(text);
        ChatMsg chatMsg = dc.getChatMsg();
        Integer action = dc.getAction();
        if(action.equals(MsgActionEnum.CONNECT.type) ){
            //2.当websocket第一次open的时候，初始化channel，关联userid
            Channel userIdChannel = UserChannelRel.get(chatMsg.getAcceptUserId());
            if(userIdChannel==null){
                //todo 推送
            }else{
                Channel channel = users.find(userIdChannel.id());
                if(channel==null){
                    //todo 推送
                }else{
                    channel.writeAndFlush(new TextWebSocketFrame(JSON.toJSONString(chatMsg)));
                }
            }
        }else if(MsgActionEnum.CHAT.type.equals(action)){

        }else if(MsgActionEnum.SIGNED.type.equals(action)){

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

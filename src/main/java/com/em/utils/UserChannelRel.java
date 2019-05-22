package com.em.utils;

import io.netty.channel.Channel;

import java.util.HashMap;
import java.util.Map;

public class UserChannelRel {
    public static Map<String, Channel> userChannel = new HashMap<>();

    public  static void put(String userId,Channel channelRef){
        userChannel.put(userId, channelRef);
    }

    public static Channel get(String userId){
        return userChannel.get(userId);
    }

}

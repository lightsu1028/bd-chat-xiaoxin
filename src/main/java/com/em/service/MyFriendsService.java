package com.em.service;

import com.em.model.vo.MyFriendsVo;

import java.util.List;

public interface MyFriendsService {
    List<MyFriendsVo> queryMyFriends(String userId);
}

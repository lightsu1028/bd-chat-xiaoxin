package com.em.service;


import com.em.model.Users;
import com.em.model.vo.FriendRequestVo;

import java.util.List;

public interface UsersService {
    Users add(Users user) throws Exception;

    boolean getUserIsExist(String userName);

    Users getUserForLogin(Users users) throws Exception;

    Users updateUserInfo(Users users);

    Integer preconditionSearchFriends(String myUserId,String friendUserName);

    Users queryUsersInfoByName(String userName);

    void sendFriendRequest(String myUserId,String friendUserName);

    List<FriendRequestVo> queryFriendRequestList(String receivedUserId);
}

package com.em.service.imple;

import com.em.dao.MyFriendsMapper;
import com.em.model.vo.MyFriendsVo;
import com.em.service.MyFriendsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MyFriendsServiceImpl implements MyFriendsService {

    @Autowired
    private MyFriendsMapper myFriendsMapper;

    @Override
    public List<MyFriendsVo> queryMyFriends(String userId) {
        return myFriendsMapper.getMyFriensInfo(userId);
    }
}

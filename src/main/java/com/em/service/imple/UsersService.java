package com.em.service.imple;

import com.em.dao.UsersMapper;
import com.em.model.Users;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * @author Baikang Lu
 * @date 2019/4/27
 */
@Service
public class UsersService implements com.em.service.UsersService{

    @Autowired
    private UsersMapper usersMapper;

    @Override
    public int add() {
        Users users = new Users();
        users.setId(UUID.randomUUID().toString());
        users.setUsername("baikang");
        users.setPassword("123456");
        users.setNickname("89757最烦");
        users.setCid("123223osf");
        users.setQrcode("1313zcd");
        users.setFaceImage("/opt/sd");
        users.setFaceImageBig("/adadd/2da");
          return  usersMapper.insert(users);
    }
}

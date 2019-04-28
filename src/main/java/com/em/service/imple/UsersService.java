package com.em.service.imple;

import com.em.dao.UsersMapper;
import com.em.model.Users;
import com.em.model.UsersExample;
import com.em.utils.AESUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
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
    public Users add(Users user) throws Exception {
//        Users users = new Users();
        user.setId(UUID.randomUUID().toString());
        //二维码生成
        user.setQrcode("");
        user.setPassword(AESUtil.enCrypt(user.getPassword()));
        usersMapper.insert(user);
        return user;
    }

    @Override
    public boolean getUserIsExist(String userName) {
        UsersExample usersExample = new UsersExample();
        UsersExample.Criteria criteria = usersExample.createCriteria();
        criteria.andUsernameEqualTo(userName);
        List<Users> users = usersMapper.selectByExample(usersExample);

        return (users!=null&&users.size()>0)?true:false;
    }


    @Override
    public Users getUserForLogin(Users users) throws Exception{

        UsersExample usersExample = new UsersExample();
        UsersExample.Criteria criteria = usersExample.createCriteria();
        criteria.andUsernameEqualTo(users.getUsername());
        criteria.andPasswordEqualTo(AESUtil.enCrypt(users.getPassword()));
        List<Users> userResult = usersMapper.selectByExample(usersExample);

        return userResult.get(0);
    }

}

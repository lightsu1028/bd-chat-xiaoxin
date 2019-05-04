package com.em.service;


import com.em.model.Users;

public interface UsersService {
    Users add(Users user) throws Exception;

    boolean getUserIsExist(String userName);

    Users getUserForLogin(Users users) throws Exception;

    Users updateUserInfo(Users users);
}

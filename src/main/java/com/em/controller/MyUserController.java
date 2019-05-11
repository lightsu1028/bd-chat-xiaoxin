package com.em.controller;

import com.alibaba.fastjson.JSONObject;
import com.em.common.Result;
import com.em.common.RspCode;
import com.em.dao.FriendsRequestMapper;
import com.em.model.Users;
import com.em.model.enums.SearchFriendsStatusEnum;
import com.em.model.vo.UsersVo;
import com.em.service.UsersService;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/user")
public class MyUserController extends BaseController {

    @Autowired
    private UsersService usersService;



    @RequestMapping("/search")
    public Result searchUser(@RequestBody JSONObject params) throws Exception {

        String myUserId = params.getString("myUserId");
        String friendUserName = params.getString("friendUserName");
        if(StringUtils.isBlank(myUserId)||StringUtils.isBlank(friendUserName)){
            return new Result(RspCode.PARAMS_ERROR);
        }
        //前置条件-1.搜索用户不存在，返回[无此用户]
        //前置条件-2.搜索用户是自己，返回[不能添加你自己]
        //前置条件-3.搜索用户已经是你好友，返回[该用户已经是你好友]
        Integer status = usersService.preconditionSearchFriends(myUserId, friendUserName);
        if(status== SearchFriendsStatusEnum.SUCCESS.status){
            Users users = usersService.queryUsersInfoByName(friendUserName);
            UsersVo usersVo = new UsersVo();
            BeanUtils.copyProperties(usersVo,users);
            return new Result(usersVo);
        }else{
            return new Result(RspCode.ADD_USER_FAIL, SearchFriendsStatusEnum.getMsgByKey(status));
        }
    }

    @RequestMapping("/addFriendRequest")
    public Result addFriendRequest(@RequestBody JSONObject params){
        String myUserId = params.getString("myUserId");
        String friendUserName = params.getString("friendUserName");
        if(StringUtils.isBlank(myUserId)||StringUtils.isBlank(friendUserName)){
            return new Result(RspCode.PARAMS_ERROR);
        }
        //前置条件-1.搜索用户不存在，返回[无此用户]
        //前置条件-2.搜索用户是自己，返回[不能添加你自己]
        //前置条件-3.搜索用户已经是你好友，返回[该用户已经是你好友]
        Integer status = usersService.preconditionSearchFriends(myUserId, friendUserName);
        if(status== SearchFriendsStatusEnum.SUCCESS.status){
            //发送请求添加好友记录
            usersService.sendFriendRequest(myUserId, friendUserName);
            return new Result();
        }else{
            return new Result(RspCode.ADD_USER_FAIL, SearchFriendsStatusEnum.getMsgByKey(status));
        }
    }

    @RequestMapping("/queryFriendRequest")
    public Result queryFriendRequest(@RequestBody JSONObject params){
        String userId = params.getString("userId");
        if(StringUtils.isBlank(userId)){
            return new Result(RspCode.PARAMS_ERROR);
        }
        return new Result(usersService.queryFriendRequestList(userId));
    }

}

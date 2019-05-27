package com.em.controller;

import com.alibaba.fastjson.JSONObject;
import com.em.common.Result;
import com.em.common.RspCode;
import com.em.model.ChatMessage;
import com.em.model.Users;
import com.em.model.enums.OperatorFriendRequestTypeEnum;
import com.em.model.enums.SearchFriendsStatusEnum;
import com.em.model.vo.MyFriendsVo;
import com.em.model.vo.UsersVo;
import com.em.service.ChatMessageService;
import com.em.service.MyFriendsService;
import com.em.service.UsersService;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequestMapping("/user")
public class MyUserController extends BaseController {

    @Autowired
    private UsersService usersService;

    @Autowired
    private MyFriendsService myFriendsService;

    @Autowired
    private ChatMessageService messageService;



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


    /**
     *  好友请求忽略/通过操作
     * @param params
     * @return
     */
    @RequestMapping("/operFriendRequest")
    public Result operFriendRequest(@RequestBody JSONObject params){
        String sendUserId = params.getString("sendUserId");
        String acceptUserId = params.getString("acceptUserId");
        Integer operType = params.getInteger("operType");
        if(StringUtils.isBlank(sendUserId)||StringUtils.isBlank(acceptUserId)
                ||operType==null){
            return new Result(RspCode.PARAMS_ERROR);
        }
        //通过操作
        if(OperatorFriendRequestTypeEnum.PASS.type.equals(operType)){
            usersService.passFriendRequest(sendUserId,acceptUserId);
        }else{//忽略操作
            usersService.deleteFriendRequest(sendUserId,acceptUserId);
        }

        //数据库查询好友列表
        List<MyFriendsVo> myFriendsVos = myFriendsService.queryMyFriends(acceptUserId);

        return new Result(myFriendsVos);
    }

    /**
     *  获取通讯录好友
     * @param params
     * @return
     */
    @RequestMapping("/myFriends")
    public Result myFriends(@RequestBody JSONObject params){
        String userId = params.getString("userId");
        if(StringUtils.isBlank(userId)){
            return new Result(RspCode.PARAMS_ERROR);
        }
        List<MyFriendsVo> myFriendsVos = myFriendsService.queryMyFriends(userId);
        return new Result(myFriendsVos);
    }

    /**
     *  获取未签收的消息
     * @param params
     * @return
     */
    @RequestMapping("/getUnReadMessage")
    public Result getUnReadMessage(@RequestBody JSONObject params){
        String receiveUserId = params.getString("receiveUserId");
        List<ChatMessage> unReadMessage = messageService.getUnReadMessage(receiveUserId);
        return new Result(unReadMessage);
    }
}

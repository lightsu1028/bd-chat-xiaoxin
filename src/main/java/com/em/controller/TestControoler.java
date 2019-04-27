package com.em.controller;

import com.alibaba.fastjson.JSONObject;
import com.em.common.Result;
import com.em.common.RspCode;
import com.em.service.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Baikang Lu
 * @date 2019/4/27
 */
@RestController
@RequestMapping("/user")
public class TestControoler  extends BaseController{

    @Autowired
    private UsersService usersService;

    @RequestMapping("/add")
    public Result test(){
        int result = usersService.add();
        return new Result(RspCode.SUCCESS);
    }
}

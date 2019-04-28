package com.em.controller;

import com.em.common.Result;
import com.em.common.RspCode;
import com.em.model.Users;
import com.em.model.vo.UsersVo;
import com.em.service.UsersService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Baikang Lu
 * @date 2019/4/28
 */
@RestController
@RequestMapping("/u")
public class LoginController extends BaseController {

    @Autowired
    private UsersService usersService;


    @RequestMapping("/registOrLogin")
    public Result login(@RequestBody Users users) throws Exception {
        if(StringUtils.isBlank(users.getUsername())||StringUtils.isBlank(users.getPassword())){
            return new Result(RspCode.PARAMS_ERROR, "用户名或密码不能为空");
        }

        boolean userIsExist = usersService.getUserIsExist(users.getUsername());

        Users result;
        if(userIsExist){
            //login
            result = usersService.getUserForLogin(users);
            if(result==null){
                return new Result(RspCode.USERNAME_PASSWORD_ERROR, "账户名或密码错误");
            }
        }else{
            //register
            users.setFaceImageBig("");
            users.setFaceImage("");
            users.setNickname(users.getUsername());
            result = usersService.add(users);
        }
        UsersVo usersVo = new UsersVo();
        BeanUtils.copyProperties(result,usersVo);
        return new Result(usersVo);
    }
}

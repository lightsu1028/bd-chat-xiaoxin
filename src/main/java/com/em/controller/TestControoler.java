package com.em.controller;

import com.em.service.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
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


}

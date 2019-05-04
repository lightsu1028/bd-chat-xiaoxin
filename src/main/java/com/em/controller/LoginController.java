package com.em.controller;

import com.em.common.Result;
import com.em.common.RspCode;
import com.em.model.User;
import com.em.model.Users;
import com.em.model.bo.UserBo;
import com.em.model.vo.UsersVo;
import com.em.service.UsersService;
import com.em.utils.FastDFSClient;
import com.em.utils.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author Baikang Lu
 * @date 2019/4/28
 */
@RestController
@RequestMapping("/u")
public class LoginController extends BaseController {

    @Autowired
    private UsersService usersService;

    @Autowired
    private FastDFSClient fastClient;


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


    @RequestMapping("/uploadFaceBase64")
    public Result uploadImg(@RequestBody UserBo userBo) throws Exception{
        //base64文件转换为文件
        String base64Img = userBo.getFaceData();
        String userFacePath = "c:\\" + userBo.getUserId() + "userFace64.png";
        FileUtils.base64ToFile(userFacePath, base64Img);

        //上传文件到fastdfs
        MultipartFile multipartFile = FileUtils.fileToMultipart(userFacePath);
        String url = fastClient.uploadBase64(multipartFile);
        System.out.println(url);

        //获取缩略图的url
        String[] split = url.split("\\.");
        String thump= "_80x80.";
        String thumpImg = split[0]+thump+split[1];
        System.out.println(thumpImg);

        Users users = new Users();
        users.setId(userBo.getUserId());
        users.setFaceImageBig(url);
        users.setFaceImage(thumpImg);

        Users result = usersService.updateUserInfo(users);

        return new Result(result);
    }

    @RequestMapping("/updateNickName")
    public Result updateNickName(@RequestBody UserBo userBo){


        Users users = new Users();
        users.setId(userBo.getUserId());
        users.setNickname(userBo.getNickName());

        Users result = usersService.updateUserInfo(users);

        return new Result(result);

    }
}

package com.em.service.imple;

import com.em.dao.UsersMapper;
import com.em.model.Users;
import com.em.model.UsersExample;
import com.em.utils.AESUtil;
import com.em.utils.FastDFSClient;
import com.em.utils.FileUtils;
import com.em.utils.QRCodeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.beans.Transient;
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

    @Autowired
    private QRCodeUtils qrCodeUtils;

    @Autowired
    FastDFSClient fastDFSClient;

    @Override
    public Users add(Users user) throws Exception {
//        Users users = new Users();
        String userId = UUID.randomUUID().toString();
        user.setId(userId);
        //二维码生成
        String qrCodePath = "C:\\user" + userId + "qrcode.png";
        //xiaoxin_qrcode:[username]
        qrCodeUtils.createQRCode(qrCodePath,"xiaoxin_qrcode:"+user.getUsername());
        MultipartFile multipartFile = FileUtils.fileToMultipart(qrCodePath);
        String qrCodeUrl = "";
        try{
            qrCodeUrl = fastDFSClient.uploadQRCode(multipartFile);
        }catch (Exception e){
            e.printStackTrace();
        }

        user.setQrcode(qrCodeUrl);
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

    @Transactional
    @Override
    public Users updateUserInfo(Users users) {
        usersMapper.updateByPrimaryKeySelective(users);

        return queryUsersInfo(users.getId());
    }

    private Users queryUsersInfo(String userId){
        return usersMapper.selectByPrimaryKey(userId);
    }

}

package com.em.service.imple;

import com.em.dao.FriendsRequestMapper;
import com.em.dao.MyFriendsMapper;
import com.em.dao.UsersMapper;
import com.em.model.*;
import com.em.model.enums.SearchFriendsStatusEnum;
import com.em.model.vo.FriendRequestVo;
import com.em.utils.AESUtil;
import com.em.utils.FastDFSClient;
import com.em.utils.FileUtils;
import com.em.utils.QRCodeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.beans.Transient;
import java.util.Date;
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


    @Autowired
    private MyFriendsMapper myFriendsMapper;

    @Autowired
    private FriendsRequestMapper friendsRequestMapper;

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

    /**
     *  添加好友前置条件
     */
   public Integer preconditionSearchFriends(String myUserId,String friendUserName){
       Users users = queryUsersInfoByName(friendUserName);
       if(users==null){
           return SearchFriendsStatusEnum.USER_NOT_EXIST.status;
       }else{
           if(users.getId().equals(myUserId)){
               return SearchFriendsStatusEnum.NOT_YOURSELF.status;
           }
           MyFriendsExample mf = new MyFriendsExample();
           MyFriendsExample.Criteria criteria = mf.createCriteria();
           criteria.andMyFriendUserIdEqualTo(users.getId());
           criteria.andMyUserIdEqualTo(myUserId);
           List<MyFriends> myFriends = myFriendsMapper.selectByExample(mf);
           if(myFriends!=null&&myFriends.size()>0){
               return SearchFriendsStatusEnum.ALREADY_FRIENDS.status;
           }else{
               return SearchFriendsStatusEnum.SUCCESS.status;
           }
       }
   }

   public Users queryUsersInfoByName(String userName){
       UsersExample example =new UsersExample();
       UsersExample.Criteria criteria = example.createCriteria();
       criteria.andUsernameEqualTo(userName);
       List<Users> users = usersMapper.selectByExample(example);

       return users.size()>0?users.get(0):null;
   }

    public static void main(String[] args) {
        String s = UUID.randomUUID().toString();
        System.out.println(s);
    }

    public void sendFriendRequest(String myUserId,String friendUserName){
        //根据用户名查询还有信息
       Users friend = queryUsersInfoByName(friendUserName);
        //1.查询发送好友请求记录表
        FriendsRequestExample fre = new FriendsRequestExample();
        FriendsRequestExample.Criteria criteria = fre.createCriteria();
        criteria.andSendUserIdEqualTo(myUserId);
        criteria.andAcceptUserIdEqualTo(friend.getId());
        List<FriendsRequest> friendsRequests = friendsRequestMapper.selectByExample(fre);
        if(friendsRequests==null||friendsRequests.size()==0){
            //2.如果不是你的好友，并且好友记录没有添加，新增好友请求记录
            FriendsRequest friendsRequest = new FriendsRequest();
            friendsRequest.setId(UUID.randomUUID().toString());
            friendsRequest.setSendUserId(myUserId);
            friendsRequest.setAcceptUserId(friend.getId());
            friendsRequest.setRequestDateTime(new Date());
            friendsRequestMapper.insert(friendsRequest);
        }
    }

    public List<FriendRequestVo> queryFriendRequestList(String receivedUserId){
        return usersMapper.selectFriendRequestList(receivedUserId);
    }

}

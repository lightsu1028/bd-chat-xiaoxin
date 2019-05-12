package com.em.model.vo;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@EqualsAndHashCode
public class MyFriendsVo {
    private String friendUserId;
    private String friendUserName;
    private String friendUserImage;
    private String friendUserNickName;

    public MyFriendsVo() {
    }

    public MyFriendsVo(String friendUserId, String friendUserName, String friendUserImage, String friendUserNickName) {
        this.friendUserId = friendUserId;
        this.friendUserName = friendUserName;
        this.friendUserImage = friendUserImage;
        this.friendUserNickName = friendUserNickName;
    }

    public String getFriendUserId() {
        return friendUserId;
    }

    public void setFriendUserId(String friendUserId) {
        this.friendUserId = friendUserId;
    }

    public String getFriendUserName() {
        return friendUserName;
    }

    public void setFriendUserName(String friendUserName) {
        this.friendUserName = friendUserName;
    }

    public String getFriendUserImage() {
        return friendUserImage;
    }

    public void setFriendUserImage(String friendUserImage) {
        this.friendUserImage = friendUserImage;
    }

    public String getFriendUserNickName() {
        return friendUserNickName;
    }

    public void setFriendUserNickName(String friendUserNickName) {
        this.friendUserNickName = friendUserNickName;
    }
}

package com.em.model.vo;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@EqualsAndHashCode
public class FriendRequestVo {
    private String sendUserId;
    private String sendUserName;
    private String sendUserFaceImage;
    private String sendUserNickName;
}

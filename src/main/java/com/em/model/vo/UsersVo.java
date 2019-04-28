package com.em.model.vo;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author Baikang Lu
 * @date 2019/4/28
 */
@Getter
@Setter
@ToString
@EqualsAndHashCode
public class UsersVo {
    private String username;

    private String password;

    private String faceImage;

    private String faceImageBig;

    private String nickname;

    private String qrcode;

}

package com.em.common;

public enum RspCode {
	
    /** 成功 */
    SUCCESS("200", "成功"),
    
    /** 未登录 */
    NOT_LOGIN("400", "未登录"),
    
    /** 系统异常 */
    EXCEPTION("401", "系统异常"),
    
    /** 参数错误 */
    PARAMS_ERROR("403", "参数错误 "),
    
    /** 图片验证码失效 */
    IMAGE_CODE_LAPSE("405", "图片验证码失效"),
    
    /** 图片验证码有误 */
    IMAGE_CODE_ERROR("406", "图片验证码有误"),
    
    /** 用户名密码有误 */
    USERNAME_PASSWORD_ERROR("407", "用户名密码有误"),
    
    /** 首次登录 */
    FIRST_LOGIN("408", "首次登陆"),
    
    /** 短信验证码失效 */
    SMS_CODE_LAPSE("409", "短信验证码失效"),
    
    /** 短信验证码错误 */
    SMS_CODE_ERROR("410", "短信验证码有误"),
    
    /** 账户锁定 */
    ACCOUNT_LOCK("411", "用户锁定"),
    
    /** 用户状态不可用*/
    ACCOUNT_STATUS_ERROR("412", "用户状态不可用"),
    
    /** 密码有误 */
    PASSWORD_ERROR("413", "当前密码有误"),
    
    /** 无访问权限 */
    INVALID_AUTHCODE("444", "无访问权限"),
    
    /** 未知错误 */
    UNKNOWN_ERROR("499", "未知错误"),

    ADD_USER_FAIL("405","查询添加好友失败");
	
    
     RspCode(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String code() {
        return code;
    }

    public String message() {
        return message;
    }

    private String code;
    private String message;
}

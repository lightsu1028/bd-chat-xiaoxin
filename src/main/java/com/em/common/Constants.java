package com.em.common;

public class Constants {

	/** 数据库字段-sequence名称 */
	public static final String DB_KEY_SEQ_NAME = "SEQ_FOR_CIC_KEY";
	
	/** 默认当前页 */
	public static final String DEFAULT_CURRENT_PAGE = "1";

	/** 默认数据抓取量 */
	public static final String DEFAULT_PAGE_SIZE = "10";

	/** 数据编辑类型-添加 */
	public static final int EDIT_FLAG_ADD = 1;

	/** 数据编辑类型-更新 */
	public static final int EDIT_FLAG_UPDATE = 2;
	
	/** 状态-用户状态-正常 */
	public static final String ACCOUNT_STS_NORMAL = "0";
	
	/** 状态-用户状态-已停用 */
	public static final String ACCOUNT_STS_CLOSE = "1";
	
	/** 状态-强制改密状态-无需改密 */
	public static final String PASSWD_STS_NORMAL = "0";
	
	/** 状态-强制改密状态-需改密 */
	public static final String PASSWD_STS_NEED = "1";



	// TODO 考虑是否迁移apollo------------------
	/** 邮件主题-添加运营账户 */
	public static final String MAIL_ADDUSER_SUBJECT = "东方财富征信运营账号开通";
	/** 邮件内容-添加运营账户 */
	public static final String MAIL_ADDUSER_CONTENT = "你好，${name}。你的东方财富征信系统运营账号已开通，账号${user}，初始随机密码${passwd}。首次登录会强制修改密码，请务必妥善保管密码。";
	/** 邮件主题-运营账户重置密码 */
	public static final String MAIL_RESETUSER_SUBJECT = "东方财富征信运营账号密码重置";
	/** 邮件内容-运营账户重置密码 */
	public static final String MAIL_RESETUSER_CONTENT = "你好，${name}。你的东方财富征信系统密码已重置为${passwd}。重置后首次登录会强制修改密码，请务必妥善保管密码。";
	/** 邮件主题-添加商户账户 */
	public static final String MAIL_ADDCUSTOM_SUBJECT = "东方财征信商户账号开通";
	/** 邮件内容-添加商户账户 */
	public static final String MAIL_ADDCUSTOM_CONTENT = "你好，${name}。你的东方财富征信系统商户账号已开通，账号${user}，初始随机密码${passwd}，AppKey为${appkey}。首次登录会强制修改密码，请务必妥善保管密码。";
	/** 邮件主题-商户账户重置密码 */
	public static final String MAIL_RESETCUSTOM_SUBJECT = "东方财富征信商户账号密码重置";
	/** 邮件内容-商户账户重置密码 */
	public static final String MAIL_RESETCUSTOM_CONTENT = "你好，${name}。你的东方财富征信系统商户密码已重置为${passwd}。重置后首次登录会强制修改密码，请务必妥善保管密码。";

}

package com.weidi.db;

/**
 * 
 * @author 白玉梁
 */
public class DBcolumns {

	/**
	 * 聊天消息信息表
	 */
	public static final String TABLE_MSG = "table_msg";
	public static final String MSG_ID = "msg_id";
	public static final String MSG_FROM = "msg_from";
	public static final String MSG_TO = "msg_to";
	public static final String MSG_TYPE = "msg_type";
	public static final String MSG_CONTENT = "msg_content";
	public static final String MSG_ISCOMING = "msg_iscoming";
	public static final String MSG_DATE = "msg_date";
	public static final String MSG_ISREADED = "msg_isreaded";
	public static final String MSG_BAK1 = "msg_bak1";
	public static final String MSG_BAK2 = "msg_bak2";
	public static final String MSG_BAK3 = "msg_bak3";
	public static final String MSG_BAK4 = "msg_bak4";
	public static final String MSG_BAK5 = "msg_bak5";
	public static final String MSG_BAK6 = "msg_bak6";

	/**
	 * 聊天会话表
	 */
	public static final String TABLE_SESSION = "table_session";
	public static final String SESSION_id = "session_id";
	public static final String SESSION_FROM = "session_from";
	public static final String SESSION_TYPE = "session_type";
	public static final String SESSION_TIME = "session_time";
	public static final String SESSION_CONTENT = "session_content";
	public static final String SESSION_TO = "session_to";// 登录人id
	public static final String SESSION_ISDISPOSE = "session_isdispose";

	/**
	 * 好友消息通知表
	 */
	public static final String TABLE_SYS_NOTICE = "table_sys_notice";
	public static final String SYS_NOTICE_ID = "sys_notice_id";
	public static final String SYS_NOTICE_TYPE = "sys_notice_type";
	public static final String SYS_NOTICE_FROM = "sys_notice_from";
	public static final String SYS_NOTICE_FROM_HEAD = "sys_notice_from_head";
	public static final String SYS_NOTICE_CONTENT = "sys_notice_content";
	public static final String SYS_NOTICE_ISDISPOSE = "sys_notice_isdispose";

	/**
	 * 添加好友
	 */
	public static final String TABLE_NEWFRIEND = "table_new_friend";

	/**
	 * 未读新消息条数
	 */
	public static final String TABLE_NEW_MSG_COUNT = "table_new_msg_count";

	public static final String TABLE_VCARD = "table_vcard";
	public static final String USER_NICK_NAME = "nickname";
	public static final String USER_NAME = "username";
	public static final String USER_TRUE_NAME = "truename";
	public static final String USER_EMAIL = "email";
	public static final String USER_SEX = "sex";
	public static final String USER_ADR = "adr";
	public static final String USER_INTRO = "intro";
	public static final String USER_MOBILE = "mobile";
	public static final String BITMAP = "bitmap";
	
	/**
	 * 公告表
	 */
	public static final String NEWSNOTICE = "table_newsnotice";
	
	

}

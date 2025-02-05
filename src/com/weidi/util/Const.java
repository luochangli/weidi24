package com.weidi.util;

import java.util.List;
import java.util.Map;

import com.weidi.bean.User;

public class Const {

	 public static final String XMPP_HOST = "120.25.157.196";
//	public static final String XMPP_HOST = "192.168.0.10";
//	 public static final String XMPP_HOST = "192.168.0.21";
	public static String XMPP_DOMAIN = "jsmny";
	public static final String RESOUCE_ID = "jsm"; // 资源名
	public static final int XMPP_PORT = 5222;
	public static String USER_ACCOUNT;
	public static String USER_PWD;
	public static User loginUser;
	public static final String YOU = "username";
	public static final String Male = "男";
	public static final String ExtraPath = "file:///";
	public static final String ACOUNT = "account";
	public static final String PASSWORD = "pwd";

	/**
	 * IQProvider 常量
	 */
	public static final String XMLNS = "com:jsm:group";
	public static final String QUERY = "query";
	public static final String Lord = "10";// 群主
	public static final String NEW_GROUP = "com:jsm:group#newgroup";
	public static final String Change_Nick = "com:jsm:group#changenick";
	public static final String NEW_MENBER = "com:jsm:group#newmember";
	public static final String GROUP_INFO = "com:jsm:group#groupinfo";

	/**
	 * IQProvider 备注常量
	 */
	public static final String XMLNS_REMARK = "com:jsm:remark";
	public static final String QUERY_REMARK = "query";
	
	
	/**
	 * IQProvider News常量
	 */
	public static final String XMLNS_News = "com:jsm:news";
	public static final String QUERY_News = "query";

	/**
	 * IQProvider User常量
	 */
	public static final String XMLNS_User = "com:jsm:user";
	public static final String QUERY_User = "query";

	public static final int HANDLER_PHONE_TO_WEIDI = 10001;
	/**
	 * 登录状态广播
	 */
	public static final String ACTION_IS_LOGIN_SUCCESS = "com.android.weidi.is_login_success";
	/**
	 * 消息记录操作广播
	 */
	public static final String ACTION_MSG_OPER = "com.android.weidi.msgoper";
	/**
	 * 添加好友请求广播
	 */
	public static final String ACTION_ADDFRIEND = "com.android.weidi.addfriend";
	/**
	 * 添加好友请求广播
	 */
	public static final String ACTION_DELETE_MSG = "com.android.weidi.delete_msg";
	/**
	 * 新消息广播
	 */
	public static final String ACTION_NEW_MSG = "com.android.weidi.newmsg";
	public static final String ACTION_NEW_FRIEND_MSG = "com.android.weidi.new_friend_msg";

	public static final String MSG_READED = "1";
	public static final String MSG_UNREAD = "0";

	// 静态地图API
	public static final String LOCATION_URL_S = "http://api.map.baidu.com/staticimage?width=320&height=240&zoom=17&center=";
	public static final String LOCATION_URL_L = "http://api.map.baidu.com/staticimage?width=480&height=800&zoom=17&center=";

	public static final String MSG_TYPE_TEXT = "text";// 文本消息
	public static final String MSG_TYPE_IMG = "img";// 图片
	public static final String MSG_TYPE_VOICE = "voice";// 语音
	public static final String MSG_TYPE_VIDEO = "video";// 视频
	public static final String MSG_TYPE_LOCATION = "location";// 位置
	public static final String MSG_TYPE_SET = "msg_type_set";// 绑定手机IQ

	public static final String MSG_TYPE_ADD_FRIEND = "msg_type_add_friend";// 添加好友
	public static final String MSG_TYPE_ADD_FRIEND_SUCCESS = "msg_type_add_friend_success";// 同意添加好友

	public static final String SPLIT = "卍";

	public static final int NOTIFY_ID = 0x90;
	public static final String UPLOAD_START = "1";// 上传文件开始
	public static final String UPLOAD_END = "0";// 上传文件完成
	/**
	 * 是否开启声音
	 */
	public static final String MSG_IS_VOICE = "msg_is_voice";
	/**
	 * 是否开启振动
	 */
	public static final String MSG_IS_VIBRATE = "msg_is_vibrate";

	/**
	 * 登录提示
	 */
	public static final int LOGIN_SECCESS = 0;// 成功
	public static final int HAS_NEW_VERSION = 1;// 发现新版本
	public static final int IS_NEW_VERSION = 2;// 当前版本为最新
	public static final int LOGIN_ERROR_ACCOUNT_PASS = 3;// 账号或者密码错误
	public static final int SERVER_UNAVAILABLE = 4;// 无法连接到服务器
	public static final int LOGIN_ERROR = 5;// 连接失败
	public static final int UNDEFINE = 6;// 连接失败
	public static final String LOGIN_PWD = "pwd";

	public static final String CODE_1 = "00001";
	public static final String CODE_2 = "00002";
	public static final String CODE_3 = "00003";
	public static final String CODE_4 = "00004";
	public static final String CODE_5 = "00005";
	public static final String CODE_6 = "00006";
	public static final String CODE_7 = "00007";
	public static final String CODE_8 = "00008";
	public static final String CODE_9 = "00009";
	public static final String CODE_10 = "00010";
	public static final String CODE_11 = "10000";
	
	//用于存储通知数据
	    public static  int  newscount;
		public static String NEWSNOTICE="newsnotice";

}

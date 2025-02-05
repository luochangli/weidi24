package com.weidi.bean;

import java.io.Serializable;
import java.util.Date;

/**
 * @author luochangdong E-mail: 2270333671@qq.com
 * @date 创建时间：2015-8-3 上午11:39:22
 * @Description 1.0
 */
public class ChatItem implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2376573627903430103L;
	public static final String TABLE_NAME = "chat_item";
	public static final String ID = "_id";
	public static final String IS_GROUP = "isGroup";
	public static final String USER_NICK = "usernick";
	public static final String ME = "from_user";
	public static final String TO = "to_user";
	public static final String MUC = "muc";
	public static final String CHAT_TYPE = "chatType";
	public static final String CONTENT = "content";
	public static final String IS_RECV = "isRecv";
	public static final String DATE = "date";
	public static final String FILE_STATUS = "fileStatus";
	public static final String VOICE_READED = "voiceReaded";
	public static final String VIEW_TYPE = "viewType";
	public static final String IS_READ = "isRead";
	public static final String BAK1 = "bak1";
	public static final String BAK2 = "bak2";
	public static final String BAK3 = "bak3";
	public static final String BAK4 = "bak4";
	public static final String BAK5 = "bak5";
	public static final String BAK6 = "bak6";
	public static final String BAK7 = "bak7";
	public static final int STATUS_0 = 0;
	public static final int STATUS_1 = 1;
	public static final int STATUS_2 = 2;
	public static final int OK = 3;

	private int _id;
	private int isGroup;// 是否为群聊 1 是 ,0 不是，2 通知
	private String usernick; // 用户昵称
	private String me;// 发送者的 jid
	private String to;// 接受者的 jid
	private String mucfrom;// 群的jid
	private String chatType;// 聊天内容类型
	private String content;// 聊天内容
	private int isRecv;// 是否为接受者 1接受者,0 发送者
	private String date; // 消息时间
	private int fileStatus; // 文件的状态 0  ，1 正在上传 、正在下载，2 失败，3完成
	private int voiceReaded;// 语音状态 1已读，0 未读。
	private int viewTyep; // listView的多item类型标识
	private int isRead; // 是否打开过对应的聊天窗口 是 1，否 0
	// 扩展
	private String bak1;
	private String bak2;
	private String bak3;
	private String bak4;
	private String bak5;
	private String bak6;
	private String bak7;

	public int get_id() {
		return _id;
	}

	public void set_id(int _id) {
		this._id = _id;
	}

	public int getIsGroup() {
		return isGroup;
	}

	public void setIsGroup(int isGroup) {
		this.isGroup = isGroup;
	}

	public String getUsernick() {
		return usernick;
	}

	public void setUsernick(String usernick) {
		this.usernick = usernick;
	}

	public String getMe() {
		return me;
	}

	public void setMe(String me) {
		this.me = me;
	}

	public String getTo() {
		return to;
	}

	public void setTo(String to) {
		this.to = to;
	}

	public String getMucFrom() {
		return mucfrom;
	}

	public void setMucFrom(String muc) {
		this.mucfrom = muc;
	}

	public String getChatType() {
		return chatType;
	}

	public void setChatType(String chatType) {
		this.chatType = chatType;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public int getIsRecv() {
		return isRecv;
	}

	public void setIsRecv(int isRecv) {
		this.isRecv = isRecv;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public int getFileStatus() {
		return fileStatus;
	}

	public void setFileStatus(int fileStatus) {
		this.fileStatus = fileStatus;
	}

	public int getVoiceReaded() {
		return voiceReaded;
	}

	public void setVoiceReaded(int voiceReaded) {
		this.voiceReaded = voiceReaded;
	}

	public int getViewTyep() {
		return viewTyep;
	}

	public void setViewTyep(int viewTyep) {
		this.viewTyep = viewTyep;
	}

	public int getIsRead() {
		return isRead;
	}

	public void setIsRead(int isRead) {
		this.isRead = isRead;
	}

	public String getBak1() {
		return bak1;
	}

	public void setBak1(String bak1) {
		this.bak1 = bak1;
	}

	public String getBak2() {
		return bak2;
	}

	public void setBak2(String bak2) {
		this.bak2 = bak2;
	}

	public String getBak3() {
		return bak3;
	}

	public void setBak3(String bak3) {
		this.bak3 = bak3;
	}

	public String getBak4() {
		return bak4;
	}

	public void setBak4(String bak4) {
		this.bak4 = bak4;
	}

	public String getBak5() {
		return bak5;
	}

	public void setBak5(String bak5) {
		this.bak5 = bak5;
	}

	public String getBak6() {
		return bak6;
	}

	public void setBak6(String bak6) {
		this.bak6 = bak6;
	}

	public String getBak7() {
		return bak7;
	}

	public void setBak7(String bak7) {
		this.bak7 = bak7;
	}

}

package com.weidi.db;

import org.jivesoftware.smack.Chat;

import com.weidi.bean.ChatItem;
import com.weidi.chat.bean.GroupInfo;
import com.weidi.chat.bean.Menbers;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * 
 * @author luochangdong
 */
public class DBHelper extends SQLiteOpenHelper {

	private static final String DB_NAME = "weidi_byl";
	private static final int DB_VERSION = 4;
	private static DBHelper mInstance;

	public DBHelper(Context context) {
		super(context, DB_NAME, null, DB_VERSION);
	}

	public DBHelper(Context context, int version) {
		super(context, DB_NAME, null, version);
	}

	public synchronized static DBHelper getInstance(Context context) {
		if (mInstance == null) {
			mInstance = new DBHelper(context);
		}
		return mInstance;
	};

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * android.database.sqlite.SQLiteOpenHelper#onCreate(android.database.sqlite
	 * .SQLiteDatabase)
	 */
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * android.database.sqlite.SQLiteOpenHelper#onCreate(android.database.sqlite
	 * .SQLiteDatabase)
	 */
	@Override
	public void onCreate(SQLiteDatabase db) {

		String sql_menbers = "Create table IF NOT EXISTS " + Menbers.TABLE
				+ "(" + Menbers.ID + " integer primary key autoincrement, "
				+ Menbers.AFFILIATION + " text," + Menbers.JID + " text,"
				+ Menbers.MUC + " text," + Menbers.NICK + " text," + Menbers.ME
				+ " text);";
		String sql_groupInfo = "Create table IF NOT EXISTS " + GroupInfo.TABLE
				+ "(" + GroupInfo.ID + "  integer primary key autoincrement, "
				+ GroupInfo.CREATEDATE + " text," + GroupInfo.DESCRIPTION
				+ " text," + GroupInfo.MUC + " text," + GroupInfo.NAME
				+ " text," + GroupInfo.ME + " text);";

		String sql_new_msg = "Create table IF NOT EXISTS "
				+ ChatItem.TABLE_NAME + "(" + ChatItem.ID
				+ "  integer primary key autoincrement, " + ChatItem.IS_GROUP
				+ " integer," + ChatItem.USER_NICK + " text," + ChatItem.ME
				+ " text," + ChatItem.TO + " text," + ChatItem.MUC + " text,"
				+ ChatItem.CHAT_TYPE + " text," + ChatItem.CONTENT + " text,"
				+ ChatItem.IS_RECV + " integer," + ChatItem.DATE + " text,"
				+ ChatItem.FILE_STATUS + " integer," + ChatItem.VOICE_READED
				+ " integer," + ChatItem.VIEW_TYPE + " integer,"
				+ ChatItem.IS_READ + " integer," + ChatItem.BAK1 + " text,"
				+ ChatItem.BAK2 + " text," + ChatItem.BAK3 + " text,"
				+ ChatItem.BAK4 + " text," + ChatItem.BAK5 + " text,"
				+ ChatItem.BAK6 + " text," + ChatItem.BAK7 + " text);";

		/**
		 * 聊天记录
		 */
		String sql_msg = "Create table IF NOT EXISTS " + DBcolumns.TABLE_MSG
				+ "(" + DBcolumns.MSG_ID
				+ " integer primary key autoincrement," + DBcolumns.MSG_FROM
				+ " text," + DBcolumns.MSG_TO + " text," + DBcolumns.MSG_TYPE
				+ " text," + DBcolumns.MSG_CONTENT + " text,"
				+ DBcolumns.MSG_ISCOMING + " integer," + DBcolumns.MSG_DATE
				+ " text," + DBcolumns.MSG_ISREADED + " text,"
				+ DBcolumns.MSG_BAK1 + " text," + DBcolumns.MSG_BAK2 + " text,"
				+ DBcolumns.MSG_BAK3 + " text," + DBcolumns.MSG_BAK4 + " text,"
				+ DBcolumns.MSG_BAK5 + " text," + DBcolumns.MSG_BAK6
				+ " text);";

		/**
		 * 最近联系人
		 */
		String sql_session = "Create table IF NOT EXISTS "
				+ DBcolumns.TABLE_SESSION + "(" + DBcolumns.SESSION_id
				+ " integer primary key AUTOINCREMENT,"
				+ DBcolumns.SESSION_FROM + " text," + DBcolumns.SESSION_TYPE
				+ " text," + DBcolumns.SESSION_TIME + " text,"
				+ DBcolumns.SESSION_TO + " text," + DBcolumns.SESSION_CONTENT
				+ " text," + DBcolumns.SESSION_ISDISPOSE + " text);";

		String sql_notice = "Create table IF NOT EXISTS "
				+ DBcolumns.TABLE_SYS_NOTICE + "(" + DBcolumns.SYS_NOTICE_ID
				+ " integer primary key AUTOINCREMENT,"
				+ DBcolumns.SYS_NOTICE_TYPE + " text,"
				+ DBcolumns.SYS_NOTICE_FROM + " text,"
				+ DBcolumns.SYS_NOTICE_FROM_HEAD + " text,"
				+ DBcolumns.SYS_NOTICE_CONTENT + " text,"
				+ DBcolumns.SYS_NOTICE_ISDISPOSE + " text);";

		String sql_vcard = "create table if not exists "
				+ DBcolumns.TABLE_VCARD
				+ "(vcard_id integer primary key autoincrement,"
				+ "username text, nickname text,truename text,email text,"
				+ " intro text, sex text,adr text," + " mobile text,"+"bitmap BLOB)";
		/**
		 * 添加好友管理 邀请与被邀请
		 */
		String sql_newFriend = "CREATE TABLE  IF NOT EXISTS "
				+ DBcolumns.TABLE_NEWFRIEND
				+ "( id INTEGER PRIMARY KEY AUTOINCREMENT,"
				+ "username text ,sendDate text,isDeal INTEGER,"
				+ "whos text,i_filed INTEGER,t_field text)";
		/**
		 * 新消息管理表 记录谁有几条未读消息
		 */
		String sql_msgCount = "CREATE TABLE  IF NOT EXISTS "
				+ DBcolumns.TABLE_NEW_MSG_COUNT
				+ "( id INTEGER PRIMARY KEY AUTOINCREMENT,msgId text,msgCount INTEGER, whosMsg text,"
				+ "i_field1 INTEGER, t_field1 text)";

		/**
		 * 公告管理表
		 */
		String sql_newsNotice = "create table if not exists "
				+ DBcolumns.NEWSNOTICE
				+ "(news_id text,"
				+ "title text, link text,imglink text,createdatetime text,content text,"
		        + " isread text DEFAULT 0)";

		db.execSQL(sql_msg);
		db.execSQL(sql_session);
		db.execSQL(sql_notice);
		db.execSQL(sql_vcard);
		db.execSQL(sql_newFriend);
		db.execSQL(sql_msgCount);
		db.execSQL(sql_newsNotice);
		db.execSQL(sql_new_msg);
		db.execSQL(sql_groupInfo);
		db.execSQL(sql_menbers);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

	}

}

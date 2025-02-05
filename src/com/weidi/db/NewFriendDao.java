package com.weidi.db;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.weidi.QApp;
import com.weidi.common.DateUtil;
import com.weidi.util.Const;
import com.weidi.util.Logger;

/**
 *@author  luochangdong  E-mail: 2270333671@qq.com
 *@date 创建时间：2015-6-25 下午1:50:56
 *@Description 1.0
 */
public class NewFriendDao {
	private static String TAG = "NewFriendDao";
	private static NewFriendDao instance = null;
    public static final int UNAGREE = 0;
	private DBHelper helper;
	private SQLiteDatabase db;  // 邀请我的
	
	public NewFriendDao(Context context) {
		helper = new DBHelper(context);
	
	}

	public NewFriendDao(Context context, int version) {
		helper = new DBHelper(context, version);
	}
	
	public static NewFriendDao getInstance() {
		if (instance == null) {
			instance = new NewFriendDao(QApp.getInstance());
		}
		return instance;
	}
	
	public void saveNewFriend(String username){
		db = helper.getWritableDatabase();
		int nowCount = getCount(username);
		ContentValues values = new ContentValues();
		if (nowCount == 0) {
			values.put("username", username);
			values.put("sendDate",DateUtil.getCurDateStr( "MM-dd  HH:mm:ss"));
			values.put("whos", Const.USER_ACCOUNT);
			values.put("isDeal", UNAGREE);//
			db.insert(DBcolumns.TABLE_NEWFRIEND, "id", values);
		}
		else{
			values.put("sendDate", DateUtil.getCurDateStr( "MM-dd  HH:mm:ss"));
			values.put("isDeal", 0);//已处理
			db.update(DBcolumns.TABLE_NEWFRIEND, values, " username=? and whos=?", 
					new String[]{username,Const.USER_ACCOUNT});
		}
//		NewMsgCountDao.getInstance(QApp.getInstance()).saveNewMsg(""+0);
		Logger.e(TAG, "saveNewFriend");
		db.close();
//		int friendId=queryTheLastMsgId();//返回新插入记录的id
//		return friendId;
	}
	
	public void delFriend(String username){
		db = helper.getWritableDatabase();
		ContentValues values = new ContentValues();  
		values.put("isDeal", 1);//状态已处理完 
		db.update(DBcolumns.TABLE_NEWFRIEND, values, " username=? and whos=?", 
				new String[]{username,Const.USER_ACCOUNT});
		db.close();
	}

	/**
	 * 取邀请我的
	 */
	public List<String> getNewFriend(){
		db = helper.getWritableDatabase();
		List<String> friends = new ArrayList<String>();
		String sql = "select username from " +DBcolumns.TABLE_NEWFRIEND +
				" where whos = ? order by sendDate desc";
		Cursor cursor = db.rawQuery(sql, new String[]{Const.USER_ACCOUNT});
		
		while(cursor.moveToNext()){
			friends.add(cursor.getString(0));
		}
	
		cursor.close();
		db.close();
		return friends;
	}
	
	public int getUnDealCount(){
		db = helper.getWritableDatabase();
		int count = 0 ;
		String sql ="select count(0) from "+DBcolumns.TABLE_NEWFRIEND+" where isDeal=? and whos=?";
		Cursor cursor = db.rawQuery(sql, new String[]{"0",Const.USER_ACCOUNT});
		while(cursor.moveToNext()){
			count = cursor.getInt(0);
		}
		cursor.close();
		db.close();
		Logger.e(TAG, ""+count);
		return count;
	}
	//某个人
	public int getCount(String username){   
	
		int count = 0 ;
		String sql ="select count(0) from "+DBcolumns.TABLE_NEWFRIEND+" where username=? and whos=?";
		Cursor cursor = db.rawQuery(sql, new String[]{username,Const.USER_ACCOUNT});
	
		while(cursor.moveToNext()){
			count = cursor.getInt(0);
		}
		cursor.close();
		return count;
	}
	
	//某个人是否已处理
	public boolean isDeal(String username){   
		db = helper.getWritableDatabase();
		boolean isDeal = false ;
		String sql ="select isDeal from "+DBcolumns.TABLE_NEWFRIEND+" where username=? and whos=?";
		Cursor cursor = db.rawQuery(sql, new String[]{username,Const.USER_ACCOUNT});
		
		while(cursor.moveToNext()){
			if (cursor.getInt(0) == UNAGREE) {
				isDeal = false;
			}
			else {
				isDeal = true;
			}
		}

		cursor.close();
		db.close();
		return isDeal;
	}
	
	
	public void clear(){
		db = helper.getWritableDatabase();
		db.delete(DBcolumns.TABLE_NEWFRIEND, "id>?", new String[]{"0"}); 
		db.close();
	}
}

package com.weidi.chat.bean;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.weidi.QApp;
import com.weidi.db.DBHelper;
import com.weidi.util.Const;

/**
 * @author luochangdong E-mail: 2270333671@qq.com
 * @date 创建时间：2015-8-10 上午11:54:27
 * @Description 1.0
 */
public class GroupInfoDao {

	private DBHelper helper;
	private static GroupInfoDao instance;

	public static GroupInfoDao getInstance() {
		if (instance == null) {
			instance = new GroupInfoDao(QApp.getInstance());
		}
		return instance;
	}

	public GroupInfoDao(Context context) {
		helper = new DBHelper(context);
	}

	public GroupInfoDao(Context context, int version) {
		helper = new DBHelper(context, version);
	}

	public long insert(GroupInfo item) {
		SQLiteDatabase db = helper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(GroupInfo.MUC, item.getMuc());
		values.put(GroupInfo.NAME, item.getName());
		values.put(GroupInfo.DESCRIPTION, item.getDescription());
		values.put(GroupInfo.CREATEDATE, item.getCreatedatetime());
		values.put(GroupInfo.ME, item.getMe());

		long row = db.insert(GroupInfo.TABLE, null, values);

		db.close();

		return row;
	}

	/**
	 * 删除一个成员
	 * 
	 * @return
	 */
	public long deleteMenById(GroupInfo item) {
		SQLiteDatabase db = helper.getWritableDatabase();
		long row = db.delete(GroupInfo.TABLE, GroupInfo.MUC + " = ? AND "
				+ GroupInfo.ME + " =?",
				new String[] { item.getMuc(), item.getMe() });
		db.close();
		return row;
	}

	public GroupInfo queryByMuc(String muc) {

		SQLiteDatabase db = helper.getWritableDatabase();
		String selection = GroupInfo.MUC + " =? AND " + GroupInfo.ME +" =? ";
		String[] selectionArgs = new String[] { muc, Const.USER_ACCOUNT };
		Cursor cursor = db.query(GroupInfo.TABLE, null, selection,
				selectionArgs, null, null, null);
		GroupInfo temp = null;
		while (cursor.moveToNext()) {
			temp = new GroupInfo();
			temp.set_id(cursor.getInt(cursor.getColumnIndex(Menbers.ID)));
			temp.setCreatedatetime(cursor.getString(cursor
					.getColumnIndex(GroupInfo.CREATEDATE)));
			temp.setDescription(cursor.getString(cursor
					.getColumnIndex(GroupInfo.DESCRIPTION)));
			temp.setMuc(cursor.getString(cursor.getColumnIndex(Menbers.MUC)));
			temp.setName(cursor.getString(cursor.getColumnIndex(GroupInfo.NAME)));
			temp.setMe(cursor.getString(cursor.getColumnIndex(GroupInfo.ME)));

		}
		return temp;

	}

	public int updateMenb(GroupInfo item) {
		SQLiteDatabase db = helper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(GroupInfo.NAME, item.getName());
		String whereClause = GroupInfo.MUC + " =? "+GroupInfo.ME;
		String[] whereArgs = new String[] { item.getMuc(),item.getMe()};

		return db.update(GroupInfo.TABLE, values, whereClause, whereArgs);
	}

}

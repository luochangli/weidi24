package com.weidi.chat.bean;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.weidi.QApp;
import com.weidi.db.DBHelper;
import com.weidi.util.Const;

/**
 * @author luochangdong E-mail: 2270333671@qq.com
 * @date 创建时间：2015-8-10 上午11:54:38
 * @Description 1.0
 */
public class MenbersDao {
	private DBHelper helper;
	private static MenbersDao instance;

	public static MenbersDao getInstance() {
		if (instance == null) {
			instance = new MenbersDao(QApp.getInstance());
		}
		return instance;
	}

	public MenbersDao(Context context) {
		helper = new DBHelper(context);
	}

	public MenbersDao(Context context, int version) {
		helper = new DBHelper(context, version);
	}

	/**
	 * 添加新信息
	 * 
	 * @param ChatItem
	 */
	public long insert(Menbers item) {
		SQLiteDatabase db = helper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(Menbers.MUC, item.getMuc());
		values.put(Menbers.NICK, item.getNick());
		values.put(Menbers.AFFILIATION, item.getAffiliation());
		values.put(Menbers.JID, item.getJid());
		values.put(Menbers.ME, item.getMe());

		long row = db.insert(Menbers.TABLE, null, values);

		db.close();

		return row;
	}

	/**
	 * 清空所有记录
	 */
	public void deleteTableData() {
		SQLiteDatabase db = helper.getWritableDatabase();
		db.delete(Menbers.TABLE, null, null);
		db.close();
	}

	/**
	 * 删除一个成员
	 * 
	 * @return
	 */
	public long deleteMenById(Menbers item) {
		SQLiteDatabase db = helper.getWritableDatabase();
		long row = db.delete(Menbers.TABLE, Menbers.MUC + " = ? AND "
				+ Menbers.JID + " =? AND " + Menbers.ME + " =? ", new String[] {
				item.getMuc(), item.getJid(), item.getMe() });
		db.close();
		return row;
	}

	public long deleteGroupBymuc(String muc) {
		SQLiteDatabase db = helper.getWritableDatabase();
		long row = db.delete(Menbers.TABLE, Menbers.MUC + " = ? AND "
				+ Menbers.ME + " =? ", new String[] { muc, Const.USER_ACCOUNT });
		db.close();
		return row;
	}

	public Menbers queryItem(String muc, String to) {
		SQLiteDatabase db = helper.getWritableDatabase();
		String selection = Menbers.MUC + " =? AND " + Menbers.ME + " =? AND "
				+ Menbers.JID + " =? ";
		String[] selectionArgs = new String[] { muc, Const.USER_ACCOUNT ,to};
		Cursor cursor = db.query(Menbers.TABLE, null, selection, selectionArgs,
				null, null, null);
		Menbers item = null;
		while (cursor.moveToNext()) {
			item = new Menbers();

			item.set_id(cursor.getInt(cursor.getColumnIndex(Menbers.ID)));
			item.setAffiliation(cursor.getString(cursor
					.getColumnIndex(Menbers.AFFILIATION)));
			item.setJid(cursor.getString(cursor.getColumnIndex(Menbers.JID)));
			item.setMuc(cursor.getString(cursor.getColumnIndex(Menbers.MUC)));
			item.setNick(cursor.getString(cursor.getColumnIndex(Menbers.NICK)));
			item.setMe(cursor.getString(cursor.getColumnIndex(Menbers.ME)));
		}
		return item;
	}

	public List<Menbers> queryAllByMuc(String muc) {
		List<Menbers> items = new ArrayList<Menbers>();
		SQLiteDatabase db = helper.getWritableDatabase();
		String selection = Menbers.MUC + " =? AND " + Menbers.ME + " =? ";
		String[] selectionArgs = new String[] { muc, Const.USER_ACCOUNT };
		Cursor cursor = db.query(Menbers.TABLE, null, selection, selectionArgs,
				null, null, null);
		Menbers item = null;
		while (cursor.moveToNext()) {
			item = new Menbers();

			item.set_id(cursor.getInt(cursor.getColumnIndex(Menbers.ID)));
			item.setAffiliation(cursor.getString(cursor
					.getColumnIndex(Menbers.AFFILIATION)));
			item.setJid(cursor.getString(cursor.getColumnIndex(Menbers.JID)));
			item.setMuc(cursor.getString(cursor.getColumnIndex(Menbers.MUC)));
			item.setNick(cursor.getString(cursor.getColumnIndex(Menbers.NICK)));
			item.setMe(cursor.getString(cursor.getColumnIndex(Menbers.ME)));
			items.add(item);

		}
		return items;

	}

	public int updateMenb(Menbers item) {
		SQLiteDatabase db = helper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(Menbers.NICK, item.getNick());
		String whereClause = Menbers.MUC + " =? AND " + Menbers.JID
				+ " =? AND " + Menbers.ME + " =? ";
		String[] whereArgs = new String[] { item.getMuc(), item.getJid(),
				Const.USER_ACCOUNT };

		return db.update(Menbers.TABLE, values, whereClause, whereArgs);
	}

}

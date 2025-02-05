package com.weidi.common;

import java.io.File;

import com.weidi.util.Logger;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.provider.MediaStore.MediaColumns;
import android.util.Log;
import android.widget.Toast;

/**
 * @author luochangdong E-mail: 2270333671@qq.com
 * @date 创建时间：2015-8-3 下午5:36:24
 * @Description 1.0 通过路径获取媒体文件的信息
 */
public class MediaInfoProvider {

	/**
	 * context
	 */
	private Context mContext = null;

	/**
	 * data path
	 */
	private static final String dataPath = "/mnt";

	/**
	 * query column
	 */
	private static final String[] mCursorCols = new String[] {
			MediaStore.Audio.Media._ID, MediaStore.Audio.Media.DISPLAY_NAME,
			MediaStore.Audio.Media.TITLE, MediaStore.Audio.Media.DURATION,
			MediaStore.Audio.Media.ARTIST, MediaStore.Audio.Media.ALBUM,
			MediaStore.Audio.Media.YEAR, MediaStore.Audio.Media.MIME_TYPE,
			MediaStore.Audio.Media.SIZE, MediaStore.Audio.Media.DATA };

	/**
	 * MediaInfoProvider
	 * 
	 * @param context
	 */
	public MediaInfoProvider(Context context) {
		this.mContext = context;
	}

	/**
	 * get the media file info by path
	 * 
	 * @param filePath
	 * @return
	 */
	public MediaInfo getMediaInfo(String filePath) {

		/* check a exit file */
		File file = new File(filePath);
		if (!file.exists()) {
			Toast.makeText(mContext, "sorry, the file is not exit!",
					Toast.LENGTH_SHORT);
		}
		Logger.e("TAG", file.getName());
		/* create the query URI, where, selectionArgs */
		Uri Media_URI = null;
		String where = null;
		String selectionArgs[] = null;

		if (filePath.startsWith("content://media/")) {
			/* content type path */
			Media_URI = Uri.parse(filePath);
			where = null;
			selectionArgs = null;
		} else {
			/* external file path */
			// if(filePath.indexOf(dataPath) < 0) {
			// filePath = dataPath + filePath;
			// }

			Media_URI = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
			where = MediaColumns.DATA + "=? ";
			selectionArgs = new String[] { filePath };
		}

		/* query */
		Cursor cursor = mContext.getContentResolver().query(Media_URI,
				mCursorCols, where, selectionArgs, MediaStore.Audio.Media.DEFAULT_SORT_ORDER);
		if (cursor == null || cursor.getCount() == 0) {
			return null;
		} else {
			cursor.moveToFirst();
			MediaInfo info = getInfoFromCursor(cursor);
			printInfo(info);
			return info;
		}
	}

	/**
	 * get the media info beans from cursor
	 * 
	 * @param cursor
	 * @return
	 */
	private MediaInfo getInfoFromCursor(Cursor cursor) {
		MediaInfo info = new MediaInfo();

		/* file name */
		if (cursor.getString(1) != null) {
			info.setmFileName(cursor.getString(1));
		}
		/* media name */
		if (cursor.getString(2) != null) {
			info.setMediaName(cursor.getString(2));
		}
		/* play duration */
		if (cursor.getString(3) != null) {
			info.setPlayDuration(cursor.getInt(3));
		}
		/* artist */
		if (cursor.getString(4) != null) {
			info.setMediaArtist(cursor.getString(4));
		}
		/* album */
		if (cursor.getString(5) != null) {
			info.setMediaAlbum(cursor.getString(5));
		}
		/* media year */
		if (cursor.getString(6) != null) {
			info.setMediaYear(cursor.getString(6));
		} else {
			info.setMediaYear("undefine");
		}
		/* media type */
		if (cursor.getString(7) != null) {
			info.setmFileType(cursor.getString(7).trim());
		}
		/* media size */
		if (cursor.getString(8) != null) {
			float temp = cursor.getInt(8) / 1024f / 1024f;
			String sizeStr = (temp + "").substring(0, 4);
			info.setmFileSize(sizeStr + "M");
		} else {
			info.setmFileSize("undefine");
		}
		/* media file path */
		if (cursor.getString(9) != null) {
			info.setmFilePath(cursor.getString(9));
		}

		return info;
	}

	/**
	 * print media info
	 * 
	 * @param info
	 */
	private void printInfo(MediaInfo info) {
		// TODO Auto-generated method stub
		Log.i("playDuration", "" + info.getPlayDuration());
		Log.i("mediaName", "" + info.getMediaName());
		Log.i("mediaAlbum", "" + info.getMediaAlbum());
		Log.i("mediaArtist", "" + info.getMediaArtist());
		Log.i("mediaYear", "" + info.getMediaYear());
		Log.i("fileName", "" + info.getmFileName());
		Log.i("fileType", "" + info.getmFileType());
		Log.i("fileSize", "" + info.getmFileSize());
		Log.i("filePath", "" + info.getmFilePath());
	}
}

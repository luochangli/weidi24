package com.weidi.common;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.Activity;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.widget.EditText;

import com.weidi.activity.RegisterActi;
import com.weidi.util.Logger;

/**
 * @author luochangdong E-mail: 2270333671@qq.com
 * @date 创建时间：2015-7-19 下午2:12:49
 * @Description 1.0
 */
public class SmsContent extends ContentObserver {

	private static String TAG = "SmsContent";
	public static final String SMS_URI_INBOX = "content://sms/inbox";
	private static final int LENGTH = 5;
	private Activity activity = null;
	private String smsContent = "";
	private EditText verifyText = null;
    private Handler handler;
	public SmsContent(Activity activity, Handler handler, EditText verifyText) {
		super(handler);
		this.activity = activity;
		this.verifyText = verifyText;
		this.handler = handler;
	}

	@SuppressWarnings("deprecation")
	@Override
	public void onChange(boolean selfChange) {
		super.onChange(selfChange);
		Cursor cursor = null;// 光标
		// 读取收件箱中指定号码的短信
		cursor = activity.managedQuery(Uri.parse(SMS_URI_INBOX), new String[] {
				"_id", "address", "body", "read" }, " read=?",
				new String[] {"0" }, "date desc");
		if (cursor != null && cursor.getCount() > 0) {// 如果短信为未读模式
			cursor.moveToFirst();
			if (cursor.moveToFirst()) {
				String smsbody = cursor
						.getString(cursor.getColumnIndex("body"));
				cursor.close();
				if(!smsbody.contains("掌上微迪"))
				return;
				Logger.i(TAG,smsbody);
				String regEx = "[^0-9]";
				Pattern p = Pattern.compile(regEx);
				Matcher m = p.matcher(smsbody.toString());
				smsContent = m.replaceAll("").trim().toString();
				verifyText.setText(smsContent);
				handler.sendEmptyMessage(RegisterActi.AUTH_CODE);
			}
		}
		cursor.close();

	}
}

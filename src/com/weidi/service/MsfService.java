package com.weidi.service;

import org.jivesoftware.smack.XMPPConnection;

import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import com.weidi.util.XmppUtil;

public class MsfService extends Service {

	private static String TAG = "MsfService";
	private static MsfService mInstance = null;

	private NotificationManager mNotificationManager;
	private XMPPConnection mXmppConnection;

	private final IBinder binder = new MyBinder();

	public class MyBinder extends Binder {
		public MsfService getService() {
			return MsfService.this;
		}
	}

	@Override
	public IBinder onBind(Intent intent) {
		return binder;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		mInstance = this;
		mNotificationManager = (NotificationManager) getSystemService(android.content.Context.NOTIFICATION_SERVICE); // 通知
	
		initXMPPTask();

	}

	private void initXMPPTask() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					initXMPP();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}).start();
	}

	private void initXMPP() {
		mXmppConnection = XmppUtil.getInstance().getConnection();

	}

	public static MsfService getInstance() {
		return mInstance;
	}

	@Override
	public void onDestroy() {

		if (mNotificationManager != null) {

		}
		try {
			if (mXmppConnection != null) {
				XmppUtil.getInstance().closeConnection();
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		super.onDestroy();
	}
}

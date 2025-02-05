package com.weidi.listener;

import java.util.Timer;

import org.jivesoftware.smack.ConnectionListener;
import org.jivesoftware.smackx.packet.MUCOwner.Destroy;

import android.content.Intent;
import android.util.Log;

import com.weidi.QApp;
import com.weidi.activity.LoginActivity;
import com.weidi.util.Logger;
import com.weidi.util.ToastUtil;
import com.weidi.util.XmppConnectionManager;
import com.weidi.util.XmppUtil;

/**
 * @author baiyuliang
 */
public class CheckConnectionListener implements ConnectionListener {
	Timer tExit;
	
   public CheckConnectionListener(){
	   Logger.e("tag", "监听创建");
   }

	@Override
	public void connectionClosed() {
         XmppConnectionManager.getInstance().setConnNull();
	}

	@Override
	public void connectionClosedOnError(Exception e) {

		Log.i("connection--", e.toString());

		boolean error = e.getMessage().equals("stream:error (conflict)");
		if (!error) {
			try {
				XmppUtil.getInstance().closeConnection();
			} catch (Exception e2) {
				Log.i("connectionClosedOnError", e2.toString());
			} finally {
				tExit = new Timer();
				tExit.schedule(new XmppTimetask(), 5000);
			}

		} else {
			// 跳转
			Intent intent = new Intent();
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			// intent.putExtra("isRelogin", true);
			intent.setClass(QApp.getInstance(), LoginActivity.class);
			QApp.getInstance().startActivity(intent);
			ToastUtil.showShortLuo("您的帐号已在别处登录了！");
		}
	}

	@Override
	public void reconnectingIn(int arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void reconnectionFailed(Exception arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void reconnectionSuccessful() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void finalize() throws Throwable {
		Logger.e("tag", "监听释放");
		super.finalize();
	}
	
	

}

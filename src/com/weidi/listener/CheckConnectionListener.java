package com.weidi.listener;

import org.jivesoftware.smack.ConnectionListener;

import android.util.Log;

import com.weidi.QApp;
import com.weidi.util.ToastUtil;
import com.weidi.util.XmppConnectionManager;


/**
 * @author baiyuliang
 */
public class CheckConnectionListener implements ConnectionListener{
	

	@Override
	public void connectionClosed() {
		Log.i("RRRRRRRRRRRRR", "手动关闭网络");
		XmppConnectionManager.getInstance().setConnNull();
	}

	@Override
	public void connectionClosedOnError(Exception e) {  
		if (e.getMessage().equals("conflict")) {
			ToastUtil.showLongToast(QApp.getInstance(), "您的账号在异地登录");
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

}

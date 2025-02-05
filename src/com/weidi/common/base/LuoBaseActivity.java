package com.weidi.common.base;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.LocalBroadcastManager;
import android.view.WindowManager;

import com.weidi.QApp;

/**
 *@author  luochangdong  E-mail: 2270333671@qq.com
 *@date 创建时间：2015-6-8 下午5:19:17
 *@Description 1.0
 */
public abstract class LuoBaseActivity extends FragmentActivity{
	protected QApp mApp;
	protected LocalBroadcastManager mLocalBroadcastManager;
	protected Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			handleMsg(msg);
		};
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mApp = QApp.getInstance();
		mLocalBroadcastManager = LocalBroadcastManager.getInstance(this);
	
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		initView(savedInstanceState);
		setListener();
		afterViews(savedInstanceState);
	}

	/**
	 * 初始化布局以及View控件
	 */
	protected abstract void initView(Bundle savedInstanceState);

	/**
	 * 给View控件添加事件监听器
	 */
	protected abstract void setListener();

	/**
	 * 处理业务逻辑，状态恢复等操作
	 * 
	 * @param savedInstanceState
	 */
	protected abstract void afterViews(Bundle savedInstanceState);

	@Override
	protected void onDestroy() {
		super.onDestroy();
	
	}

	/**处理handler发送的消息
	 * @param msg
	 */
	protected abstract void handleMsg(Message msg);
	
	}

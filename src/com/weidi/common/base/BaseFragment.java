package com.weidi.common.base;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.weidi.QApp;

/**
 * @author luochangdong E-mail: 2270333671@qq.com
 * @date 创建时间：2015-6-8 下午5:19:17
 * @Description 1.0
 */
public abstract class BaseFragment extends Fragment {

	protected QApp mApp;
	protected Context mContext;
	protected LocalBroadcastManager mLocalBroadcastManager;
	protected BroadcastReceiver mReceiver;
	protected View mRootView;
	protected Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			handleMsg(msg);
		};
	};

	public void onAttach(Activity activity) {
		super.onAttach(activity);
		mApp = QApp.getInstance();
		mLocalBroadcastManager = LocalBroadcastManager.getInstance(activity);
	};

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		if (mRootView == null) {
			mContext = getActivity();
			initView(savedInstanceState);
			setListener();
			afterViews(savedInstanceState);
		} else {
			ViewGroup parent = (ViewGroup) mRootView.getParent();
			if (parent != null) {
				parent.removeView(mRootView);
			}
		}
		return mRootView;
	}

	protected void setRootView(int layoutResID) {
		mRootView = LayoutInflater.from(mApp).inflate(layoutResID, null);
	}

	/**
	 * 处理handler发送的消息
	 * 
	 * @param msg
	 */
	protected abstract void handleMsg(Message msg);

	/**
	 * 初始化View控件
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

}

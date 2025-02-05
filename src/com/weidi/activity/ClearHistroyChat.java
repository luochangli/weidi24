package com.weidi.activity;



import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;

import com.weidi.R;
import com.weidi.db.ChatMsgDao;
import com.weidi.db.SessionDao;
import com.weidi.util.Const;
import com.weidi.util.ToastUtil;
import com.weidi.view.TitleBarView;

public class ClearHistroyChat extends Activity implements OnClickListener {
	TitleBarView mTitleBarView;
	RelativeLayout rl_msg_voice,rl_msg_vibrate;
	ChatMsgDao chatMsgDao;
	SessionDao sessionDao;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_clearhistroy);
		chatMsgDao=new ChatMsgDao(this);
		sessionDao=new SessionDao(this);
		initView();
		initTitleView();
		
	}
	
	private void initView() {
		mTitleBarView=(TitleBarView)findViewById(R.id.title_bar);
		 rl_msg_voice=(RelativeLayout) findViewById(R.id.rl_msg_voice);//清空消息列表
		 rl_msg_vibrate=(RelativeLayout) findViewById(R.id.rl_msg_vibrate);//清空所有聊天记录
	     rl_msg_voice.setOnClickListener(this);
		 rl_msg_vibrate.setOnClickListener(this);
		
	}

	private void initTitleView(){
		mTitleBarView.setCommonTitle(View.VISIBLE, View.VISIBLE,View.GONE,View.GONE, View.GONE);
		mTitleBarView.setTitleText(R.string.histroy);
		mTitleBarView.setBtnLeftIcon(R.drawable.btn_back_nomal);
		mTitleBarView.setBtnLeftOnclickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				finish();
				
			}
		});

	}

	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {//返回
		case R.id.rl_msg_voice://清空消息列表
			sessionDao.deleteTableData();
			sendBroadcast(new Intent(Const.ACTION_NEW_MSG));
			ToastUtil.showShortToast(this, "消息列表已清空");
			break;
		case R.id.rl_msg_vibrate://清空所有聊天记录
			ToastUtil.showShortToast(this, "聊天记录已清空");
			sendBroadcast(new Intent(Const.ACTION_NEW_MSG));
			chatMsgDao.deleteTableData();
			break;
	}
		
	}

}

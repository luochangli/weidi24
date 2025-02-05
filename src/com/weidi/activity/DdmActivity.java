package com.weidi.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.RelativeLayout;

import com.weidi.R;
import com.weidi.util.Const;
import com.weidi.util.PreferencesUtils;
import com.weidi.view.TitleBarView;

public class DdmActivity extends Activity implements OnClickListener{

	TitleBarView mTitleBarView;
	RelativeLayout rl_msg_voice,rl_msg_vibrate;
	CheckBox cb_msg_voice,cb_msg_vibrate;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ddm);
		initView();
		initTitleView();
	}

	private void initView() {
		mTitleBarView=(TitleBarView)findViewById(R.id.title_bar);
		rl_msg_voice=(RelativeLayout)findViewById(R.id.rl_msg_voice);
		rl_msg_vibrate=(RelativeLayout)findViewById(R.id.rl_msg_vibrate);
		cb_msg_voice=(CheckBox) findViewById(R.id.cb_msg_voice);
	    cb_msg_vibrate=(CheckBox) findViewById(R.id.cb_msg_vibrate);
	    
	    
	    if(PreferencesUtils.getSharePreBoolean(Const.MSG_IS_VOICE)){//声音
	    	cb_msg_voice.setChecked(true);
	    }
	    if(PreferencesUtils.getSharePreBoolean(Const.MSG_IS_VIBRATE)){//振动.
	    	cb_msg_vibrate.setChecked(true);
	    }
	    
	    rl_msg_voice.setOnClickListener(this);
		rl_msg_vibrate.setOnClickListener(this);
		
	}
	
	private void initTitleView(){
		mTitleBarView.setCommonTitle(View.VISIBLE, View.VISIBLE,View.GONE,View.GONE, View.GONE);
		mTitleBarView.setTitleText(R.string.ddm);
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
		case R.id.rl_msg_voice://声音
			if(cb_msg_voice.isChecked()){
				cb_msg_voice.setChecked(false);
				PreferencesUtils.putSharePre( Const.MSG_IS_VOICE, false);
			}else{
				cb_msg_voice.setChecked(true);
				PreferencesUtils.putSharePre(Const.MSG_IS_VOICE, true);
			}
			break;
		case R.id.rl_msg_vibrate://振动
			if(cb_msg_vibrate.isChecked()){
				cb_msg_vibrate.setChecked(false);
				PreferencesUtils.putSharePre(Const.MSG_IS_VIBRATE, false);
			}else{
				cb_msg_vibrate.setChecked(true);
				PreferencesUtils.putSharePre(Const.MSG_IS_VIBRATE, true);
			}
			break;
	}
		
	}
}

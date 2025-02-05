package com.weidi.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.weidi.QApp;
import com.weidi.R;
import com.weidi.util.PreferencesUtils;
import com.weidi.util.ToastUtil;
import com.weidi.util.XmppUtil;
import com.weidi.view.LoadingDialog;


/**
 * 
 * @author 白玉梁
 */
public class SignActivity extends Activity implements OnClickListener{
	
	private ImageView go_back;
	private Button btn_ok;
	private EditText sign_content;
	private LoadingDialog loadingDialog;
	
	
	@SuppressLint("HandlerLeak")
	private final Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if(loadingDialog.isShowing()){
				loadingDialog.dismiss();
			}
			switch (msg.what) {
			case 1:
				ToastUtil.showShortToast(SignActivity.this, "设置签名成功");
				String sign=sign_content.getText().toString();
				Log.i("TAGtt", sign+"回到这里来吗");
				Intent intent=new Intent("updatesign");
				Bundle bundle=new Bundle();
				bundle.putString("sign", sign);
				intent.putExtras(bundle);
				sendBroadcast(intent);
				Log.i("TAGtt", "广播发出去了");
				finish();
				break;
			}
		}
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sign);
		loadingDialog=new LoadingDialog(this);
		loadingDialog.setTitle("请稍后...");
		initView();
	}



	/**
	 * 初始化控件
	 */
	private void initView() {
		go_back = (ImageView) findViewById(R.id.img_back);//返回
		btn_ok=(Button) findViewById(R.id.btn_ok);
		sign_content=(EditText) findViewById(R.id.sign_content);
		
		go_back.setOnClickListener(this);
		btn_ok.setOnClickListener(this);
	}
	
	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {//返回
		case R.id.img_back:
			this.finish();
			break;
		case R.id.btn_ok:
			if(QApp.getXmppConnection()==null){
				ToastUtil.showLongToast(SignActivity.this, "请检查您的网络");
				return;
			}
			if(TextUtils.isEmpty(sign_content.getText().toString().trim())){
				return;
			}
			loadingDialog.show();
			new Thread(new Runnable() {
				@Override
				public void run() {
					int code=PreferencesUtils.getSharePreInt("online_status");
					try{
						XmppUtil.changeSign(QApp.getXmppConnection(), code, sign_content.getText().toString());
						PreferencesUtils.putSharePre( "sign", sign_content.getText().toString());//保存个性签名
					}catch(Exception e){
						ToastUtil.showLongToast(SignActivity.this, "设置签名失败："+e.getMessage());
					}
					mHandler.sendEmptyMessage(1);
				}
			}).start();
			break;
		}
	}
	
	
	
	
}

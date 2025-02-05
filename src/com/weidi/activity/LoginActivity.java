package com.weidi.activity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.weidi.MainActivity;
import com.weidi.QApp;
import com.weidi.R;
import com.weidi.chat.IQOrder;
import com.weidi.provider.GetAccountByPhoneIQ;
import com.weidi.service.MsfService;
import com.weidi.util.Const;
import com.weidi.util.Logger;
import com.weidi.util.PreferencesUtils;
import com.weidi.util.ToastUtil;
import com.weidi.util.XmppLoadThread;
import com.weidi.util.XmppUtil;
import com.weidi.view.LoadingDialog;

public class LoginActivity extends Activity {

	public static String TAG = LoginActivity.class.getSimpleName();

	public static final String USER_NOT_EXIST = "00007";
	public static final String USER_NOT_EXIST_STRING = "用户不存在";
	private Context mContext;
	private RelativeLayout rl_user;
	private Button mLogin;
	private TextView mTextViewURL;
	private TextView btnLeft, btnRight;
	private EditText account, password;
	private LoadingDialog loadDialog;
	String username;// 用户名
	String pwd;// 密码

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		mContext = this;

		loadDialog = new LoadingDialog(this);
		loadDialog.setTitle("正在登录...");
		findView();
		initTvUrl();
		init();
		
	}

	private void findView() {
		rl_user = (RelativeLayout) findViewById(R.id.rl_user);
		mLogin = (Button) findViewById(R.id.login);
		btnLeft = (TextView) findViewById(R.id.tvTopLeft);

		btnRight = (TextView) findViewById(R.id.tvTopRight);
		mTextViewURL = (TextView) findViewById(R.id.tv_forget_password);
		btnRight.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(mContext, RegisterActi.class);
				startActivity(intent);
			}
		});
		account = (EditText) findViewById(R.id.account);
		password = (EditText) findViewById(R.id.password);
	}

	private void init() {
		Animation anim = AnimationUtils.loadAnimation(mContext,
				R.anim.login_anim);
		anim.setFillAfter(true);
		rl_user.startAnimation(anim);

		mLogin.setOnClickListener(loginOnClickListener);

	}

	private void initTvUrl() {
		mTextViewURL.setText(R.string.forget_password);
		mTextViewURL.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				startActivity(new Intent(mContext, ForgetPwdActi.class));
			}
		});
	}

	/**
	 * 登录
	 */
	private OnClickListener loginOnClickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			doLogin();
		}
	};

	protected void onStart() {
		super.onStart();
		String username = PreferencesUtils.getSharePreStr("username");// 用户名
		String pwd = PreferencesUtils.getSharePreStr("pwd");// 密码
		if (!TextUtils.isEmpty(username)) {
			account.setText(username);
		}
		if (!TextUtils.isEmpty(pwd)) {
			password.setText(pwd);
		}
	};

	void doLogin() {
		username = account.getText().toString();// 用户名
		pwd = password.getText().toString();// 密码
		if (TextUtils.isEmpty(username)) {
			ToastUtil.showShortToast(mContext, "请输入您的账号");
			return;
		}
		if (TextUtils.isEmpty(pwd)) {
			ToastUtil.showShortToast(mContext, "请输入您的密码");
			return;
		}
		if (username.length() < 11) {
			loginAccount(username, pwd);
		}
		if (username.length() == 11) {
			GetAccountByPhoneIQ iq = IQOrder.getInstance().getAccountByPhone(
					username);

			if (iq.getErrorCode() != null) {
				if (iq.getErrorCode().equals(LoginActivity.USER_NOT_EXIST)) {
					ToastUtil.showShortToast(mContext, "用户不存在");
					return;
				}

			}
			if (iq.getAccount() != null) {
				String account = iq.getAccount();
				Logger.e(TAG, username + "手机转微迪号：" + account);
				loginAccount(account, pwd);
			
			}
		}

	}

	public void loginAccount(final String account, final String password) {
		new XmppLoadThread(this) {

			@Override
			protected void result(Object object) {
				boolean isSuccess = (Boolean) object;
				if (isSuccess) {
					
				} else {
					ToastUtil.showShortToast(mContext,
							"登录失败，请检您的网络是否正常以及用户名和密码是否正确");
				}

			}

			@Override
			protected Object load() {
				boolean isSuccess = XmppUtil.getInstance().login(account,
						password);
				if (isSuccess) {
					Const.USER_ACCOUNT = account;
					Const.USER_PWD = password;
					PreferencesUtils.putSharePre("username", account);
					PreferencesUtils.putSharePre("pwd", password);
//					//启动核心Service
//					Intent service=new Intent(LoginActivity.this,MsfService.class);
//					startService(service);
					PreferencesUtils.putSharePre(Const.MSG_IS_VOICE, true);
					PreferencesUtils.putSharePre(Const.MSG_IS_VIBRATE, true);
					Intent intent = new Intent(LoginActivity.this,
							MainActivity.class);
					intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK
							| Intent.FLAG_ACTIVITY_NEW_TASK);
					startActivity(intent);
					finish();
				}
				return isSuccess;
			}
		};
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	
	}

}

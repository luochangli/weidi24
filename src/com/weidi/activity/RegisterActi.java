package com.weidi.activity;

import org.jivesoftware.smackx.packet.VCard;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.weidi.R;
import com.weidi.chat.ErrorHandle;
import com.weidi.chat.IQOrder;
import com.weidi.common.SmsContent;
import com.weidi.common.TimeButton;
import com.weidi.common.base.BaseActivity;
import com.weidi.provider.RegAuthcodeIQ;
import com.weidi.provider.RegIQ;
import com.weidi.service.MsfService;
import com.weidi.util.Const;
import com.weidi.util.Logger;
import com.weidi.util.PreferencesUtils;
import com.weidi.util.ToastUtil;
import com.weidi.util.Util;
import com.weidi.util.XmppUtil;

/**
 * @author luochangdong E-mail: 2270333671@qq.com
 * @date 创建时间：2015-7-19 上午10:32:42
 * @Description 1.0
 */
public class RegisterActi extends BaseActivity implements OnClickListener {

	private String TAG = "RegisterActi";
	public static final int AUTH_CODE = 12;
	LinearLayout reg1, reg2;
	EditText etNick, etPwd1, etPwd2, etPhone, etAuth;
	TextView tvNextStep, tvExplain, tvLabelStep1, tvLabelStep2;
	TimeButton tvGetAuth;
	ImageView ivRead;
	private ImageView topLeft;
	private TextView topTitle, topRight;
	private Boolean RegTag = true;
	private Boolean readTag = true;
	private String pwd1;
	private String nick;
	private String phone;
	private String auth;
	SmsContent content;
	public static String FirstNick = null; 

	@Override
	protected void initView(Bundle savedInstanceState) {
		setContentView(R.layout.acti_reg);
		initView();
		smsHandle();
	}

	private void smsHandle() {
		initTimeButton();
		content = new SmsContent(RegisterActi.this, mHandler, etAuth);
		// 注册短信变化监听
		this.getContentResolver().registerContentObserver(
				Uri.parse("content://sms/"), true, content);
	}

	private void initView() {
		topLeft = (ImageView) findViewById(R.id.topLeft);
		topTitle = (TextView) findViewById(R.id.topTitle);
		topRight = (TextView) findViewById(R.id.topRight);
		topTitle.setText("注册");
		topRight.setText("确定");

		tvLabelStep1 = (TextView) findViewById(R.id.tvLabelStep1);
		tvLabelStep2 = (TextView) findViewById(R.id.tvLabelStep2);

		reg1 = (LinearLayout) findViewById(R.id.reg1);
		reg2 = (LinearLayout) findViewById(R.id.reg2);
		etNick = (EditText) findViewById(R.id.etRegNick);
		etPwd1 = (EditText) findViewById(R.id.etRegPwd1);
		etPwd2 = (EditText) findViewById(R.id.etRegPwd2);
		etPhone = (EditText) findViewById(R.id.etPhone);
		etAuth = (EditText) findViewById(R.id.etAuth);
		ivRead = (ImageView) findViewById(R.id.ivRead);

		tvNextStep = (TextView) findViewById(R.id.tvNextStep);
		tvExplain = (TextView) findViewById(R.id.tvExplain);
		tvGetAuth = (TimeButton) findViewById(R.id.tvGetAuth);

	}

	@Override
	protected void setListener() {
		topLeft.setOnClickListener(this);
		topRight.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (RegTag) {
					regStep1();
				} else {
					regStep2();
				}
			}
		});
		tvNextStep.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				if (RegTag) {
					regStep1();
				} else {
					regStep2();
				}

			}

		});

		tvGetAuth.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				phone = etPhone.getText().toString();
				if (phone.isEmpty()) {

					ToastUtil.showShortToast(mContext, "手机号不能为空！");
					return;
				}
				if (!Util.getInstance().isMobileNumber(phone)
						|| !(phone.length() == 11)) {

					Logger.i(TAG, "" + Util.getInstance().isMobileNumber(phone));
					ToastUtil.showShortToast(mContext, "手机号格式不正确！");
					return;
				}

				RegAuthcodeIQ iq = IQOrder.getInstance().getRegAuthCode(phone);
				if (iq == null) {
					ToastUtil.showShortToast(mContext, "服务器异常，请稍后再试！");
					return;
				}
				if (iq.getErrorCode() != null) {
					ErrorHandle.errorCodeHint(iq.getErrorCode());
					return;
				}
				tvGetAuth.startTimer(v);
				tvGetAuth.setBackgroundResource(R.drawable.btn_reg_wait_auth);
				ToastUtil.showShortLuo("您好，短信已发出，请注意查收。");
			}
		});
		ivRead.setOnClickListener(this);
	}

	@Override
	protected void afterViews(Bundle savedInstanceState) {

	}

	@Override
	protected void handleMsg(Message msg) {

	}

	private void initTimeButton() {
		tvGetAuth.onCreate();
		tvGetAuth.setTextAfter("秒后重新获取").setTextBefore("获取验证码");
	}

	private void regStep1() {

		pwd1 = etPwd1.getText().toString();
		nick = etNick.getText().toString();
		if (nick.isEmpty() || pwd1.isEmpty()) {
			ToastUtil.showShortToast(mContext, "内容不能有空！");
			return;
		}
		if (pwd1.length() < 6) {
			ToastUtil.showShortToast(mContext, "密码设置不能小于6位数！");
			return;
		}
		tvLabelStep1.setTextColor(mContext.getResources().getColor(
				R.color.text_color_light));
		tvLabelStep2.setTextColor(mContext.getResources().getColor(
				R.color.green_bg));
		reg1.setVisibility(View.GONE);
		reg2.setVisibility(View.VISIBLE);
		RegTag = false;
	}

	private void regStep2() {
		phone = etPhone.getText().toString();
		auth = etAuth.getText().toString();
		if (phone.isEmpty() || auth.isEmpty()) {
			ToastUtil.showShortToast(mContext, "手机号或验证码不能为空！");
			return;
		}
		RegIQ iq = IQOrder.getInstance().reg(phone, pwd1, nick, auth);
		if (iq == null) {
			ToastUtil.showShortToast(mContext, "服务器异常，请稍后再试！");
			return;
		}
		if (iq.getErrorCode() != null) {
			ErrorHandle.errorCodeHint(iq.getErrorCode());
			return;
		}
		if (iq.getAccount() != null) {
			Logger.i(TAG, "注册成功：" + iq.getAccount());
			PreferencesUtils.putSharePre("username", phone);
			PreferencesUtils.putSharePre("pwd", pwd1);
		    FirstNick = nick;
			Intent intent = new Intent(this, MsfService.class);
			startService(intent);
			finish();
		}
	}

	@Override
	protected void onDestroy() {
		tvGetAuth.onDestroy();
		this.getContentResolver().unregisterContentObserver(content);
		super.onDestroy();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.topLeft:
			finish();
			break;

		case R.id.ivRead:
			checkRead();
			break;
		default:
			break;
		}

	}

	private void checkRead() {
		if (readTag) {
			ivRead.setImageResource(R.drawable.reg_unread);
			tvNextStep.setClickable(false);
			tvNextStep.setBackgroundResource(R.drawable.btn_reg_wait_auth);
			topRight.setClickable(false);
			readTag = false;
		} else {
			ivRead.setImageResource(R.drawable.reg_check);
			tvNextStep.setClickable(true);
			tvNextStep.setBackgroundResource(R.drawable.btn_reg_next_selector);
			topRight.setClickable(true);
			readTag = true;
		}
	}
}

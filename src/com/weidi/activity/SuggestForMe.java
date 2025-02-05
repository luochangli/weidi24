package com.weidi.activity;

import org.jivesoftware.smack.packet.IQ.Type;

import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.weidi.QApp;
import com.weidi.R;
import com.weidi.chat.IQOrder;
import com.weidi.common.base.BaseActivity;
import com.weidi.provider.SuggestIQ;
import com.weidi.util.ToastUtil;

/**
 * @author luochangdong E-mail: 2270333671@qq.com
 * @date 创建时间：2015-7-17 下午3:00:31
 * @Description 1.0
 */
public class SuggestForMe extends BaseActivity implements OnClickListener {

	private ImageView topLeft;
	private TextView topTitle, topRight;
	private Button btnSubmit;
	private EditText etMsg;
	private RelativeLayout rlOk;
	private RelativeLayout rlSuggestMsg;

	@Override
	protected void initView(Bundle savedInstanceState) {
		setContentView(R.layout.acti_suggest);
		initView();

	}

	private void initView() {
		topLeft = (ImageView) findViewById(R.id.topLeft);
		topTitle = (TextView) findViewById(R.id.topTitle);
		topRight = (TextView) findViewById(R.id.topRight);
		btnSubmit = (Button) findViewById(R.id.btnSubmit);
		etMsg = (EditText) findViewById(R.id.etSuggestMsg);
		rlOk = (RelativeLayout) findViewById(R.id.rlOK);
		rlSuggestMsg = (RelativeLayout) findViewById(R.id.rlSuggestMsg);
	}

	@Override
	protected void setListener() {
		btnSubmit.setOnClickListener(this);
		topLeft.setOnClickListener(this);
		topRight.setOnClickListener(this);
	}

	@Override
	protected void afterViews(Bundle savedInstanceState) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void handleMsg(Message msg) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.topLeft:
			finish();
			break;
		case R.id.topRight:
			sendMsg();
			break;
		case R.id.btnSubmit:
			sendMsg();
			break;
		default:
			break;
		}

	}

	private void sendMsg() {
		String msg = etMsg.getText().toString();
		if (msg == null || msg.isEmpty()) {
			ToastUtil.showShortToast(mContext, "建议内容不能为空！");
			return;
		}
		SuggestIQ iq = IQOrder.getInstance().sendSuggest(msg);
		if (iq == null) {
			ToastUtil.showShortToast(mContext, "服务器繁忙，请稍后再试！");
			return;
		}
		rlOk.setVisibility(View.VISIBLE);
		rlSuggestMsg.setVisibility(View.GONE);
		topRight.setVisibility(View.GONE);

	}

}

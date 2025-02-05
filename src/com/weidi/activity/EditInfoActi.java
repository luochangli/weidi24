package com.weidi.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.weidi.R;
import com.weidi.common.base.BaseActivity;
import com.weidi.util.Logger;
import com.weidi.util.ToastUtil;

/**
 * @author luochangdong E-mail: 2270333671@qq.com
 * @date 创建时间：2015-7-18 下午3:14:57
 * @Description 1.0
 */
public class EditInfoActi extends BaseActivity implements OnClickListener {
	private static String TAG = "EditInfoActi";
	public static final String EditValue = "edit_value";
	private ImageView topLeft;
	private TextView topTitle, topRight;
	private EditText etEditValue;
	private RadioGroup sexGroup;
	private RadioButton manRadio, womanRadio;
	private LinearLayout rlInfoEdit;
	private String sex;
	private int code;

	@Override
	protected void initView(Bundle savedInstanceState) {
		setContentView(R.layout.edit_info_acti);
		initView();

	}

	private void initView() {
		code = getIntent().getIntExtra(MyInfoActi.CODE, 0);
		Logger.i(TAG, "" + code);
		etEditValue = (EditText) findViewById(R.id.etEditValue);
		topLeft = (ImageView) findViewById(R.id.topLeft);
		topTitle = (TextView) findViewById(R.id.topTitle);
		topRight = (TextView) findViewById(R.id.topRight);
		sexGroup = (RadioGroup) findViewById(R.id.sexGroup);
		manRadio = (RadioButton) findViewById(R.id.manRadio);
		womanRadio = (RadioButton) findViewById(R.id.womanRadio);
		rlInfoEdit = (LinearLayout) findViewById(R.id.rlInfoEdit);
		switch (code) {
		case MyInfoActi.EDIT_NICK:
			topTitle.setText("修改昵称");
			break;
		case MyInfoActi.EDIT_SEX:
			topTitle.setText("修改性别");
			rlInfoEdit.setVisibility(View.GONE);
			sexGroup.setVisibility(View.VISIBLE);
			break;
		case MyInfoActi.EDIT_SIGN:
			topTitle.setText("修改签名");
			break;

		default:
			break;
		}

	}

	@Override
	protected void setListener() {
		topLeft.setOnClickListener(this);
		topRight.setOnClickListener(this);
		sexGroup.setOnCheckedChangeListener(new android.widget.RadioGroup.OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				if (checkedId == manRadio.getId()) {
					sex = "男";
				} else {
					sex = "女";
				}
			}
		});
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
			topRight();

			break;

		default:
			break;
		}

	}

	private void topRight() {
		if (code == MyInfoActi.EDIT_SEX) {
			Intent data = new Intent();
			data.putExtra(EditValue, sex);
			setResult(RESULT_OK, data);
			finish();

		} else if (etEditValue.getText().toString().isEmpty()) {

			ToastUtil.showMine(this,"修改内容不能为空！");
		} else {

			Intent data = new Intent();
			data.putExtra(EditValue, etEditValue.getText().toString());
			setResult(RESULT_OK, data);
			finish();
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}
}

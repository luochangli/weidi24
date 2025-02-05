package com.weidi.activity;

import java.io.File;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.weidi.QApp;
import com.weidi.R;
import com.weidi.chat.repository.AvatarRepo;
import com.weidi.common.base.BaseActivity;
import com.weidi.common.image.ImgConfig;
import com.weidi.common.scanner.ShowBarcode;
import com.weidi.common.view.EditSexDialog;
import com.weidi.common.view.EditSexDialog.OnCustomDialogListener;
import com.weidi.fragment.PersonalFrag;
import com.weidi.util.Const;
import com.weidi.util.ImageCache;
import com.weidi.util.Logger;
import com.weidi.util.XmppLoadThread;
import com.weidi.util.XmppUtil;
import com.weidi.view.CircleImageView;
import com.weidi.view.wheel.AeraWheel;
import com.weidi.view.wheel.WheelView;

/**
 * @author luochangdong E-mail: 2270333671@qq.com
 * @date 创建时间：2015-7-18 下午12:13:08
 * @Description 1.0
 */
public class MyInfoActi extends BaseActivity implements OnClickListener {

	private static String TAG = "MyInfoActi";
	public static final String NICK = "weidi.info.nick";
	public static final String SEX = "weidi.info.sex";
	public static final String SIGN = "weidi.info.sign";
	public static final String CODE = "result_code";

	public static final int EDIT_NICK = 10;
	public static final int EDIT_SEX = 20;
	public static final int EDIT_SIGN = 30;

	private CircleImageView civHeadInfo;
	private TextView tvInfoNick, tvInfoSex, tvInfoAdr, tvInfoSign;
	private RelativeLayout ivNickEdit, ivSexEdit, ivAdrEdit, ivSingEdit;
	private ImageView topLeft;
	private TextView topTitle, topRight;
	private WheelView mViewProvince, mViewCity, mViewDistrict;// 设置地址的
	private LinearLayout changeAdrLayout;
	private Button sureBtn, btnCancle;
	private AeraWheel area;
	@ViewInject(R.id.rlBarcode)
	RelativeLayout rlBarcode;
	@ViewInject(R.id.rlHead)
	RelativeLayout rlHead;

	@Override
	protected void initView(Bundle savedInstanceState) {
		setContentView(R.layout.acti_me_info);
		ViewUtils.inject(this);
		initView();
		initData();
		area = new AeraWheel(mViewProvince, mViewCity, mViewDistrict, mContext);
	
	}

	private void initData() {

//		ImgConfig.showHeadImg(Const.loginUser.getUsername(), civHeadInfo);
		AvatarRepo.getInstance().setAvatarRepo(Const.loginUser.getUsername(), civHeadInfo, mHandler);
		if (Const.loginUser.getNickname() != null) {
			tvInfoNick.setText(Const.loginUser.getNickname());
		}
		if (Const.loginUser.getAdr() != null) {
			tvInfoAdr.setText(Const.loginUser.getAdr());
		}
		if (Const.loginUser.getSex() != null) {
			tvInfoSex.setText(Const.loginUser.getSex());
		}
		if (Const.loginUser.getIntro() != null) {
			tvInfoSign.setText(Const.loginUser.getIntro());
		}
	}

	private void initView() {
		civHeadInfo = (CircleImageView) findViewById(R.id.ivInfoHead);
		tvInfoAdr = (TextView) findViewById(R.id.tvInfoAdr);
		tvInfoNick = (TextView) findViewById(R.id.tvInfoNick);
		tvInfoSex = (TextView) findViewById(R.id.tvInfoSex);
		tvInfoSign = (TextView) findViewById(R.id.tvInfoSign);

		ivAdrEdit = (RelativeLayout) findViewById(R.id.rlAdrEdit);
		ivNickEdit = (RelativeLayout) findViewById(R.id.rlInfoNick);
		ivSexEdit = (RelativeLayout) findViewById(R.id.rlSexEdit);
		ivSingEdit = (RelativeLayout) findViewById(R.id.rlSignEdit);

		topLeft = (ImageView) findViewById(R.id.topLeft);
		topTitle = (TextView) findViewById(R.id.topTitle);
		topRight = (TextView) findViewById(R.id.topRight);
		topRight.setVisibility(View.GONE);
		topTitle.setText("个人资料");

		mViewProvince = (WheelView) findViewById(R.id.id_province);
		mViewCity = (WheelView) findViewById(R.id.id_city);
		mViewDistrict = (WheelView) findViewById(R.id.id_district);
		changeAdrLayout = (LinearLayout) findViewById(R.id.changeAdrLayout);
		sureBtn = (Button) findViewById(R.id.sureBtn);
		btnCancle = (Button) findViewById(R.id.btnCancle);
	}

	@Override
	protected void setListener() {

		ivAdrEdit.setOnClickListener(this);
		ivNickEdit.setOnClickListener(this);
		ivSexEdit.setOnClickListener(this);
		ivSingEdit.setOnClickListener(this);
		topLeft.setOnClickListener(this);
		topRight.setOnClickListener(this);

		sureBtn.setOnClickListener(this);
		btnCancle.setOnClickListener(this);

		rlBarcode.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (ShowBarcode.Instance == null)
					startActivity(new Intent(mContext, ShowBarcode.class));
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

	@OnClick(R.id.rlHead)
	public void editHead(View v) {
		editInfoHead();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {

		case R.id.rlAdrEdit:
			changeAdrLayout.setVisibility(View.VISIBLE);
			break;
		case R.id.rlInfoNick:
			editInfoText(EDIT_NICK);
			break;
		case R.id.rlSexEdit:
			EditSexDialog dialog = new EditSexDialog(this, tvInfoSex.getText()
					.toString(), new OnCustomDialogListener() {

				@Override
				public void back(String name) {
					tvInfoSex.setText(name);
					Const.loginUser.setSex(name);
					updateVcard("sex", name);
				}
			});
			dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
			dialog.show();
			break;
		case R.id.rlSignEdit:
			editInfoText(EDIT_SIGN);
			break;
		case R.id.topLeft:
			finish();
			break;
		case R.id.topRight:

			break;
		case R.id.sureBtn:
			showSelectedResult();
			break;
		case R.id.btnCancle:
			changeAdrLayout.setVisibility(View.GONE);
			break;
		default:
			break;
		}

	}

	private void showSelectedResult() {
		String adr = area.getArea();
		tvInfoAdr.setText(adr);
		changeAdrLayout.setVisibility(View.GONE);
		Const.loginUser.setAdr(adr);
		updateVcard("adr", adr);

	}

	private void editInfoText(int code) {
		Intent intent = new Intent(this, EditInfoActi.class);
		intent.putExtra(CODE, code);
		startActivityForResult(intent, code);
	}

	private void editInfoHead() {
		Intent intent = new Intent();
		CropImageActivity.isAutoSend = false;
		intent.setClass(this, PicSrcPickerActivity.class);
		startActivityForResult(intent, PicSrcPickerActivity.CROP);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (data == null)
			return;
		String editValue = data.getExtras().getString(EditInfoActi.EditValue);
		Logger.i(TAG, "" + editValue + ":" + requestCode + ":" + resultCode);
		if (resultCode == RESULT_OK) {
			switch (requestCode) {

			case PicSrcPickerActivity.CROP:
				// String imgName = data.getStringExtra("imgName");
				Logger.i(TAG, data.getStringExtra("imgPath"));
				changeHead(data.getStringExtra("imgPath"));
				break;

			case EDIT_NICK:
				tvInfoNick.setText(editValue);
				Const.loginUser.setNickname(editValue);
				updateVcard("nickname", editValue);
				break;
			case EDIT_SEX:
				tvInfoSex.setText(data.getStringExtra(EditInfoActi.EditValue));
				Const.loginUser.setSex(editValue);
				updateVcard("sex", editValue);
				break;
			case EDIT_SIGN:
				Const.loginUser.setIntro(editValue);
				tvInfoSign.setText(data.getStringExtra(EditInfoActi.EditValue));
				updateVcard("intro", editValue);
				break;
			default:
				break;
			}

		}

	}

	private void refreshVcard() {
		QApp.mLocalBroadcastManager.sendBroadcast(new Intent(
				PersonalFrag.UPDATE_PERSONAL));
	}

	public void changeHead(final String imgPath) {
		new XmppLoadThread(this) {

			@Override
			protected void result(Object object) {
				if (object != null) {
					Bitmap bitmap = (Bitmap) object;
					civHeadInfo.setImageBitmap((Bitmap) object);
					ImageCache.getInstance().put(Const.USER_ACCOUNT, bitmap);
					refreshVcard();
				}
			}

			@Override
			protected Object load() {
				return XmppUtil.getInstance().changeImage(new File(imgPath));
			}
		};
	}

	private void updateVcard(String field, String data) {
		Const.loginUser.getvCard().setField(field, data);
		XmppUtil.getInstance().changeVcard(Const.loginUser.getvCard());
		refreshVcard();
	}

}

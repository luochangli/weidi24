package com.weidi.fragment;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.content.LocalBroadcastManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.weidi.MainActivity;
import com.weidi.R;
import com.weidi.activity.DevelopmentActi;
import com.weidi.activity.MyInfoActi;
import com.weidi.bean.User;
import com.weidi.common.base.BaseFragment;
import com.weidi.common.image.ImgConfig;
import com.weidi.common.scanner.ShowBarcode;
import com.weidi.util.Const;
import com.weidi.util.XmppUtil;
import com.weidi.view.CircleImageView;

/**
 * @author luochangdong E-mail: 2270333671@qq.com
 * @date 创建时间：2015-7-18 上午10:15:04
 * @Description 1.0
 */
public class PersonalFrag extends BaseFragment implements OnClickListener {

	@ViewInject(R.id.rlMeGallerys)
	RelativeLayout rlMeGallerys;
	@ViewInject(R.id.rlMeSetting)
	RelativeLayout rlMe2Code;
	@ViewInject(R.id.rlMeCollotions)
	RelativeLayout rlMeCollotions;
	@ViewInject(R.id.rlMeWallet)
	RelativeLayout rlMeWallet;
	@ViewInject(R.id.llTopRight)
	LinearLayout llTopRight;
	@ViewInject(R.id.llBarcode)
	LinearLayout llBarcode;
	@ViewInject(R.id.llEntry)
	LinearLayout llEntry;
	@ViewInject(R.id.ivMeSex)
	ImageView ivMeSex;
	MainActivity activity;

	CircleImageView civHead;
	TextView tvNickName, tvWeidi;
	public static final String UPDATE_PERSONAL = "update_personal";

	@Override
	protected void handleMsg(Message msg) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void initView(Bundle savedInstanceState) {
		setRootView(R.layout.frag_me);
		Const.loginUser = new User(XmppUtil.getUserInfo(null));// 加载用户数据
		ViewUtils.inject(this, mRootView);
		initView();
		initData();
		initBroadcast();
	}

	private void initBroadcast() {
		mLocalBroadcastManager = LocalBroadcastManager.getInstance(mContext);
		mReceiver = new BroadcastReceiver() {

			@Override
			public void onReceive(Context context, Intent intent) {
				initData();

			}
		};
		mLocalBroadcastManager.registerReceiver(mReceiver, new IntentFilter(
				UPDATE_PERSONAL));
	}

	private void initData() {
		ImgConfig.showHeadImg(Const.USER_NAME, civHead);
		tvWeidi.setText(Const.USER_NAME);
		if (Const.loginUser.getNickname() != null) {
			tvNickName.setText(Const.loginUser.getNickname());
		}
		if (Const.loginUser.getSex() != null) {
			if (Const.loginUser.getSex().equals(Const.Female)) {
				ivMeSex.setImageResource(R.drawable.female);

			} else {
				ivMeSex.setImageResource(R.drawable.male);
			}
		}
	}

	private void initView() {
		civHead = (CircleImageView) mRootView.findViewById(R.id.civHeadImg);
		tvNickName = (TextView) mRootView.findViewById(R.id.tvNickName);
		tvWeidi = (TextView) mRootView.findViewById(R.id.tvWeidi);

	}

	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
		this.activity = (MainActivity) activity;
	}

	@Override
	protected void setListener() {
		civHead.setOnClickListener(this);

		llTopRight.setOnClickListener(this);
		llBarcode.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (ShowBarcode.Instance == null)
  				startActivity(new Intent(mContext, ShowBarcode.class));
			}
		});

		llEntry.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				entryMine();
			}
		});
		
	
	}

	@OnClick(R.id.rlMeSetting)
	public void saoyisao(View v) {
		startActivity(new Intent(mContext, DevelopmentActi.class));
	}

	@OnClick(R.id.rlMeCollotions)
	public void collotions(View v){
		startActivity(new Intent(mContext, DevelopmentActi.class));
	}
	@OnClick(R.id.rlMeGallerys)
	public void gallerys(View v){
		startActivity(new Intent(mContext, DevelopmentActi.class));
		
	}
	@OnClick(R.id.rlMeWallet)
	public void wallet(View v){
		startActivity(new Intent(mContext, DevelopmentActi.class));
	}
	
	@Override
	protected void afterViews(Bundle savedInstanceState) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.civHeadImg:
			entryMine();
			break;
		case R.id.llTopRight:
			activity.moreDialog();
			break;
		default:
			break;
		}

	}

	private void entryMine() {
		Intent intent = new Intent(mContext, MyInfoActi.class);
		mContext.startActivity(intent);
	}

	@Override
	public void onDestroy() {
		mLocalBroadcastManager.unregisterReceiver(mReceiver);
		super.onDestroy();
	}

}

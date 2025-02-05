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
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
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
import com.weidi.activity.RegisterActi;
import com.weidi.bean.User;
import com.weidi.chat.IQOrder;
import com.weidi.chat.repository.AvatarRepo;
import com.weidi.chat.repository.VipManager;
import com.weidi.common.DateUtil;
import com.weidi.common.base.BaseFragment;
import com.weidi.common.image.ImgConfig;
import com.weidi.common.scanner.ShowBarcode;
import com.weidi.provider.sign.SignManager;
import com.weidi.util.Const;
import com.weidi.util.PreferencesUtils;
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
	@ViewInject(R.id.ivMeSex)
	ImageView ivMeSex;
	@ViewInject(R.id.tvIntegral)
	TextView tvIntegral;
	@ViewInject(R.id.tvRegistrat)
	TextView tvRegistrat;

	CircleImageView civHead;
	TextView tvNickName, tvWeidi;

	@ViewInject(R.id.tvIsSign)
	TextView tvIsSign;
	@ViewInject(R.id.rlSign)
	RelativeLayout rlSign;
	@ViewInject(R.id.vip)
	TextView vip;
	@ViewInject(R.id.tvSignAinm)
	TextView tvSignAinm;

	public static final String UPDATE_PERSONAL = "update_personal";
	public static final String UPDATE_INTEGRAL = "update_integral";
	Animation animFadein;
	MainActivity activity;

	@OnClick(R.id.rlSign)
	private void rlSign(View v) {
		IQOrder.getInstance().sendSignIn();
		String date = DateUtil.getCurDateStr("yyyy-MM-dd");
		PreferencesUtils.putSharePre(Const.USER_ACCOUNT + "signIn", date);
		tvRegistrat.setBackgroundDrawable(getResources().getDrawable(
				R.drawable.bg_registration_signed));

		 v.setClickable(false);
		startSignAnim();
	}

	private void startSignAnim() {
		tvSignAinm.setVisibility(View.VISIBLE);
		tvSignAinm.startAnimation(animFadein);

	}

	@Override
	protected void handleMsg(Message msg) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void initView(Bundle savedInstanceState) {
		setRootView(R.layout.frag_me);
		ViewUtils.inject(this, mRootView);
		initView();
		Const.loginUser = new User(XmppUtil.getUserInfo(null));// 加载用户数据
		if (RegisterActi.FirstNick != null) {

			Const.loginUser.getvCard().setField("nickname",
					RegisterActi.FirstNick);
			Const.loginUser.setNickname(RegisterActi.FirstNick);
			XmppUtil.getInstance().changeVcard(Const.loginUser.getvCard());
		}
		initData();
		initBroadcast();
		initAnim();

	}

	private void initAnim() {
		// 加载动画
		animFadein = AnimationUtils.loadAnimation(mContext, R.anim.signed_out);

	}

	private void initBroadcast() {
		mLocalBroadcastManager = LocalBroadcastManager.getInstance(mContext);
		mReceiver = new BroadcastReceiver() {

			@Override
			public void onReceive(Context context, Intent intent) {
				if (intent.getAction().equals(UPDATE_INTEGRAL)) {
					String integral = intent
							.getStringExtra(SignManager.INTEGRAL);
					tvIntegral.setText(integral);
					tvIsSign.setText("已签到");
					tvIsSign.setTextColor(getResources().getColor(
							R.color.text_color_light));

				} else {
					initData();
				}

			}
		};
		mLocalBroadcastManager.registerReceiver(mReceiver, new IntentFilter(
				UPDATE_PERSONAL));
		mLocalBroadcastManager.registerReceiver(mReceiver, new IntentFilter(
				UPDATE_INTEGRAL));
	}

	private void initData() {
		// ImgConfig.showHeadImg(Const.USER_NAME, civHead);
		AvatarRepo.getInstance().setAvatarRepo(Const.USER_ACCOUNT, civHead,
				mHandler);
		tvWeidi.setText(Const.USER_ACCOUNT);
		initSign();

	}

	private void initSign() {
		VipManager.getInstance().isVipWeidi(Const.USER_ACCOUNT, vip);
		if (Const.loginUser.getNickname() != null) {
			tvNickName.setText(Const.loginUser.getNickname());
		}
		if (Const.loginUser.getSex() != null) {
			if (Const.loginUser.getSex().equals(Const.Male)) {
				ivMeSex.setImageResource(R.drawable.male_ic);

			} else {
				ivMeSex.setImageResource(R.drawable.female_ic);
			}
		}

		if (SignManager.getInstance().isSinged()) {
			tvRegistrat.setBackgroundDrawable(getResources().getDrawable(
					R.drawable.bg_registration_signed));
			 rlSign.setClickable(false);
			tvIsSign.setText("已签到");
			tvIsSign.setTextColor(getResources().getColor(
					R.color.text_color_light));

		} else {
			tvRegistrat.setBackgroundDrawable(getResources().getDrawable(
					R.drawable.bg_registration));
		}
		SignManager.getInstance().getIntegral(tvIntegral);
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
		animFadein.setAnimationListener(new AnimationListener() {

			@Override
			public void onAnimationStart(Animation animation) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onAnimationRepeat(Animation animation) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onAnimationEnd(Animation animation) {
			  tvSignAinm.setVisibility(View.GONE);

			}
		});
	}

	@OnClick(R.id.rlMeSetting)
	public void saoyisao(View v) {
		startActivity(new Intent(mContext, DevelopmentActi.class));
	}

	@OnClick(R.id.rlMeCollotions)
	public void collotions(View v) {
		startActivity(new Intent(mContext, DevelopmentActi.class));
	}

	@OnClick(R.id.rlMeGallerys)
	public void gallerys(View v) {
		startActivity(new Intent(mContext, DevelopmentActi.class));

	}

	@OnClick(R.id.rlMeWallet)
	public void wallet(View v) {
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
			activity.popMenu.showAsDropDown(v);
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

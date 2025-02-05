package com.weidi.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.weidi.R;
import com.weidi.util.SystemMethod;

public class TitleBarView extends RelativeLayout {

	private Context mContext;
	private Button btnLeft;
	private Button btnRight;
	private Button btn_titleLeft;
	private Button btn_titleRight;
	private TextView tv_center,tvRight;
	private LinearLayout common_constact;
	public TitleBarView(Context context) {
		super(context);
		mContext = context;
		initView();
	}

	public TitleBarView(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;
		initView();
	}
	private void initView() {
		LayoutInflater.from(mContext).inflate(R.layout.common_title_bar, this);
		btnLeft = (Button) findViewById(R.id.title_btn_left);
		btnRight = (Button) findViewById(R.id.title_btn_right);
		btn_titleLeft = (Button) findViewById(R.id.constact_group);
		btn_titleRight = (Button) findViewById(R.id.constact_all);
		tv_center = (TextView) findViewById(R.id.title_txt);
		tvRight = (TextView) findViewById(R.id.tv_right);
		common_constact = (LinearLayout) findViewById(R.id.common_constact);
	}

	public void setCommonTitle(int LeftVisibility, int centerVisibility,
			int center1Visibilter,int tv_right, int rightVisibility) {
		btnLeft.setVisibility(LeftVisibility);
		btnRight.setVisibility(rightVisibility);
		tv_center.setVisibility(centerVisibility);
		tvRight.setVisibility(tv_right);
		common_constact.setVisibility(center1Visibilter);

	}

	public void setBtnLeftIcon(int icon) {
		Drawable img = mContext.getResources().getDrawable(icon);
		int height = SystemMethod.dip2px(mContext, 20);
		int width = img.getIntrinsicWidth() * height / img.getIntrinsicHeight();
		img.setBounds(0, 0, width, height);
		btnLeft.setCompoundDrawables(img, null, null, null);
	}

	public void setBtnLeft(int icon, int txtRes) {
		Drawable img = mContext.getResources().getDrawable(icon);
		int height = SystemMethod.dip2px(mContext, 20);
		int width = img.getIntrinsicWidth() * height / img.getIntrinsicHeight();
		img.setBounds(0, 0, width, height);
		btnLeft.setText(txtRes);
		btnLeft.setCompoundDrawables(img, null, null, null);
	}

	public void setBtnLeft(int txtRes) {
		btnLeft.setText(txtRes);
	}
	
	public void setBtnRightTV(int txtRes) {
		btnRight.setText(txtRes);
	}

	public void setBtnRight(int icon) {
		btnRight.setBackgroundResource(icon);
//		Drawable img = mContext.getResources().getDrawable(icon);
//		int height = SystemMethod.dip2px(mContext, 30);
//		int width = img.getIntrinsicWidth() * height / img.getIntrinsicHeight();
//		img.setBounds(0, 0, width, height);
//		btnRight.setCompoundDrawables(img, null, null, null);
	}
	
	public void setTVRightIcon(int icon_id) {
		tvRight.setBackgroundResource(icon_id);;
	}
	
	public void setTVRight(int txtRes) {
		tvRight.setText(txtRes);
	}
	

	public void setTitleLeft(int resId) {
		btn_titleLeft.setText(resId);
	}

	public void setTitleLeft(String res) {
		btnLeft.setText(res);
	}

	public void setTitleRight(int resId) {
		btn_titleRight.setText(resId);
	}

	public void setTitleRight(String res) {
		btnRight.setText(res);
	}

	@SuppressLint("NewApi")
	public void setPopWindow(PopupWindow mPopWindow, TitleBarView titleBaarView) {
		mPopWindow.setBackgroundDrawable(new ColorDrawable(Color
				.parseColor("#E9E9E9")));
		mPopWindow.showAsDropDown(titleBaarView, 0, -15);
		mPopWindow.setAnimationStyle(R.style.popwin_anim_style);
		mPopWindow.setFocusable(true);
		mPopWindow.setOutsideTouchable(true);
		mPopWindow.update();

		setBtnRight(R.drawable.skin_conversation_title_right_btn_selected);
	}

	public void setTitleText(int txtRes) {
		tv_center.setText(txtRes);
	}

	public void setBtnLeftOnclickListener(OnClickListener listener) {
		btnLeft.setOnClickListener(listener);
	}

	public void setBtnRightOnclickListener(OnClickListener listener) {
		btnRight.setOnClickListener(listener);
	}
	
	public void setTvRightOnclickListener(OnClickListener listener) {
		tvRight.setOnClickListener(listener);
	}
	
	public TextView getTvRight(){
		return tvRight;
	}
	public Button getTitleLeft() {
		return btn_titleLeft;
	}

	public Button getTitleRight() {
		return btn_titleRight;
	}

	public void destoryView() {
		btnLeft.setText(null);
		btnRight.setText(null);
		tv_center.setText(null);
	}

}

package com.weidi.common.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;

import com.weidi.R;

/**
 * @author luochangdong E-mail: 2270333671@qq.com
 * @date 创建时间：2015-7-22 上午11:17:51
 * @Description 1.0
 */
public class LuoTitleBar extends RelativeLayout {

	private Context mContext;

	public LuoTitleBar(Context context) {
		super(context);
		mContext = context;
		initView();
	}

	public LuoTitleBar(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	private void initView() {
		LayoutInflater.from(mContext).inflate(R.layout.top_luo_tv, this);

	}

}

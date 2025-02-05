package com.weidi.common.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

import com.weidi.R;

/**
 * @author luochangdong E-mail: 2270333671@qq.com
 * @date 创建时间：2015-7-27 下午5:09:06
 * @Description 1.0
 */
public class EditMoreDialog extends Dialog {
	
	Context context;

	// 定义回调事件，用于dialog的点击事件
	public interface OnCustomDialogListener {
		public void back(String name);
	}

	private OnCustomDialogListener customDialogListener;

	public EditMoreDialog(Context context) {
		super(context);
		this.context=context;
		
		this.customDialogListener = customDialogListener;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_alert);
//		// 设置标题
//		setTitle(name);
	
	}

}

package com.weidi.common.view;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.weidi.QApp;
import com.weidi.R;
import com.weidi.activity.LoginActivity;
import com.weidi.service.MsfService;

/**
 * @author luochangdong E-mail: 2270333671@qq.com
 * @date 创建时间：2015-7-27 下午5:09:06
 * @Description 1.0
 */
public class EditExitDialog extends Dialog {
	
	Context context;

	// 定义回调事件，用于dialog的点击事件
	public interface OnCustomDialogListener {
		public void back(String name);
	}

	private OnCustomDialogListener customDialogListener;
	TextView tv_exit,tv_allexit;

	public EditExitDialog(Context context) {
		super(context);
		this.context=context;
		
		this.customDialogListener = customDialogListener;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_exit);
//		// 设置标题
//		setTitle(name);
		tv_exit = (TextView) findViewById(R.id.tv_exit);
		tv_allexit = (TextView) findViewById(R.id.tv_allexit);
		
		tv_exit.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				context.startActivity(new Intent(context,
						LoginActivity.class));

				MsfService.getInstance().stopSelf();
				java.lang.System.exit(0);
				
			}
		});
		
		tv_allexit.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				QApp.getInstance().exit();

				MsfService.getInstance().stopSelf();
				java.lang.System.exit(0);
				
			}
		});
		

	}

}

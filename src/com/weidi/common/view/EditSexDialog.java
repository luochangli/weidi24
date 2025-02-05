package com.weidi.common.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.weidi.R;

/**
 * @author luochangdong E-mail: 2270333671@qq.com
 * @date 创建时间：2015-7-27 下午5:09:06
 * @Description 1.0
 */
public class EditSexDialog extends Dialog {

	// 定义回调事件，用于dialog的点击事件
	public interface OnCustomDialogListener {
		public void back(String name);
	}

	private String name;
	private OnCustomDialogListener customDialogListener;
	private RadioGroup sexGroup;
	private RadioButton manRadio, womanRadio;
	private String sex;

	public EditSexDialog(Context context, String sex,
			OnCustomDialogListener customDialogListener) {
		super(context);
		this.sex = sex;
		this.customDialogListener = customDialogListener;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_edit_sex);
//		// 设置标题
//		setTitle(name);
		sexGroup = (RadioGroup) findViewById(R.id.sexGroup);
		manRadio = (RadioButton) findViewById(R.id.manRadio);
		womanRadio = (RadioButton) findViewById(R.id.womanRadio);
		if(sex.equals("男") ){
			manRadio.setChecked(true);
			womanRadio.setChecked(false);
		}else{
			manRadio.setChecked(false);
			womanRadio.setChecked(true);
		}
		sexGroup.setOnCheckedChangeListener(new android.widget.RadioGroup.OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				if (checkedId == manRadio.getId()) {
					sex = "男";
					customDialogListener.back(sex);
					EditSexDialog.this.dismiss();
				} else {
					sex = "女";
					customDialogListener.back(sex);
					EditSexDialog.this.dismiss();
				}
			}
		});

	}

}

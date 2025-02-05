package com.weidi.fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.weidi.R;
import com.weidi.common.base.BaseFragment;

public class FindFragment extends BaseFragment{

	private View mBaseView;
	private RelativeLayout re_fujin;
	private Context mContent;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mContent = getActivity();
		mBaseView = inflater.inflate(R.layout.fragment_find, null);
		re_fujin = (RelativeLayout) mBaseView.findViewById(R.id.re_fujin);
		setListener();
		return mBaseView;
	}

	@Override
	protected void handleMsg(Message msg) {
		// TODO Auto-generated method stub

	}
	/**
	 * 初始化View控件
	 */
	@Override
	protected void initView(Bundle savedInstanceState) {
		
	}

	@Override
	protected void setListener() {
		re_fujin.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Log.i("onLocationChanged", "come in");
				String strContent = getResources().getString(R.string.re_fujin_content);
				AlertDialog.Builder builder = new AlertDialog.Builder(mContent);
				builder.setTitle("提示")
				.setCancelable(false)
				.setMessage(strContent)
				.setPositiveButton(getResources().getString(R.string.fujin_ok), 
						new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface arg0, int arg1) {
					
					}
				})
				.setNegativeButton(getResources().getString(R.string.fujin_cancel), 
						new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int arg1) {
						dialog.dismiss();
					}
				});
				AlertDialog alertDialog = builder.create();
				alertDialog.show();
			}
		});
	}

	@Override
	protected void afterViews(Bundle savedInstanceState) {
		// TODO Auto-generated method stub

	}
}

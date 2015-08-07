package com.weidi.view;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

import com.weidi.R;
import com.weidi.activity.FujinActivity;
import com.weidi.activity.SearchActivity;
import com.weidi.activity.SettingActivity;
import com.weidi.activity.TradeActivity;
import com.weidi.chat.groupchat.CreatChatRoomActivity;



/**
 *@author  luochangdong  E-mail: 2270333671@qq.com
 *@date 创建时间：2015-6-19 下午3:44:03
 *@Description 1.0 topbar功能菜单
 */
public class AddPopWindow extends PopupWindow {
    private View conentView;
    private RelativeLayout   re_addfriends,re_chatroom,re_nearpeople,re_trade,re_setting;
    private Context mContext;
	@SuppressLint("InflateParams")
	public AddPopWindow( Activity context) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        conentView = inflater.inflate(R.layout.popupwindow_add, null);
        mContext = context;
        // 设置SelectPicPopupWindow的View
        this.setContentView(conentView);
        // 设置SelectPicPopupWindow弹出窗体的宽
        this.setWidth(LayoutParams.WRAP_CONTENT);
        // 设置SelectPicPopupWindow弹出窗体的高
        this.setHeight(LayoutParams.WRAP_CONTENT);
        // 设置SelectPicPopupWindow弹出窗体可点击
        this.setFocusable(true);
        this.setOutsideTouchable(true);
        // 刷新状态
        this.update();
        // 实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0000000000);
        // 点back键和其他地方使其消失,设置了这个才能触发OnDismisslistener ，设置其他控件变化等操作
        this.setBackgroundDrawable(dw);
        
        // 设置SelectPicPopupWindow弹出窗体动画效果
        this.setAnimationStyle(R.style.AnimationPreview);
        
        
        re_addfriends =(RelativeLayout) conentView.findViewById(R.id.re_addfriends);
        re_chatroom =(RelativeLayout) conentView.findViewById(R.id.re_crechatroom);
        re_nearpeople =(RelativeLayout) conentView.findViewById(R.id.re_nearpoeple);
        re_trade =(RelativeLayout) conentView.findViewById(R.id.re_saoyisao);
        re_setting =(RelativeLayout) conentView.findViewById(R.id.re_setting);
        
        setListener();
        
 
    }


	private void setListener() {
	
		re_addfriends.setOnClickListener(new OnClickListener(){

            @Override
            public void onClick(View v) {
                mContext.startActivity(new Intent(mContext,SearchActivity.class));  
                AddPopWindow.this.dismiss();
          
            }
            
        } );
        re_chatroom.setOnClickListener(new OnClickListener(){

            @Override
            public void onClick(View v) {
                /*mContext.startActivity(new Intent(mContext,CreatChatRoomActivity.class));  
                AddPopWindow.this.dismiss();*/
            	mContext.startActivity(new Intent(mContext,CreatChatRoomActivity.class));  
                AddPopWindow.this.dismiss();

                
            }
            
        } );
        re_nearpeople.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				   mContext.startActivity(new Intent(mContext,FujinActivity.class));  
	                AddPopWindow.this.dismiss();
				
			}
		});
        
        re_trade.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				  mContext.startActivity(new Intent(mContext,TradeActivity.class));  
	                AddPopWindow.this.dismiss();
				
			}
		});
        
        re_setting.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				mContext.startActivity(new Intent(mContext,SettingActivity.class));
				AddPopWindow.this.dismiss();
				
			}
		});
        
        
	}


    /**
     * 显示popupWindow
     * 
     * @param parent
     */
    public void showPopupWindow(View parent) {
        if (!this.isShowing()) {
            // 以下拉方式显示popupwindow
            this.showAsDropDown(parent, 0, 0);
        } else {
            this.dismiss();
        }
    }
}


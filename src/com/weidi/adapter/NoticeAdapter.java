package com.weidi.adapter;

import java.util.List;
import java.util.Map;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.weidi.R;

public class NoticeAdapter extends BaseAdapter {
	private Context context;
	private List<Map<String, Object>> list_notice;
	
	public NoticeAdapter(Context context,List<Map<String, Object>> list_notice){
		this.context=context;
		this.list_notice=list_notice;
		Log.i("aaaaaa", ""+list_notice.size());
	}
	

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list_notice.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return list_notice.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public View getView(int arg0, View view, ViewGroup arg2) {
		final ViewHodler hodler;
		if(view==null){
			hodler = new ViewHodler();
			view = LayoutInflater.from(context).inflate(
					R.layout.notice_style, null);
			
			hodler.re_notice = (RelativeLayout) view
					.findViewById(R.id.re_notice);
			hodler.title=(TextView)view.findViewById(R.id.title);
			hodler.time=(TextView)view.findViewById(R.id.time);
			hodler.content=(TextView)view.findViewById(R.id.content);
			hodler.img=(ImageView)view.findViewById(R.id.img);
			hodler.re_content=(RelativeLayout)view.findViewById(R.id.re_content);
			
			view.setTag(hodler);
		}else{
			hodler = (ViewHodler) view.getTag();
			
		}
		hodler.title.setText((String)list_notice.get(arg0).get("title"));
		hodler.time.setText((String)list_notice.get(arg0).get("createdatetime"));
		hodler.content.setText((String)list_notice.get(arg0).get("content"));
		
		String isread=(String)list_notice.get(arg0).get("isread");
		
		if(isread.equals("1")){
			hodler.img.setBackgroundResource(R.drawable.huise);
			hodler.title.setBackgroundColor(Color.parseColor("#b3b3b3"));
			hodler.re_content.setBackgroundResource(R.drawable.hui);
			Log.i("111111111", "2222222222");
		}
		return view;
	}
	
	
	class ViewHodler {
		RelativeLayout re_img,re_notice,re_content;
		ImageView img;
		TextView tv_shu,title,content,time;
	}

}

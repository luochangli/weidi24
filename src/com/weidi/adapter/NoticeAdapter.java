package com.weidi.adapter;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
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
		//hodler.time.setText((String)list_notice.get(arg0).get("createdatetime"));
		hodler.content.setText((String)list_notice.get(arg0).get("content"));
		String isread=(String)list_notice.get(arg0).get("isread");
//		setdate((String)list_notice.get(arg0).get("createdatetime"),);
		String date = (String) list_notice.get(arg0).get("createdatetime");
		hodler.time.setText(date);
		if(isread.equals("1")){
			hodler.title.setTextColor(Color.parseColor("#000000"));
		}
		return view;
	}
	
	private void setdate(String time,TextView tv_time){
		try {
			int start=time.indexOf("T");
			int end=time.lastIndexOf(":");
			int startmonth=time.indexOf("-");
			
			String mm=time.substring(0,start);
			Date now = new Date();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");  
			 String dateNowStr = sdf.format(now);  
			
			if(mm.equals(dateNowStr)){
				String today=time.substring(start+1,end);
				tv_time.setText(today);
			}else{
				String untoday=time.substring(startmonth+1,start);
				if(untoday.contains("-")){
					untoday=untoday.replace("-", "/");
				}
				tv_time.setText(untoday);
			}
			
			//time=time.substring(start+1,end);
			/*SimpleDateFormat sdf = new SimpleDateFormat( "HH:mm" );
			Date date = sdf.parse(time);
			String str = sdf.format(date);*/
		   //tv_time.setText(today);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	class ViewHodler {
		RelativeLayout re_img,re_notice,re_content;
		ImageView img;
		TextView tv_shu,title,content,time;
	}
	
	public void addItem(Map<String, Object> i){
		list_notice.add(i);
	}

}

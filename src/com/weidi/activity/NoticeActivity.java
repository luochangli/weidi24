package com.weidi.activity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.weidi.R;
import com.weidi.adapter.NoticeAdapter;
import com.weidi.db.NewsNotice;
import com.weidi.util.Const;
import com.weidi.view.TitleBarView;

public class NoticeActivity extends Activity {
	ListView list_notice;
	List<Map<String, Object>> notice_data;
	String tmpeStr=null;
	Handler mHandler;
	Map<String, Object> map;
	NewsNotice news;
	private ImageView topLeft;
	private TextView topTitle, topRight;
	String news_id;
	NoticeAdapter adapter;
	@SuppressLint("HandlerLeak")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_notice);
		notice_data=new ArrayList<Map<String, Object>>();
		initData();
		initTitleView();
		initView();
		
		
	}
	private void initData() {	
		 news=NewsNotice.getInstance();
		 List<Map<String,Object>> news_list;
		 if(Const.newscount!=0&&Const.newscount<10){
			  news_list=news.query(Const.newscount);
			 notice_data=news_list;
		 }else{
			 int count=news.querycount();
			 if(count>10)
			 {
				 news_list=news.query(10);
			 }else{
				 news_list=news.query(count);
			 }
			 notice_data=news_list;
		 }
		 Const.newscount=0;
		 
	}
	
	private void initTitleView() {
		topLeft = (ImageView) findViewById(R.id.topLeft);
		topTitle = (TextView) findViewById(R.id.topTitle);
		topRight = (TextView) findViewById(R.id.topRight);
		topTitle.setText("公告");
	    topRight.setVisibility(View.GONE);
	    topLeft.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		
	}
	private void initView() {
		 list_notice=(ListView)findViewById(R.id.list_notice);
			// list_notice.setOnTouchListener(new MyTouchListener());
			// list_notice.setOnScrollListener(new MyScrollListener());  
			/*LayoutInflater infalter = LayoutInflater.from(NoticeActivity.this);
			View foot=infalter.inflate(R.layout.notice_foot, null);
			list_notice.addFooterView(foot);*/
		  
			adapter=new NoticeAdapter(NoticeActivity.this,notice_data);
			list_notice.setAdapter(adapter);
			list_notice.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> arg0, View view, int arg2,
						long arg3) {
					// TODO Auto-generated method stub
					Intent intent=new Intent(NoticeActivity.this,WebViewActivity.class);
					String pathurl=(String)notice_data.get(arg2).get("link");
					Bundle bundle=new Bundle();
					bundle.putString("httpurl", pathurl);
					intent.putExtras(bundle);
					startActivity(intent);
					TextView text=(TextView)view.findViewById(R.id.title);
					news_id=(String)notice_data.get(arg2).get("news_id");
					isRead(news_id);
					text.setTextColor(Color.parseColor("#000000"));
				}
			}); 
	}
	private void isRead(String news_id){
		ContentValues values = new ContentValues();
		values.put("isread", 1);
		news.updateRead(news_id, values);
	}

}

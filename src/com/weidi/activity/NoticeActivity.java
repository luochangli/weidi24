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
	TitleBarView mTitleBarView;
	int news_id;
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
		List<Map<String,Object>> news_list=news.query(Const.newscount);
		if(news_list.size()!=0&&news_list.size()>5){
			notice_data=news_list;
		}else{
			news_list=news.query(5);
			notice_data=news_list;
		}
		Const.newscount=0;
	}
	
	private void initTitleView() {
		mTitleBarView = (TitleBarView) findViewById(R.id.title_bar);
		mTitleBarView.setCommonTitle(View.VISIBLE, View.VISIBLE, View.GONE,
				View.GONE, View.GONE);
		mTitleBarView.setTitleText(R.string.notice);
		mTitleBarView.setBtnLeftIcon(R.drawable.btn_back);
		mTitleBarView.setBtnLeftOnclickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				finish();

			}
		});

	}
	private void initView() {
		
	    list_notice=(ListView)findViewById(R.id.list_notice);
		NoticeAdapter adapter=new NoticeAdapter(NoticeActivity.this,notice_data);
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
				Log.i("URL", pathurl);	 
				TextView text=(TextView)view.findViewById(R.id.title);
				ImageView img=(ImageView)view.findViewById(R.id.img);
				news_id=(Integer) notice_data.get(arg2).get("news_id");
				isRead(news_id);
				img.setBackgroundResource(R.drawable.huise);
				text.setBackgroundColor(Color.parseColor("#b3b3b3"));
				RelativeLayout re_content=(RelativeLayout)view.findViewById(R.id.re_content);
				re_content.setBackgroundResource(R.drawable.hui);
			}
		}); 
	}
	private void isRead(int news_id){
		ContentValues values = new ContentValues();
		values.put("isread", 1);
		news.updateRead(news_id, values);
	}

}

package com.weidi.activity;




import android.app.ListActivity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

import com.google.gson.Gson;
import com.weidi.QApp;
import com.weidi.R;
import com.weidi.image.MyListAdapter;
import com.weidi.image.MyMessage;
import com.weidi.view.ArcMenu;
import com.weidi.view.ArcMenu.OnMenuItemClickListener;
import com.weidi.view.TitleBarView;

public class TradeActivity extends ListActivity{
	public static final String TAG = "TradeActivity";
	private MyListAdapter mAdapter;
	TitleBarView mTitleBarView;
	ArcMenu mArcMenu;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_trade);
		initView();
		initTitleView();
		initData();
		new MyTask().execute();

	}
	
	
	 private void initView() {
		 mArcMenu = (ArcMenu) findViewById(R.id.arcmenu);
		 mTitleBarView=(TitleBarView)findViewById(R.id.title_bar);
	 }
	
	
	 private void initTitleView(){
			mTitleBarView.setCommonTitle(View.VISIBLE, View.VISIBLE,View.GONE,View.INVISIBLE, View.GONE);
			mTitleBarView.setTitleText(R.string.title_friend);
			mTitleBarView.setBtnLeft(R.drawable.fft, R.string.back);
			mTitleBarView.setBtnLeftOnclickListener(new OnClickListener() {	
				@Override
				public void onClick(View v) {
					finish();
				}
			});
			
			 mArcMenu.setOnMenuItemClickListener(new OnMenuItemClickListener() {

		            @Override
		            public void onItemClick(View view, int position) {
		                // TODO Auto-generated method stub
		                Toast.makeText(TradeActivity.this,
		                        (String) view.getTag() + ":" + position,
		                        Toast.LENGTH_SHORT).show();
		            }
		        });
		}
	 
	 private void initData(){
		//QApp.getFriend();
		 QApp.saveFriendMessage();
		 
	 }
	 

	class MyTask extends AsyncTask<Void, Void, MyMessage> {

		@Override
		protected MyMessage doInBackground(Void... params) {
			String json = "{\"code\":200,\"msg\":\"ok\",list:["
					+ "{\"id\":110,\"avator\":\"http://img0.bdstatic.com/img/image/shouye/leimu/mingxing.jpg\",\"name\":\"张三\",\"content\":\"大家好ddddddddddddfwfggsfgfbfvbfvvsdffwefsdgvsffffffffgvcbxvsd\",\"urls\":[]},"
					+ "{\"id\":111,\"avator\":\"http://img0.bdstatic.com/img/image/shouye/leimu/mingxing2.jpg\",\"name\":\"李四\",\"content\":\"大家好大家好ddddddddddddfwfggsfgfbfvbfvvsdffwefsdgvsffffffffgvcbxvsd\",\"urls\":[\"http://d.hiphotos.baidu.com/album/w%3D2048/sign=14b0934b78310a55c424d9f4837d42a9/a8014c086e061d95e9fd3e807af40ad163d9cacb.jpg\"]},"
					+ "{\"id\":112,\"avator\":\"http://img0.bdstatic.com/img/image/shouye/leimu/mingxing1.jpg\",\"name\":\"王五\",\"content\":\"大家好hfghhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhh\",\"urls\":[\"http://g.hiphotos.bdimg.com/album/s%3D680%3Bq%3D90/sign=ccd33b46d53f8794d7ff4b26e2207fc9/0d338744ebf81a4c0f993437d62a6059242da6a1.jpg\",\"http://c.hiphotos.bdimg.com/album/s%3D900%3Bq%3D90/sign=b8658f17f3d3572c62e290dcba28121a/5fdf8db1cb134954bb97309a574e9258d0094a47.jpg\"]},"
					+ "{\"id\":113,\"avator\":\"http://img0.bdstatic.com/img/image/shouye/leimu/mingxing6.jpg\",\"name\":\"赵六\",\"content\":\"大家好\",\"urls\":[\"http://f.hiphotos.bdimg.com/album/s%3D680%3Bq%3D90/sign=6b62f61bac6eddc422e7b7f309e0c7c0/6159252dd42a2834510deef55ab5c9ea14cebfa1.jpg\",\"http://g.hiphotos.bdimg.com/album/s%3D680%3Bq%3D90/sign=e58fb67bc8ea15ce45eee301863b4bce/a5c27d1ed21b0ef4fd6140a0dcc451da80cb3e47.jpg\",\"http://c.hiphotos.bdimg.com/album/s%3D680%3Bq%3D90/sign=cdab1512d000baa1be2c44b3772bc82f/91529822720e0cf3855c96050b46f21fbf09aaa1.jpg\"]}]}";
			Gson gson = new Gson();
			MyMessage msg = gson.fromJson(json, MyMessage.class);
			return msg;
		}

		@Override
		protected void onPostExecute(MyMessage result) {
			mAdapter = new MyListAdapter(TradeActivity.this, result.list);
			setListAdapter(mAdapter);

		}

	}
	
	
	  @Override
	    public boolean onCreateOptionsMenu(Menu menu) {
	        // Inflate the menu; this adds items to the action bar if it is present.
	        getMenuInflater().inflate(R.menu.a, menu);
	        return true;
	    }

	    @Override
	    public boolean onOptionsItemSelected(MenuItem item) {
	        // Handle action bar item clicks here. The action bar will
	        // automatically handle clicks on the Home/Up button, so long
	        // as you specify a parent activity in AndroidManifest.xml.
	        int id = item.getItemId();
	        if (id == R.id.action_settings) {
	            return true;
	        }
	        return super.onOptionsItemSelected(item);
	    }
}



/**
 * 
 */
package com.weidi.activity;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.weidi.R;
import com.weidi.adapter.SearchAdapter;
import com.weidi.chat.repository.RemarkRepo;
import com.weidi.common.base.LuoBaseActivity;
import com.weidi.db.NewFriendDao;

/**
 * @author luochangdong E-mail: 2270333671@qq.com
 * @date 创建时间：2015-6-25 下午4:50:41
 * @Description 1.0
 */
public class NewFriendActivity extends LuoBaseActivity {
	private ImageView topLeft;
	private TextView topTitle, topRight;
	private ListView listView;
	private List<String> friends = new ArrayList<String>();
	private SearchAdapter adapter;

	public void initData() {
		friends = NewFriendDao.getInstance().getNewFriend();
		adapter.addAll(friends);
		if (adapter.getCount() == 0) {
			listView.setVisibility(View.GONE);
		}
	}

	@Override
	protected void onDestroy() {

		super.onDestroy();
	}

	@Override
	protected void initView(Bundle savedInstanceState) {
		setContentView(R.layout.acti_new_friend);
		listView = (ListView) findViewById(R.id.lvFriendInvator);
		topLeft = (ImageView) findViewById(R.id.topLeft);
		topTitle = (TextView) findViewById(R.id.topTitle);
		topRight = (TextView) findViewById(R.id.topRight);
		topTitle.setText("新朋友");
	    topRight.setVisibility(View.GONE);
		adapter = new SearchAdapter(this);
		adapter.isNewFriend = true;
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent intent = new Intent(getApplicationContext(),
						FriendActivity.class);
				intent.putExtra("username", adapter.getItem(position));
				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(intent);
			}
		});
		initData();

		new RemarkRepo(this);
	}

	@Override
	protected void setListener() {
		topLeft.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});

	}

	@Override
	protected void afterViews(Bundle savedInstanceState) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void handleMsg(Message msg) {
		// TODO Auto-generated method stub

	}

}

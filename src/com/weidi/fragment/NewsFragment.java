package com.weidi.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jivesoftware.smack.Roster;
import org.jivesoftware.smack.XMPPConnection;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.weidi.MainActivity;
import com.weidi.QApp;
import com.weidi.R;
import com.weidi.activity.NoticeActivity;
import com.weidi.adapter.SessionAdapter;
import com.weidi.bean.Session;
import com.weidi.chat.IQOrder;
import com.weidi.chat.NewChatActivity;
import com.weidi.db.ChatMsgDao;
import com.weidi.db.NewsNotice;
import com.weidi.db.SessionDao;
import com.weidi.util.Const;
import com.weidi.util.Logger;
import com.weidi.util.PreferencesUtils;
import com.weidi.util.ToastUtil;
import com.weidi.util.XmppUtil;
import com.weidi.view.CustomListView;
import com.weidi.view.CustomListView.OnRefreshListener;

public class NewsFragment extends Fragment implements OnRefreshListener {
	private Context mContext;
	private View mBaseView;
	private CustomListView mCustomListView;
	private SessionAdapter adapter;
	private List<Session> sessionList = new ArrayList<Session>();
	private SessionDao sessionDao;
	private ChatMsgDao chatMsgDao;
	private String userid, title, content;
	private static String TAG = "NewsFragment";
	private AddFriendReceiver addFriendReceiver;
	private NewNoticeBroadcast newnoticebroadcast;
	private TextView red_shot, notice_title, notice_content;
	private static final int UPDATA = 10001;
	private MainActivity activity;

	@ViewInject(R.id.tvNoMsg)
	TextView tvNoMsg;

	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			if (msg.arg1 == UPDATA) {
				if (sessionList.size() == 0) {
					tvNoMsg.setVisibility(View.VISIBLE);
				} else {
					adapter = new SessionAdapter(mContext, sessionList);
					mCustomListView.setAdapter(adapter);
					tvNoMsg.setVisibility(View.GONE);
				}

			}
		};
	};

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mContext = getActivity();
		mBaseView = inflater.inflate(R.layout.fragment_news_father, null);
		ViewUtils.inject(this, mBaseView);
		userid = PreferencesUtils.getSharePreStr("username");
		if (userid.length() == 11) {
			userid = PreferencesUtils.getSharePreStr("weidi");
		}
		sessionDao = SessionDao.getInstance();
		chatMsgDao = ChatMsgDao.getInstance();
		addFriendReceiver = new AddFriendReceiver();
		IntentFilter intentFilter = new IntentFilter(Const.ACTION_ADDFRIEND);
		mContext.registerReceiver(addFriendReceiver, new IntentFilter(
				Const.ACTION_DELETE_MSG));
		mContext.registerReceiver(addFriendReceiver, intentFilter);

		newnoticebroadcast = new NewNoticeBroadcast();
		mContext.registerReceiver(newnoticebroadcast, new IntentFilter(
				Const.NEWSNOTICE));

		new mm().execute();

		return mBaseView;
	}

	@OnClick(R.id.llTopRight)
	public void right(View v) {
		activity.popMenu.showAsDropDown(v);
	}

	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
		this.activity = (MainActivity) activity;
	}

	private void findView() {

		red_shot = (TextView) mBaseView.findViewById(R.id.red_shot);
		notice_title = (TextView) mBaseView.findViewById(R.id.notice_title);
		notice_content = (TextView) mBaseView.findViewById(R.id.notice_content);
		RelativeLayout re_notice = (RelativeLayout) mBaseView
				.findViewById(R.id.re_notice);
		re_notice.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				startActivity(new Intent(mContext, NoticeActivity.class));
				red_shot.setVisibility(View.GONE);

			}
		});

		notice_title.setText(title);
		notice_content.setText(content);

		mCustomListView = (CustomListView) mBaseView.findViewById(R.id.lv_news);// listview
		mCustomListView.setOnRefreshListener(this);// 设置listview下拉刷新监听
		// mCustomListView.setCanLoadMore(false);// 设置禁止加载更多
		mCustomListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				final Session session = sessionList.get(arg2 - 1);
				if (session.getType().equals(Const.MSG_TYPE_ADD_FRIEND)) {
					if (!TextUtils.isEmpty(session.getIsdispose())) {
						if (!session.getIsdispose().equals("1")) {
							Builder bd = new AlertDialog.Builder(mContext);
							bd.setItems(new String[] { "同意" },
									new OnClickListener() {
										@Override
										public void onClick(
												DialogInterface arg0, int arg1) {
											XMPPConnection con = QApp
													.getXmppConnection();
											Roster roster = con.getRoster();

											final String toJid = session
													.getFrom()
													+ "@"
													+ QApp.getXmppConnection()
															.getServiceName();
											String fromJid = userid
													+ "@"
													+ QApp.getXmppConnection()
															.getServiceName();
											Logger.e(TAG, toJid + "and"
													+ session.getFrom());

											XmppUtil.addUsers(roster, toJid,
													session.getFrom(), "我的好友");
											QApp.mLocalBroadcastManager
													.sendBroadcast(new Intent(
															NewConstactFragment.REFRESH_CONSTACT));

											sessionDao
													.updateSessionToDisPose(session
															.getId());// 将本条数据在数据库中改为已处理
											sessionList.remove(session);
											session.setIsdispose("1");
											sessionList.add(0, session);

											adapter.notifyDataSetChanged();
											// new Thread(new Runnable() {
											// public void run() {
											// try {
											//
											// XmppUtil.sendMessage(
											// QApp.xmppConnection,
											// "我们已经是好友了，快来和我聊天吧！",
											// session.getFrom());
											// } catch (XMPPException e) {
											// // TODO Auto-generated
											// // catch block
											// e.printStackTrace();
											// }
											// // 发送广播更新好友列表
											// }
											// }).start();

										}
									});
							bd.create().show();
						} else {
							ToastUtil.showShortToast(mContext, "已同意");
						}
					}
				} else {
					Intent intent = new Intent(mContext, NewChatActivity.class);
					if (session.getFrom().contains("g")) {
						intent.putExtra("from", session.getFrom());
					} else {
						intent.putExtra("from", session.getFrom());
					}
					startActivity(intent);
				}
			}
		});
		mCustomListView
				.setOnItemLongClickListener(new OnItemLongClickListener() {
					@Override
					public boolean onItemLongClick(AdapterView<?> arg0,
							View arg1, int arg2, long arg3) {
						final Session session = sessionList.get(arg2 - 1);
						Builder bd = new AlertDialog.Builder(mContext);
						bd.setItems(new String[] { "删除会话" },
								new OnClickListener() {
									@Override
									public void onClick(DialogInterface arg0,
											int arg1) {
										deleteMsg(session);
										initData();
									}

									private void deleteMsg(final Session session) {
										sessionDao.deleteSession(session);
										chatMsgDao.deleteAllMsg(
												session.getFrom(),
												session.getTo());
										mContext.sendBroadcast(new Intent(
												Const.ACTION_NEW_MSG));
										initData();
									}
								}).create().show();
						return true;
					}
				});

	}

	private void initData() {
		new Thread(new Runnable() {

			@Override
			public void run() {
				// 注意，当数据量较多时，此处要开线程了，否则阻塞主线程
				sessionList = sessionDao.queryAllSessions(userid);
				Message msg = new Message();
				msg.obj = sessionList;
				msg.arg1 = UPDATA;
				mHandler.sendMessage(msg);
			}
		}).start();

	}

	@Override
	public void onStart() {
		super.onStart();
		initData();
	}

	@Override
	public void onRefresh() {
		initData();
		mCustomListView.onRefreshComplete();
	}

	class AddFriendReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context arg0, Intent intent) {

			initData();
		}

	}

	@Override
	public void onDestroy() {

		mContext.unregisterReceiver(addFriendReceiver);
		mContext.unregisterReceiver(newnoticebroadcast);
		super.onDestroy();
	}

	class NewNoticeBroadcast extends BroadcastReceiver {

		@Override
		public void onReceive(Context arg0, Intent arg1) {
			try {
				if (Const.newscount != 0) {
					red_shot.setVisibility(View.VISIBLE);
					red_shot.setText("" + Const.newscount);
					NewsNotice news = NewsNotice.getInstance();
					List<Map<String, Object>> news_list = news.query();
					Map<String, Object> map = new HashMap<String, Object>();
					map = news_list.get(0);
					title = (String) map.get("title");
					content = (String) map.get("content");
					notice_title.setText(title);
					notice_content.setText(content);
				}
			} catch (Exception e) {
				// TODO: handle exception
			}

		}
	}

	public void getNewData() {
		NewsNotice news = NewsNotice.getInstance();
		List<Map<String, Object>> news_list = news.query();
		Map<String, Object> map = new HashMap<String, Object>();
		if (news_list.size() == 0)
			return;
		map = news_list.get(0);
		title = (String) map.get("title");
		content = (String) map.get("content");
		Log.i("fff", "kkk");
	}

	public void send_NewsIQ() {
		NewsNotice news = NewsNotice.getInstance();
		List<Map<String, Object>> news_list = news.query();
		Map<String, Object> map = new HashMap<String, Object>();
		if (news_list.size() != 0) {
			map = news_list.get(0);
			String createdatetime = (String) map.get("createdatetime");
			IQOrder.getInstance().getNews2(createdatetime);

		} else {
			IQOrder.getInstance().getNews();

		}
		mContext.sendBroadcast(new Intent(Const.NEWSNOTICE));
	}

	class mm extends AsyncTask<Void, Integer, Integer> {

		@Override
		protected Integer doInBackground(Void... arg0) {
			send_NewsIQ();
			return 1;
		}

		@Override
		protected void onPostExecute(Integer result) {
			super.onPostExecute(result);
			findView();
			if (result == 1)
				getNewData();
			initData();
		}
	}

}

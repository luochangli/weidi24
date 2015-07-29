package com.weidi.fragment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.jivesoftware.smack.util.StringUtils;
import org.jivesoftware.smackx.packet.VCard;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.content.LocalBroadcastManager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.weidi.MainActivity;
import com.weidi.QApp;
import com.weidi.R;
import com.weidi.activity.ChatActivity;
import com.weidi.activity.FriendActivity;
import com.weidi.activity.FriendInfoActi;
import com.weidi.activity.FriendInfoActivity;
import com.weidi.activity.NewFriendActivity;
import com.weidi.activity.SearchActivity;
import com.weidi.bean.Friend;
import com.weidi.bean.User;
import com.weidi.chat.groupchat.GroupListActivity;
import com.weidi.common.CharacterParser;
import com.weidi.common.ClearEditText;
import com.weidi.common.PinyinComparator;
import com.weidi.common.SideBar;
import com.weidi.common.SideBar.OnTouchingLetterChangedListener;
import com.weidi.common.SortAdapter;
import com.weidi.common.SortModel;
import com.weidi.common.ViewHolder;
import com.weidi.common.base.BaseFragment;
import com.weidi.db.NewFriendDao;
import com.weidi.db.SessionDao;
import com.weidi.db.VCardDao;
import com.weidi.util.Const;
import com.weidi.util.Logger;
import com.weidi.util.ShowPopWindow;
import com.weidi.util.ToastUtil;
import com.weidi.util.XmppLoadThread;
import com.weidi.util.XmppUtil;
import com.weidi.view.CircleImageView;
import com.weidi.view.FriendPopWindow;

/**
 * @author luochangdong E-mail: 2270333671@qq.com
 * @date 创建时间：2015-6-8 下午5:19:17
 * @Description 1.0
 */
public class NewConstactFragment extends BaseFragment {
	private static String TAG = "NewConstactFragment";
	public static final String REFRESH_CONSTACT = "refresh_constacts";
	public static final String SHOW_NEW_FRIEND = "show_new_friend";
	private ListView sortListView;
	private SideBar sideBar;
	private TextView dialog;
	private SortAdapter adapter;
	private ClearEditText mClearEditText;
	private TextView tvTotal, newFriCount;
	private Context mContext;
	private RelativeLayout tvNewFriend, rlAddFriend, re_chatroom;
	private PopupWindow popFriendInfo;// 好友信息
	FriendPopWindow addPopWindow;// 好友更多
	private LayoutInflater layoutInflater;
	private TextView popSendMsg, popSendVideo, popFriendWeidi, popFriendAddr,
			popFriendSign, popFriendNick;
	private CircleImageView civHeadImg;
	private ImageView popFriendGender, popFriendClose, popFriendMore;
	private User friend;
	List<String> friendData;
	MainActivity activity;
	@ViewInject(R.id.llTopRight)
	LinearLayout llTopRight;

	/**
	 * 汉字转换成拼音的类
	 */
	private CharacterParser characterParser;
	public static List<SortModel> sourceDateList;

	/**
	 * 根据拼音来排列ListView里面的数据类
	 */
	private PinyinComparator pinyinComparator;

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();

	}

	@Override
	protected void initView(Bundle savedInstanceState) {
		setRootView(R.layout.frag_constact);
		ViewUtils.inject(this, mRootView);
		mContext = getActivity();
		// 实例化汉字转拼音类
		characterParser = CharacterParser.getInstance();
		layoutInflater = (LayoutInflater) mContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		pinyinComparator = new PinyinComparator();
		sourceDateList = new ArrayList<SortModel>();
		friendData = new ArrayList<String>();
		initView();
		pinyinComparator = new PinyinComparator();
		sourceDateList = new ArrayList<SortModel>();
		initData();
		tvTotal.setText(String.valueOf(sourceDateList.size()) + "位联系人");
		adapter = new SortAdapter(mApp, sourceDateList);
		sortListView.setAdapter(adapter);
		initBroadcast();

	}

	private void initBroadcast() {
		mLocalBroadcastManager = LocalBroadcastManager.getInstance(mContext);
		mReceiver = new BroadcastReceiver() {

			@Override
			public void onReceive(Context context, Intent intent) {
				if (intent.getAction().equals(REFRESH_CONSTACT)) {
					mHandler.sendEmptyMessage(1);// 更新界面
					QApp.mLocalBroadcastManager.sendBroadcast(new Intent(
							FriendActivity.SHOW_IS_FRIEND));
				}
				if (intent.getAction().equals(SHOW_NEW_FRIEND)) {
					updateConstant();
				}

			}
		};
		mLocalBroadcastManager.registerReceiver(mReceiver, new IntentFilter(
				REFRESH_CONSTACT));
		mLocalBroadcastManager.registerReceiver(mReceiver, new IntentFilter(
				SHOW_NEW_FRIEND));
	}

	private void initView() {
		sortListView = (ListView) mRootView
				.findViewById(R.id.country_lvcountry);
		dialog = (TextView) mRootView.findViewById(R.id.dialog);
		sideBar = (SideBar) mRootView.findViewById(R.id.sidrbar);
		sideBar.setTextView(dialog);
		// 通讯录页眉页脚
		LayoutInflater infalter = LayoutInflater.from(mContext);
		View headView = infalter.inflate(R.layout.item_contact_list_header,
				null);
		sortListView.addHeaderView(headView);
		View footerView = infalter.inflate(R.layout.item_contact_list_footer,
				null);
		sortListView.addFooterView(footerView);
		// SourceDateList =
		// filledData(getResources().getStringArray(R.array.date));
		// 新的朋友
		tvNewFriend = (RelativeLayout) headView
				.findViewById(R.id.re_newfriends);
		rlAddFriend = (RelativeLayout) headView.findViewById(R.id.rlAddFriend);
		newFriCount = (TextView) headView.findViewById(R.id.tv_unread);
		tvTotal = (TextView) footerView.findViewById(R.id.tv_total);
		mClearEditText = (ClearEditText) mRootView
				.findViewById(R.id.filter_edit);
		re_chatroom = (RelativeLayout) headView.findViewById(R.id.re_chatroom);
	}

	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
		this.activity = (MainActivity) activity;
	}

	@Override
	protected void setListener() {
		llTopRight.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				activity.moreDialog();
			}
		});
		// 设置右侧触摸监听
		sideBar.setOnTouchingLetterChangedListener(new OnTouchingLetterChangedListener() {

			@Override
			public void onTouchingLetterChanged(String s) {
				// 该字母首次出现的位置
				int position = adapter.getPositionForSection(s.charAt(0));
				if (position != -1) {
					sortListView.setSelection(position);
				}

			}
		});
		//
		sortListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// 这里要利用adapter.getItem(position)来获取当前position所对应的对象
				if (position != 0 && position != sourceDateList.size() + 1) {
					String from = ((SortModel) adapter.getItem(position - 1))
							.getValue();
					// ShowPopWindow mm=new ShowPopWindow(mContext, 0);
					// showPopupWindow(view, from);
					Intent in = new Intent(mContext, FriendInfoActi.class);
					Bundle bundle = new Bundle();
					bundle.putString(Const.USER_NAME, from);
					in.putExtras(bundle);
					startActivity(in);

				}

			}
		});

		// 根据输入框输入值的改变来过滤搜索
		mClearEditText.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// 当输入框里面的值为空，更新为原来的列表，否则为过滤数据列表
				filterData(s.toString());
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {

			}

			@Override
			public void afterTextChanged(Editable s) {
			}
		});
		// 新的朋友
		tvNewFriend.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(mContext, NewFriendActivity.class);
				startActivity(intent);

			}
		});
		rlAddFriend.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				startActivity(new Intent(mContext, SearchActivity.class));
			}
		});

		re_chatroom.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// startActivity(new Intent(mContext, GroupListActivity.class));
			}
		});
	}

	/**
	 * 为ListView填充数据
	 * 
	 * @param data
	 * @return
	 */
	private List<SortModel> filledData(List<String> data) {
		List<SortModel> mSortList = new ArrayList<SortModel>();

		User user;
		for (int i = 0; i < data.size(); i++) {
			try {
				SortModel sortModel = new SortModel();
				user = VCardDao.getInstance().isContain(
						StringUtils.parseName(data.get(i)));
				if (user == null) {
					user = new User(XmppUtil.getUserInfo(StringUtils
							.parseName(data.get(i))));
				}
				String nick = user.getNickname() == null ? user.getUsername()
						: user.getNickname();
				sortModel.setName(nick);
				sortModel.setValue(user.getUsername());

				// 汉字转换成拼音
				String pinyin = characterParser.getSelling(nick);
				String sortString = pinyin.substring(0, 1).toUpperCase();

				// 正则表达式，判断首字母是否是英文字母
				if (sortString.matches("[A-Z]")) {
					sortModel.setSortLetters(sortString.toUpperCase());
				} else {
					sortModel.setSortLetters("#");
				}

				mSortList.add(sortModel);
			} catch (Exception e) {

			}

		}
		return mSortList;

	}

	/**
	 * 根据输入框中的值来过滤数据并更新ListView
	 * 
	 * @param filterStr
	 */
	private void filterData(String filterStr) {
		List<SortModel> filterDateList = new ArrayList<SortModel>();

		if (TextUtils.isEmpty(filterStr)) {
			filterDateList = sourceDateList;
		} else {
			filterDateList.clear();
			for (SortModel sortModel : sourceDateList) {
				String name = sortModel.getName();
				if (name.indexOf(filterStr.toString()) != -1
						|| characterParser.getSelling(name).startsWith(
								filterStr.toString())) {
					filterDateList.add(sortModel);
				}
			}
		}

		// 根据a-z进行排序
		Collections.sort(filterDateList, pinyinComparator);
		tvTotal.setText(String.valueOf(sourceDateList.size()) + "位联系人");
		adapter.updateListView(filterDateList);

	}

	@Override
	protected void handleMsg(Message msg) {
		switch (msg.what) {
		case 1:

			initData();
			tvTotal.setText(String.valueOf(sourceDateList.size()) + "位联系人");
			adapter.updateListView(sourceDateList);
			break;
		}
	}

	@Override
	protected void afterViews(Bundle savedInstanceState) {

	}

	private void initData() {
		friendData.clear();
		sourceDateList.clear();
		friendData = XmppUtil.getRosterBoth(QApp.getXmppConnection()
				.getRoster());
		if (friendData.size() > 0) {
			sourceDateList = filledData(friendData);
			Logger.i(TAG, "好友数量=" + sourceDateList.size());
		}
		// 根据a-z进行排序源数据
		Collections.sort(sourceDateList, pinyinComparator);
	}

	private void showPopupWindow(View parent, String friendWeidi) {
		if (popFriendInfo == null) {
			View view = layoutInflater.inflate(R.layout.pop_friend_info, null);
			popFriendInfo = new PopupWindow(view, LayoutParams.MATCH_PARENT,
					LayoutParams.MATCH_PARENT, true);
			initPop(view, friendWeidi);
		}
		popFriendInfo.setAnimationStyle(android.R.style.Animation_InputMethod);
		popFriendInfo.setFocusable(true);
		popFriendInfo.setOutsideTouchable(true);
		popFriendInfo.setBackgroundDrawable(new BitmapDrawable());
		popFriendInfo
				.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
		popFriendInfo.showAtLocation(parent, Gravity.CENTER, 0, 0);
	}

	public void initPop(View view, final String friendWeidi) {
		popFriendClose = (ImageView) view.findViewById(R.id.ivPopClose);// 退出
		popFriendMore = (ImageView) view.findViewById(R.id.ivFriendMore);
		popSendMsg = (TextView) view.findViewById(R.id.tvSendMsg);
		popSendVideo = (TextView) view.findViewById(R.id.tvSendVideo);
		civHeadImg = (CircleImageView) view.findViewById(R.id.civPopHeadImg);
		popFriendAddr = (TextView) view.findViewById(R.id.tvFriendAddr);
		popFriendGender = (ImageView) view.findViewById(R.id.ivFriendGender);
		popFriendSign = (TextView) view.findViewById(R.id.tvFriendSign);
		popFriendWeidi = (TextView) view.findViewById(R.id.tvFriendWeidi);
		popFriendNick = (TextView) view.findViewById(R.id.tvFriendName);

		addPopWindow = new FriendPopWindow(layoutInflater,
				R.layout.pop_friend_more) {

			@Override
			public void convert(ViewHolder helper) {
				RelativeLayout rlDelete = helper.getView(R.id.rlDelete);
				rlDelete.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						XmppUtil.removeUser(QApp.getXmppConnection()
								.getRoster(), XmppUtil
								.getFullUsername(friendWeidi));
						ToastUtil.showShortToast(mContext, "移除成功");
						mLocalBroadcastManager.sendBroadcast(new Intent(
								REFRESH_CONSTACT));
						SessionDao.getInstance().deleteYou(friendWeidi);
						closePop();
					}
				});
			}
		};
		initFriendData(friendWeidi);
		setPopFriend(friendWeidi);
	}

	private void closePop() {
		popFriendInfo.dismiss();
		popFriendInfo = null;
		addPopWindow.dismiss();
		addPopWindow = null;

	}

	private void setPopFriend(final String friendWeidi) {
		popFriendClose.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				closePop();
			}

		});
		popFriendMore.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				addPopWindow.showPopupWindow(mClearEditText);

			}
		});
		popSendMsg.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent();
				intent.putExtra("from", friendWeidi);
				intent.setClass(mContext, ChatActivity.class);
				startActivity(intent);
				popFriendInfo.dismiss();
				popFriendInfo = null;
			}
		});
		popSendVideo.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {

			}
		});
	}

	private void initFriendData(final String friendWeidi) {
		new XmppLoadThread(mContext) {

			@Override
			protected void result(Object object) {

				if (friend != null) {
					friend = new User(XmppUtil.getUserInfo(friendWeidi));
					fillFriendInfo();
					popFriendWeidi.setText(friendWeidi);
					VCardDao.getInstance().insertUser(friend);
			

				} else {

					VCard vCard = (VCard) object;
					friend = new User(vCard);
					fillFriendInfo();
					popFriendWeidi.setText(friendWeidi);
					VCardDao.getInstance().insertUser(friend);
	
				}

			}

			private void fillFriendInfo() {
				if (friend.getSex() != null) {
					if (friend.getSex().equals("男")) {
						popFriendGender.setImageResource(R.drawable.male);
					} else {
						popFriendGender.setImageResource(R.drawable.female);
					}
				}
				if (friend.getIntro() != null) {
					popFriendSign.setText(friend.getIntro());
				}
				if (friend.getNickname() != null) {
					popFriendNick.setText(friend.getNickname());
				}
				if (friend.getAdr() != null) {
					popFriendAddr.setText(friend.getAdr());
				}
				friend.showHead(civHeadImg);
			}

			@Override
			protected Object load() {

				friend = VCardDao.getInstance().isContain(friendWeidi);
				if (friend == null) {
					return XmppUtil.getUserInfo(friendWeidi);
				} else {
					return friend;
				}
			}
		};
	}

	public void updateConstant() {
		// 更新界面
		int count = NewFriendDao.getInstance().getUnDealCount();
		if (count > 0) {
			newFriCount.setVisibility(View.VISIBLE);
			newFriCount.setText("" + count);
		} else {
			newFriCount.setVisibility(View.GONE);
		}
	}

	public boolean compareContent(User friend) {
		String old_sex = friend.getSex();
		String old_intro = friend.getIntro();
		String old_nickname = friend.getNickname();
		String old_adr = friend.getAdr();
		VCard vcard = XmppUtil.getUserInfo(friend.getUsername());
		String new_sex = vcard.getField("sex");
		String new_nickname = vcard.getField("nickName");
		String new_adr = vcard.getField("adr");
		String new_intro = vcard.getField("intro");
		if (new_sex.equals(old_sex) && new_nickname.equals(old_nickname)
				&& new_adr.equals(old_adr) && new_intro.equals(old_intro)) {

			return false;

		} else {
			return true;
		}
	}

	@Override
	public void onDestroy() {
		mLocalBroadcastManager.unregisterReceiver(mReceiver);
		super.onDestroy();
	}
}

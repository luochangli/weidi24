package com.weidi.util;


import java.util.List;

import org.jivesoftware.smackx.packet.VCard;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.BitmapDrawable;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.weidi.QApp;
import com.weidi.R;
import com.weidi.activity.FriendActivity;
import com.weidi.bean.User;
import com.weidi.common.ClearEditText;
import com.weidi.db.VCardDao;
import com.weidi.view.CircleImageView;
import com.weidi.view.FriendPopWindow;

public class ShowPopWindow {
	
	public static final String REFRESH_CONSTACT = "refresh_constacts"; 
	public static final String SHOW_NEW_FRIEND = "show_new_friend";
	private Context mContext;
	private PopupWindow popFriendInfo;// 好友信息
	FriendPopWindow addPopWindow;// 好友更多
	private LayoutInflater layoutInflater;
	private TextView popFriendWeidi, popFriendAddr,
			popFriendSign, popFriendNick,popSendMsg,popSendVideo,remove;
	private CircleImageView civHeadImg;
	private ClearEditText mClearEditText;
	private ImageView popFriendGender, popFriendClose, popFriendMore;
	private User friend;
	List<String> friendData;
	private LocalBroadcastManager mLocalBroadcastManager;
	private BroadcastReceiver mReceiver;
	int state=0;
	View view_layout;
	Button operBtn;
	String weidi;
	public  ShowPopWindow(Context mContext,int state,String weidi){
		this.mContext=mContext;
		this.state=state;
		this.weidi=weidi;
	}
	
	public void initBroadcast() {
		mLocalBroadcastManager = LocalBroadcastManager.getInstance(mContext);
		mReceiver = new BroadcastReceiver() {
			
			@Override
			public void onReceive(Context context, Intent intent) {
				if(intent.getAction().equals(REFRESH_CONSTACT)){
					
					QApp.mLocalBroadcastManager.sendBroadcast(new Intent(FriendActivity.SHOW_IS_FRIEND));
				}
				
			}
		};
		mLocalBroadcastManager.registerReceiver(mReceiver, new IntentFilter(REFRESH_CONSTACT));
		mLocalBroadcastManager.registerReceiver(mReceiver, new IntentFilter(SHOW_NEW_FRIEND));
	}

	/**
	 * 为ListView填充数据
	 * 
	 * @param data
	 * @return
	 *//*
	public List<SortModel> filledData(List<String> data) {
		List<SortModel> mSortList = new ArrayList<SortModel>();

		User user;
		for (int i = 0; i < data.size(); i++) {
			try{
				SortModel sortModel = new SortModel();
				user = VCardDao.getInstance().isContain(StringUtils.parseName(data.get(i)));
				if (user == null) {
					user = new User(XmppUtil.getUserInfo(StringUtils.parseName(data.get(i))));
				}
				String nick = user.getNickname() == null ? user.getUsername()
						: user.getNickname();
				sortModel.setName(nick);
				sortModel.setValue(user.getUsername());

				

				mSortList.add(sortModel);
			}catch(Exception e){
				
			}
			
			
		}
		return mSortList;

	}*/

	public void showPopupWindow(View parent, String friendWeidi) {
		Log.i("QQQQQQQQQQQ", friendWeidi);
		layoutInflater = (LayoutInflater) mContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		initBroadcast();
		if (popFriendInfo == null) {
			/*if(state==0){
				view_layout = layoutInflater.inflate(R.layout.pop_friend_info, null);
			}else{
				view_layout = layoutInflater.inflate(R.layout.pop_friend_info1, null);
				Log.i("TTTT", "TTTT");
			}*/
			view_layout = layoutInflater.inflate(R.layout.pop_friend_info1, null);
			popFriendInfo = new PopupWindow(view_layout, LayoutParams.MATCH_PARENT,
					LayoutParams.MATCH_PARENT, true);
			initPop(view_layout, friendWeidi);
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
		civHeadImg = (CircleImageView) view.findViewById(R.id.civPopHeadImg);
		/*popSendMsg = (TextView) view.findViewById(R.id.tvSendMsg);
		popSendVideo = (TextView) view.findViewById(R.id.tvSendVideo);*/
		//remove = (TextView) view.findViewById(R.id.remove);
		popFriendAddr = (TextView) view.findViewById(R.id.tvFriendAddr);
		popFriendGender = (ImageView) view.findViewById(R.id.ivFriendGender);
		popFriendSign = (TextView) view.findViewById(R.id.tvFriendSign);
		popFriendWeidi = (TextView) view.findViewById(R.id.tvFriendWeidi);
		popFriendNick = (TextView) view.findViewById(R.id.tvFriendName);
		operBtn=(Button)view.findViewById(R.id.remove);
		/*addPopWindow = new FriendPopWindow(layoutInflater,
				R.layout.pop_friend_more) {

			@Override
			public void convert(ViewHolder helper) {
				RelativeLayout rlDelete = helper.getView(R.id.rlDelete);
				rlDelete.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						XmppUtil.removeUser(QApp.getXmppConnection().getRoster(),XmppUtil.getFullUsername(friendWeidi));
						ToastUtil.showShortToast(mContext, "移除成功");
					    mLocalBroadcastManager.sendBroadcast(new Intent(REFRESH_CONSTACT));
						closePop();
					}
				});
			}
		};*/
		initFriendData(friendWeidi);
		setPopFriend(friendWeidi);
	}

	public void closePop() {
		popFriendInfo.dismiss();
		popFriendInfo = null;
		/*addPopWindow.dismiss();
		addPopWindow = null;*/

	}

	public void setPopFriend(final String friendWeidi) {
		popFriendClose.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				closePop();
			}

		});
		/*popFriendMore.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				addPopWindow.showPopupWindow(mClearEditText);
				Log.i("TAG", "GGGGGGGGG");
			}
		});*/
		/*popSendMsg.setOnClickListener(new OnClickListener() {
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
		});*/
		/*operBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				if (operBtn.getText().equals("添加到通讯录")) {
					isFriend();
					XmppUtil.addUsers(QApp.getXmppConnection().getRoster(),XmppUtil.getFullUsername(friendWeidi),friendWeidi,"friend");
					ToastUtil
							.showShortToast(mContext, "添加成功，等待通过验证");
				    QApp.mLocalBroadcastManager.sendBroadcast(new Intent(NewConstactFragment.REFRESH_CONSTACT));
				} else if (operBtn.getText().equals("移出通讯录")) {
					XmppUtil.removeUser(QApp.getXmppConnection().getRoster(), XmppUtil.getFullUsername(friendWeidi));
					ToastUtil.showShortToast(mContext, "移除成功"+friendWeidi);
					operBtn.setText("添加到通讯录");
					QApp.mLocalBroadcastManager.sendBroadcast(new Intent(NewConstactFragment.REFRESH_CONSTACT));
	                //删除聊天数据和其他与该好友的数据
					SessionDao.getInstance().deleteYou(friendWeidi);
					((ChatActivity)mContext).finish();
				}
			}
		});*/
	}
	
	public void isFriend() {
		if (XmppUtil.getRosterBoth(QApp.getXmppConnection().getRoster())
				.contains(XmppUtil.getFullUsername(weidi))) {
			operBtn.setText("移出通讯录");
		} else {
			operBtn.setText("添加到通讯录");
		}
	}

	public void initFriendData(final String friendWeidi) {
		new XmppLoadThread(mContext) {
			@Override
			protected void result(Object object) {
				if (friend != null) {
					//boolean aa=compareContent(friend);
						/*Log.i("GGGGGG", "这不是第一次创建friend但他们的内容改变了"); 
						friend = new User(XmppUtil.getUserInfo(friendWeidi));
						fillFriendInfo();
						popFriendWeidi.setText(friendWeidi);
						VCardDao.getInstance().insertUser(friend);*/
						Log.i("GGGGGG", "这不是第一次创建friend"); 
						fillFriendInfo();
						popFriendWeidi.setText(friendWeidi);
						friend = null;
						
				
				} else {
					VCard vCard = (VCard) object;
					friend = new User(vCard);
					fillFriendInfo();
					popFriendWeidi.setText(friendWeidi);
					VCardDao.getInstance().insertUser(friend);
					Log.i("GGGGGG", "这是第一次创建friend"); 
				}
				
			}

			public void fillFriendInfo() {
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
//				friend.showHead(civHeadImg);
			}

			@Override
			protected Object load() {
				friend = VCardDao.getInstance().getUser(weidi);
				if (friend == null) {
					return XmppUtil.getUserInfo(weidi);
				} else {
					return friend;
				}
			}
		};
	}
}

package com.weidi.activity;

import org.jivesoftware.smackx.packet.VCard;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.content.LocalBroadcastManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.weidi.QApp;
import com.weidi.R;
import com.weidi.bean.User;
import com.weidi.common.base.BaseActivity;
import com.weidi.db.SessionDao;
import com.weidi.fragment.NewConstactFragment;
import com.weidi.util.Const;
import com.weidi.util.ToastUtil;
import com.weidi.util.XmppLoadThread;
import com.weidi.util.XmppUtil;
import com.weidi.view.CircleImageView;

/**
 * @author luochangdong E-mail: 2270333671@qq.com
 * @date 创建时间：2015-6-25 下午4:50:41
 * @Description 1.0
 */
public class FriendActivity extends BaseActivity implements OnClickListener {
   public static final String SHOW_IS_FRIEND = "show_is_friend";
	Button operBtn;
	TextView nameView, sexView, signView, nickNameView, phoneView, emailView;
	CircleImageView headView;
	ImageView leftBtn;
	private String username;
	private User friend;

	@Override
	protected void initView(Bundle savedInstanceState) {
		setContentView(R.layout.acti_friend);

		initView();

		username = getIntent().getStringExtra(Const.YOU);
		operBtn.setOnClickListener(this);
		nameView.setText(username);
		

		if (username.equals(Const.loginUser.getUsername())) {
			operBtn.setVisibility(View.GONE);
		}
		isFriend();
		initData();
       initBraodcast();
	}

	private void initBraodcast() {
		mLocalBroadcastManager = LocalBroadcastManager.getInstance(mContext);
		   mReceiver = new BroadcastReceiver() {
			
			@Override
			public void onReceive(Context context, Intent intent) {
				isFriend();
			}
		};
		mLocalBroadcastManager.registerReceiver(mReceiver, new IntentFilter(SHOW_IS_FRIEND));
	}

	private void initView() {
		operBtn = (Button) findViewById(R.id.operBtn);
		nameView = (TextView) findViewById(R.id.nameView);
		sexView = (TextView) findViewById(R.id.sexView);
		signView = (TextView) findViewById(R.id.signView);
		nickNameView = (TextView) findViewById(R.id.nickNameView);
		phoneView = (TextView) findViewById(R.id.phoneView);
		emailView = (TextView) findViewById(R.id.emailView);
		headView = (CircleImageView) findViewById(R.id.headView);
		leftBtn = (ImageView) findViewById(R.id.leftBtn);
	}

	public void initData() {
		new XmppLoadThread(this) {

			@Override
			protected void result(Object object) {
				VCard vCard = (VCard) object;
				friend = new User(vCard);
				if (friend != null) {
					if (friend.getSex() != null) {
						sexView.setText(friend.getSex());
					}
					if (friend.getIntro() != null) {
						signView.setText(friend.getIntro());
					}
					if (friend.getNickname() != null) {
						nickNameView.setText("    "+friend.getNickname());
					}
					if (friend.getEmail() != null) {
						emailView.setText("    "+friend.getEmail());
					}
					if (friend.getMobile() != null) {
						phoneView.setText("    "+friend.getMobile());
					}
					friend.showHead(headView,mHandler);
				}
			}

			@Override
			protected Object load() {
				return XmppUtil.getUserInfo(username);
			}
		};
	}

	public void isFriend() {
		if (XmppUtil.getRosterBoth(QApp.getXmppConnection().getRoster())
				.contains(XmppUtil.getFullUsername(username))) {
			operBtn.setText("移出通讯录");
		} else {
			operBtn.setText("添加到通讯录");
		}
	}

	@Override
	protected void setListener() {
		operBtn.setOnClickListener(this);
        leftBtn.setOnClickListener(this);
	}

	@Override
	protected void afterViews(Bundle savedInstanceState) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void handleMsg(Message msg) {
		// TODO Auto-generated method stub

	}


	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.operBtn:
			if (operBtn.getText().equals("添加到通讯录")) {
				isFriend();
				XmppUtil.addUsers(QApp.getXmppConnection().getRoster(),XmppUtil.getFullUsername(username),username,"friend");
				ToastUtil
						.showShortToast(getApplicationContext(), "添加成功，等待通过验证");
				QApp.mLocalBroadcastManager.sendBroadcast(new Intent(
						NewConstactFragment.REFRESH_CONSTACT));
			    finish();
			} else if (operBtn.getText().equals("移出通讯录")) {
				XmppUtil.removeUser(QApp.getXmppConnection().getRoster(), XmppUtil.getFullUsername(username));
				ToastUtil.showShortToast(getApplicationContext(), "移除成功"+username);
				operBtn.setText("添加到通讯录");
				QApp.mLocalBroadcastManager.sendBroadcast(new Intent(NewConstactFragment.REFRESH_CONSTACT));
                //删除聊天数据和其他与该好友的数据
				SessionDao.getInstance().deleteYou(username);
			}
			break;
		case R.id.leftBtn:
			finish();
			break;

		default:
			break;
		}

	}
	
	@Override
	protected void onDestroy() {
		mLocalBroadcastManager.unregisterReceiver(mReceiver);
		super.onDestroy();
	}
}

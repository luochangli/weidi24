package com.weidi.chat;

import java.io.Serializable;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import org.jivesoftware.smack.util.StringUtils;
import org.xbill.DNS.tests.primary;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.content.LocalBroadcastManager;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.weidi.MainActivity;
import com.weidi.R;
import com.weidi.activity.FriendActivity;
import com.weidi.chat.bean.GroupInfo;
import com.weidi.chat.bean.GroupInfoDao;
import com.weidi.chat.bean.Menbers;
import com.weidi.chat.bean.MenbersDao;
import com.weidi.chat.groupchat.CreatChatRoomActivity;
import com.weidi.chat.repository.AvatarRepo;
import com.weidi.chat.repository.MucChatRepo;
import com.weidi.common.CommonAdapter;
import com.weidi.common.ViewHolder;
import com.weidi.common.base.BaseActivity;
import com.weidi.common.image.ImgConfig;
import com.weidi.db.SessionDao;
import com.weidi.provider.ObtainMUCDestroyIQ;
import com.weidi.provider.ObtainMUCExitIQ;
import com.weidi.provider.ObtainMUCInfoIQ;
import com.weidi.provider.ObtainMUCKickIQ;
import com.weidi.provider.ObtainMUCmemberIQ;
import com.weidi.provider.ObtainMUCmemberIQ.Item;
import com.weidi.util.Const;
import com.weidi.util.Logger;
import com.weidi.util.PreferencesUtils;
import com.weidi.util.ToastUtil;
import com.weidi.util.XmppLoadThread;
import com.weidi.util.XmppUtil;
import com.weidi.view.CircleImageView;
import com.weidi.view.ExpandGridView;
import com.weidi.view.LoadingDialog;

/**
 * @author luochangdong E-mail: 2270333671@qq.com
 * @date 创建时间：2015-7-9 下午2:36:29
 * @Description 1.0
 */
public class GroupChatSettingActi extends BaseActivity implements
		OnClickListener {

	public static final String GROUP_MENBER_LIST = "group_menber_list";
	private LoadingDialog loadDialog;
	private static final int MY_MUCNICK = 100;
	private static final int KICK_MENBER = 110;
	public static final int INFO = 113;
	public static final int MUC_MENBERS = 115;

	private static String TAG = "GroupChatSettingActi";
	private TextView tvMucNick, tvMyNick;
	private ImageView topLeft;
	private TextView topTitle, topRight;
	// 成员总数
	int m_total = 0;
	// 成员列表
	private ExpandGridView gvMenberHead;
	// 修改群名称、置顶
	private RelativeLayout re_change_groupname, rlSwitchSaveConstact;
	private RelativeLayout rl_switch_chattotop;
	private RelativeLayout rl_switch_block_groupmsg;
	private RelativeLayout re_clear;

	// 状态变化
	private ImageView iv_switch_chattotop, iv_switch_unchattotop,
			iv_switch_block_groupmsg, iv_switch_unblock_groupmsg,
			ivOpenSaveConstact, ivCloseSaveConstact;

	// 删除并退出

	private Button exitBtn;
	private String I;
	private String YOU;

	// 是否是管理员
	boolean isAdmin = false;
	List<ObtainMUCmemberIQ.Item> existMenbers;
	String longClickUsername = null;
	private CommonAdapter<Menbers> adapter;
	private ProgressDialog progressDialog;
	private boolean isInDeleteMode = false;

	List<Menbers> menbers;
	private MucChatRepo repo;

	@Override
	protected void initView(Bundle savedInstanceState) {
		setContentView(R.layout.social_groupchatsetting_activity);
		YOU = getIntent().getStringExtra(Const.YOU);
		I = Const.USER_ACCOUNT;
		existMenbers = new ArrayList<ObtainMUCmemberIQ.Item>();
		menbers = new ArrayList<Menbers>();
		repo = new MucChatRepo(mContext, mHandler);
		loadDialog = new LoadingDialog(this);
		loadDialog.setTitle("正在加载...");
		initView();

		initData();

	}

	private void initData() {
		loadDialog.show();
		new Thread(new Runnable() {
			@Override
			public void run() {
			
				repo.getMenbByDb(YOU);
				repo.getInfoFromDb(YOU);
			}
		}).start();

	}

	@Override
	protected void setListener() {

		re_change_groupname.setOnClickListener(this);
		rl_switch_chattotop.setOnClickListener(this);
		rl_switch_block_groupmsg.setOnClickListener(this);
		rlSwitchSaveConstact.setOnClickListener(this);
		re_clear.setOnClickListener(this);
		exitBtn.setOnClickListener(this);
		topLeft.setOnClickListener(this);

	}

	@Override
	protected void afterViews(Bundle savedInstanceState) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void handleMsg(Message msg) {
		switch (msg.what) {
		case INFO:
			GroupInfo item = (GroupInfo) msg.obj;
			tvMucNick.setText(item.getName());

			if (isAdmin)
				exitBtn.setText("销毁群");
			break;
		case MUC_MENBERS:
			@SuppressWarnings("unchecked")
			ArrayList<Menbers> items = (ArrayList<Menbers>) msg.obj;
			menbers.clear();
			menbers = items;
			handleMenbers();
			break;
		case KICK_MENBER:

			ToastUtil.showShortToast(mContext, "踢人成功");
			break;

		default:
			break;
		}

	}

	private void initView() {

		progressDialog = new ProgressDialog(this);
		tvMucNick = (TextView) findViewById(R.id.tv_groupname);
		tvMyNick = (TextView) findViewById(R.id.tvMyNick);
		gvMenberHead = (ExpandGridView) findViewById(R.id.gridview);

		re_change_groupname = (RelativeLayout) findViewById(R.id.re_change_groupname);
		rl_switch_chattotop = (RelativeLayout) findViewById(R.id.rl_switch_chattotop);
		rl_switch_block_groupmsg = (RelativeLayout) findViewById(R.id.rl_switch_block_groupmsg);
		re_clear = (RelativeLayout) findViewById(R.id.re_clear);
		rlSwitchSaveConstact = (RelativeLayout) findViewById(R.id.rlSaveConstact);

		ivOpenSaveConstact = (ImageView) findViewById(R.id.ivOpenSaveConstact);
		ivCloseSaveConstact = (ImageView) findViewById(R.id.ivCloseSaveConstact);
		iv_switch_chattotop = (ImageView) findViewById(R.id.iv_switch_chattotop);
		iv_switch_unchattotop = (ImageView) findViewById(R.id.iv_switch_unchattotop);
		iv_switch_block_groupmsg = (ImageView) findViewById(R.id.iv_switch_block_groupmsg);
		iv_switch_unblock_groupmsg = (ImageView) findViewById(R.id.iv_switch_unblock_groupmsg);
		exitBtn = (Button) findViewById(R.id.btn_exit_grp);

		topLeft = (ImageView) findViewById(R.id.topLeft);
		topTitle = (TextView) findViewById(R.id.topTitle);
		topRight = (TextView) findViewById(R.id.topRight);
		topTitle.setText("群信息");
		topRight.setVisibility(View.GONE);

		initAdapter();
		gvMenberHead.setAdapter(adapter);
		// 退出删除模式
		gvMenberHead.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					if (isInDeleteMode) {
						isInDeleteMode = false;
						adapter.notifyDataSetChanged();
						return true;
					}
					break;
				default:
					break;
				}
				return false;
			}
		});

	}

	private void initAdapter() {
		adapter = new CommonAdapter<Menbers>(this, menbers,
				R.layout.social_chatsetting_gridview_item) {

			@Override
			public void convert(ViewHolder helper, final Menbers item) {
				final CircleImageView ivAvatar = (CircleImageView) helper
						.getView(R.id.ivMucAvatar);
				TextView tv_username = helper.getView(R.id.tv_username);
				final ImageView delPerson = (ImageView) helper
						.getView(R.id.badge_delete);

				int position = helper.getPosition();
				View convertView = helper.getConvertView();
				if (position == getCount() - 1) {
					tv_username.setText("");

					if (isAdmin) {
						hideView(convertView);
						ivAvatar.setImageResource(R.drawable.icon_btn_deleteperson);
						ivAvatar.setOnClickListener(new OnClickListener() {
							@Override
							public void onClick(View v) {
								isInDeleteMode = true;
								notifyDataSetChanged();
							}
						});
					} else {
						ivAvatar.setVisibility(View.GONE);
					}
				} else if (position == getCount() - 2) {
					tv_username.setText("");
					hideView(convertView);
					ivAvatar.setImageResource(R.drawable.icon_btn_addperson);
					ivAvatar.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							Intent intent = new Intent(mContext,
									CreatChatRoomActivity.class);
							intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);// 设置不要刷新将要跳到的界面
							intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);// 它可以关掉所要到的界面中间的activity
							Bundle bundle = new Bundle();
							bundle.putSerializable(GROUP_MENBER_LIST,
									(Serializable) existMenbers);
							intent.putExtras(bundle);
							mContext.startActivity(intent);
						}
					});
				} else {
					if (isInDeleteMode) {
						delPerson.setVisibility(View.VISIBLE);
					} else {
						delPerson.setVisibility(View.GONE);
					}

//					ImgConfig.showHeadImg(item.getJid(), ivAvatar);
					AvatarRepo.getInstance().setAvatarRepo(item.getJid(), ivAvatar, mHandler);
					tv_username.setText(item.getNick());
					ivAvatar.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							Intent intent = new Intent(mContext,
									FriendActivity.class);
							intent.putExtra(Const.YOU,
									StringUtils.parseName(item.getJid()));
							mContext.startActivity(intent);
						}
					});

					delPerson.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {

							ObtainMUCKickIQ iq = IQOrder.getInstance().mucKick(
									item.getMuc(), item.getJid());
							if (iq != null) {
								mHandler.sendEmptyMessage(KICK_MENBER);
							}

						}
					});
				}

			}

			private void hideView(View convertView) {
				if (isInDeleteMode) {
					// 正处于删除模式下，隐藏删除按钮
					convertView.setVisibility(View.GONE);
				} else {
					convertView.setVisibility(View.VISIBLE);
				}
			}
		};
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.topLeft:
			finish();
			break;
		case R.id.rlSaveConstact:// 保存到通讯录
			if (ivOpenSaveConstact.getVisibility() == View.VISIBLE) {
				ivOpenSaveConstact.setVisibility(View.INVISIBLE);
				ivCloseSaveConstact.setVisibility(View.VISIBLE);
			} else {
				ivOpenSaveConstact.setVisibility(View.VISIBLE);
				ivCloseSaveConstact.setVisibility(View.INVISIBLE);
			}
			break;
		case R.id.rl_switch_block_groupmsg: // 屏蔽群组
			if (iv_switch_block_groupmsg.getVisibility() == View.VISIBLE) {
				iv_switch_block_groupmsg.setVisibility(View.INVISIBLE);
				iv_switch_unblock_groupmsg.setVisibility(View.VISIBLE);
				PreferencesUtils.putSharePre(Const.MSG_IS_VOICE, false);
				PreferencesUtils.putSharePre(Const.MSG_IS_VIBRATE, false);
			} else {

				iv_switch_block_groupmsg.setVisibility(View.VISIBLE);
				iv_switch_unblock_groupmsg.setVisibility(View.INVISIBLE);
				PreferencesUtils.putSharePre(Const.MSG_IS_VOICE, true);
				PreferencesUtils.putSharePre(Const.MSG_IS_VIBRATE, true);
			}
			break;

		case R.id.re_clear: // 清空聊天记录
			progressDialog.setMessage("正在清空群消息...");
			progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			progressDialog.show();
			// 按照你们要求必须有个提示，防止记录太少，删得太快，不提示

			break;
		case R.id.re_change_groupname:

			break;
		case R.id.rl_switch_chattotop:
			// 当前状态是已经置顶,点击后取消置顶
			if (iv_switch_chattotop.getVisibility() == View.VISIBLE) {
				iv_switch_chattotop.setVisibility(View.INVISIBLE);
				iv_switch_unchattotop.setVisibility(View.VISIBLE);
			} else {
				// 当前状态是未置顶点击后置顶
				iv_switch_chattotop.setVisibility(View.VISIBLE);
				iv_switch_unchattotop.setVisibility(View.INVISIBLE);
			}
			break;

		case R.id.btn_exit_grp:
			if (isAdmin) {
				ObtainMUCDestroyIQ result = IQOrder.getInstance().mucDestroy(
						XmppUtil.getFullMUC(YOU));
				if (result.toXML().contains("destroygroup")) {
					exitMUC();
				} else {
					ToastUtil.showShortToast(mContext, "销毁群失败，请稍后操作。");
				}
			} else {
				ObtainMUCExitIQ result = IQOrder.getInstance().mucExit(
						XmppUtil.getFullMUC(YOU));
				if (result.toXML().contains("quit")) {
					exitMUC();
				} else {
					ToastUtil.showShortToast(mContext, "退群失败，请稍后操作。");
				}
			}

			break;

		default:
			break;
		}
	}

	private void exitMUC() {

		finish();
		NewChatActivity.instance.finish();
		SessionDao.getInstance().deleteYou(YOU);

	}

	private void handleMenbers() {

		for (Menbers item : menbers) {
			if (item.getJid().equals(Const.USER_ACCOUNT)) {

				if (item.getAffiliation().equals(Const.Lord)) {
					isAdmin = true;// 群主
					Logger.e(TAG, "群主：" + Const.Lord);
				} else {
					isAdmin = false;
				}
				tvMyNick.setText(item.getNick());
			}

		}

		menbers.add(new Menbers());
		menbers.add(new Menbers());
		adapter.addAll(menbers);
		if (loadDialog.isShowing()) {
			loadDialog.dismiss();
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();

	}
}

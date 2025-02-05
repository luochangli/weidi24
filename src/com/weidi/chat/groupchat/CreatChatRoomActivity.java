package com.weidi.chat.groupchat;

import java.util.ArrayList;
import java.util.List;

import org.jivesoftware.smack.util.StringUtils;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.weidi.R;
import com.weidi.activity.ChatActivity;
import com.weidi.chat.GroupChatSettingActi;
import com.weidi.chat.GroupRoom;
import com.weidi.chat.IQOrder;
import com.weidi.chat.NewChatActivity;
import com.weidi.chat.repository.AvatarRepo;
import com.weidi.common.CommonAdapter;
import com.weidi.common.SortModel;
import com.weidi.common.ViewHolder;
import com.weidi.common.base.BaseActivity;
import com.weidi.common.image.ImgConfig;
import com.weidi.fragment.NewConstactFragment;
import com.weidi.provider.CreateMUCIQ;
import com.weidi.provider.ObtainMUCmemberIQ;
import com.weidi.util.Const;
import com.weidi.util.ToastUtil;
import com.weidi.util.XmppUtil;
import com.weidi.view.CircleImageView;

/**
 * @author luochangdong E-mail: 2270333671@qq.com
 * @date 创建时间：2015-7-6 下午4:29:45
 * @Description 1.0
 */
public class CreatChatRoomActivity extends BaseActivity {

	public static final String ADD_GROUP_MENBER = "action_add_group_menber";
	private static String TAG = "CreatChatRoomActivity";
	private ImageView iv_search;
	private ImageView topLeft;
	private TextView topTitle, topRight;
	private ListView listView;
	private EditText etSearch;
	/** 是否为一个新建的群组 */
	protected boolean isCreatingNewGroup;

	private CommonAdapter<SortModel> adapter;
	/** group中一开始就有的成员 */
	private List<ObtainMUCmemberIQ.Item> existMembers = new ArrayList<ObtainMUCmemberIQ.Item>();
	private List<String> existMen = new ArrayList<String>();
	// 新添加的列表
	private List<SortModel> addList = new ArrayList<SortModel>();
	/**
	 * 可滑动的显示选中用户的View 头像
	 */
	private LinearLayout menuLinerLayout;

	// 选中用户总数,右上角显示
	int total = 0;

	private ProgressDialog progressDialog;
	private String groupname;

	/**
	 * 所有成员
	 */
	private List<SortModel> alluserList;

	@SuppressWarnings("unchecked")
	@Override
	protected void initView(Bundle savedInstanceState) {
		setContentView(R.layout.activity_chatroom);
		existMembers = (List<ObtainMUCmemberIQ.Item>) getIntent()
				.getSerializableExtra(GroupChatSettingActi.GROUP_MENBER_LIST);
		if (existMembers != null) {
			for (ObtainMUCmemberIQ.Item item : existMembers) {
				existMen.add(StringUtils.parseName(item.getJid()));
			}
		}

		progressDialog = new ProgressDialog(this);

		iv_search = (ImageView) findViewById(R.id.iv_search);
		topLeft = (ImageView) findViewById(R.id.topLeft);
		topTitle = (TextView) findViewById(R.id.topTitle);
		topRight = (TextView) findViewById(R.id.topRight);
		topTitle.setText("发起群聊");
		topRight.setText("确定");
		topRight.setVisibility(View.GONE);

		listView = (ListView) findViewById(R.id.lvCreGroupChat);
		etSearch = (EditText) findViewById(R.id.et_search);
		menuLinerLayout = (LinearLayout) this
				.findViewById(R.id.linearLayoutMenu);

		alluserList = NewConstactFragment.sourceDateList;
		setLVAdapter();
		initBroadcast();
	}

	private void initBroadcast() {
		mReceiver = new BroadcastReceiver() {

			@Override
			public void onReceive(Context context, Intent intent) {

			}

		};
		mLocalBroadcastManager.registerReceiver(mReceiver, new IntentFilter(
				ADD_GROUP_MENBER));

	}

	private void setLVAdapter() {

		adapter = new CommonAdapter<SortModel>(this, alluserList,
				R.layout.item_contactlist_listview_checkbox) {

			@Override
			public void convert(ViewHolder helper, final SortModel item) {

				CircleImageView iv_avatar = (CircleImageView) helper
						.getView(R.id.iv_avatar);
				final CheckBox checkBox = (CheckBox) helper
						.getView(R.id.checkbox);

				helper.setText(
						R.id.tv_name,
						item.getName() == null ? item.getValue() : item
								.getName());
				helper.setText(R.id.header, item.getSortLetters());
				iv_avatar.setImageBitmap(item.getPhoto());
				// 群组中原来的成员一直设为选中状态
				if (existMen != null && existMen.contains(item.getValue())) {
					// checkBox.setButtonDrawable(R.drawable.btn_check);
					checkBox.setChecked(true);
				}
				checkBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {

						if (isChecked) {

							setChecked(item, checkBox);
							addList.add(item);
						} else {
							total--;
							deleteImage(item.getValue());
							addList.remove(item);
						}
						// 群组中原来的成员一直设为选中状态
						// if (existMen != null
						// && existMen.contains(item.getValue())) {
						// // checkBox.setButtonDrawable(R.drawable.btn_check);
						// checkBox.setChecked(true);
						// }
					}

					private void setChecked(final SortModel item,
							final CheckBox checkBox) {
						// 选中用户显示在滑动栏显示

						total++;
						topRight.setText("确定(" + total + ")");
						if (total > 0) {
							if (iv_search.getVisibility() == View.VISIBLE) {
								iv_search.setVisibility(View.GONE);
								topRight.setVisibility(View.VISIBLE);
							}
						}
						View view = LayoutInflater.from(mContext).inflate(
								R.layout.item_chatroom_header_item, null);
						CircleImageView friendHead = (CircleImageView) view
								.findViewById(R.id.iv_avatar);
						friendHead.setOnClickListener(new OnClickListener() {

							@SuppressLint("NewApi")
							@Override
							public void onClick(View v) {
								checkBox.setChecked(false);
							}
						});
						// 设置id，方便后面删除
						view.setTag(item.getValue());
//						ImgConfig.showHeadImg(item.getValue(), friendHead);
					    friendHead.setImageBitmap(item.getPhoto());
						menuLinerLayout.addView(view);
					}
				});

			}
		};
		listView.setAdapter(adapter);
	}

	private void deleteImage(String friend) {
		View view = (View) menuLinerLayout.findViewWithTag(friend);
		menuLinerLayout.removeView(view);
		topRight.setText("确定(" + total + ")");
		addList.remove(friend);
		if (total < 1) {
			if (iv_search.getVisibility() == View.GONE) {
				iv_search.setVisibility(View.VISIBLE);
				topRight.setVisibility(View.GONE);
			}
		}

	}

	@Override
	protected void setListener() {

		topLeft.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				CheckBox checkBox = (CheckBox) view.findViewById(R.id.checkbox);
				checkBox.toggle();

			}
		});
		topRight.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				groupname = addList.get(0).getName() + "...";
				CreateMUCIQ iq = IQOrder.getInstance().createMUCRoom(
						groupname,
						"群描述",
						Const.loginUser.getNickname() == null ? Const.loginUser
								.getUsername() : Const.loginUser.getNickname());

				if (iq == null) {
					ToastUtil.showShortLuo("创建群失败");
				} else {
					GroupRoom room = new GroupRoom(iq.getMuc(), iq.getName());
					XmppUtil.myRooms.add(room);

					for (SortModel item : addList) {
						IQOrder.getInstance().mucAddmember(
								iq.getMuc(),
								XmppUtil.getFullUsername(item.getValue()),
								item.getName() == null ? item.getValue() : item
										.getName());

					}
					Intent intent1 = new Intent(mContext, NewChatActivity.class);
					intent1.putExtra("from", StringUtils.parseName(iq.getMuc()));
					startActivity(intent1);
					finish();

				}
			}

		});

		etSearch.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				if (s.length() > 0) {
					String str_s = etSearch.getText().toString().trim();
					List<SortModel> users_temp = new ArrayList<SortModel>();
					for (SortModel user : alluserList) {
						String usernick = user.getName() == null ? user
								.getValue() : user.getName();

						if (usernick.contains(str_s)) {

							users_temp.add(user);
						}
					}
					adapter.clear();
					adapter.addAll(users_temp);

				} else {
					adapter.clear();
					adapter.addAll(alluserList);
				}

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub

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

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		mLocalBroadcastManager.unregisterReceiver(mReceiver);
	}
}

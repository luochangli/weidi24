package com.weidi.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.weidi.QApp;
import com.weidi.R;
import com.weidi.chat.repository.AvatarRepo;
import com.weidi.common.CircularImage;
import com.weidi.common.image.ImgConfig;
import com.weidi.db.NewFriendDao;
import com.weidi.util.Const;
import com.weidi.util.ToastUtil;
import com.weidi.util.XmppUtil;

/**
 * @author luochangdong E-mail: 2270333671@qq.com
 * @date 创建时间：2015-6-25 下午1:43:16
 * @Description 1.0
 */
public class SearchAdapter extends ArrayAdapter<String> {
	Context context;
	public boolean isNewFriend = false; // 添加新朋友

	public SearchAdapter(Context context) {
		super(context, 0);
		this.context = context;
	}
	Handler mHandler = new Handler();

	
	public View getView(final int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(
					R.layout.row_addfriend, null);
		}
		final String item = getItem(position);
		CircularImage imgView = (CircularImage) convertView
				.findViewById(R.id.imgView);
		TextView nameView = (TextView) convertView.findViewById(R.id.nameView);
		final Button addBtn = (Button) convertView.findViewById(R.id.addBtn);

		nameView.setText(item);
		AvatarRepo.getInstance().setAvatarRepo(item, imgView, mHandler);

		if (NewFriendDao.getInstance().isDeal(item)) {
			addBtn.setVisibility(View.GONE);
		} else {
			addBtn.setVisibility(View.VISIBLE);
			addBtn.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					XmppUtil.addUsers(QApp.getXmppConnection().getRoster(),
							XmppUtil.getFullUsername(item), item, "friend");
					XmppUtil.sendAgreeAddFriend(QApp.getXmppConnection(),
							XmppUtil.getFullUsername(item));
					ToastUtil.showShortToast(context, "发送成功");
					addBtn.setVisibility(View.GONE);
					NewFriendDao.getInstance().delFriend(item);
					QApp.mLocalBroadcastManager.sendBroadcast(new Intent(
							Const.ACTION_NEW_FRIEND_MSG));

				}
			});
		}

		if (!isNewFriend) {
			addBtn.setVisibility(View.GONE);
		}
		return convertView;
	}
}

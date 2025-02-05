package com.weidi.adapter;

import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.weidi.R;
import com.weidi.bean.Session;
import com.weidi.bean.User;
import com.weidi.chat.repository.AvatarRepo;
import com.weidi.chat.repository.RemarkRepo;
import com.weidi.common.image.ImgConfig;
import com.weidi.db.VCardDao;
import com.weidi.util.Const;
import com.weidi.util.ExpressionUtil;
import com.weidi.view.CircleImageView;

public class SessionAdapter extends BaseAdapter {
	private Context mContext;
	private List<Session> lists;
	private Handler mHandler = new Handler();

	Bitmap bm;

	public SessionAdapter(Context context, List<Session> lists) {
		this.mContext = context;
		this.lists = lists;
	}

	@Override
	public int getCount() {
		if (lists != null) {
			return lists.size();
		}
		return 0;
	}

	@Override
	public Object getItem(int position) {
		return lists.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final Holder holder;
		if (convertView == null) {
			convertView = View.inflate(mContext, R.layout.fragment_news_item,
					null);
			holder = new Holder();
			holder.iv = (CircleImageView) convertView
					.findViewById(R.id.user_head);
			holder.tv_name = (TextView) convertView
					.findViewById(R.id.user_name);
			holder.vip = (TextView) convertView.findViewById(R.id.vip);
			holder.tv_content = (TextView) convertView
					.findViewById(R.id.content);
			holder.tv_time = (TextView) convertView.findViewById(R.id.tv_time);
			holder.tv_newmsg = (TextView) convertView
					.findViewById(R.id.tv_newmsg);
			initData(position, holder);

			convertView.setTag(holder);
		} else {
			holder = (Holder) convertView.getTag();
		}

		return convertView;
	}

	private void initData(int position, final Holder holder) {
		Session session = lists.get(position);
		if (session.getType().equals(Const.MSG_TYPE_ADD_FRIEND)) {

			holder.iv.setImageResource(R.drawable.ibl);
		} else {

			AvatarRepo.getInstance().setAvatarRepo(session.getFrom(),
					holder.iv, mHandler);

		}
		RemarkRepo.setRemark(mContext, session.getFrom(), holder.tv_name);

		SpannableStringBuilder sb = ExpressionUtil.prase(mContext,
				holder.tv_content, session.getContent());// 对内容做处理
		if (session.getContent().contains("[p/_")) {
			String str = "[原创表情]";
			holder.tv_content.setText(str);
		} else {
			holder.tv_content.setText(session.getContent());
		}
		if (session.getFrom().length() == 6) {
			holder.vip.setVisibility(View.VISIBLE);
		} else {
			holder.vip.setVisibility(View.GONE);
		}

		holder.tv_time.setText(session.getTime());
		if (!TextUtils.isEmpty(session.getNotReadCount())
				&& Integer.parseInt(session.getNotReadCount()) > 0) {
			holder.tv_newmsg.setVisibility(View.VISIBLE);
			holder.tv_newmsg.setText(session.getNotReadCount());
		} else {
			holder.tv_newmsg.setVisibility(View.GONE);
			holder.tv_newmsg.setText("");
		}
	}

	class Holder {
		CircleImageView iv;
		TextView tv_name;
		TextView tv_content;
		TextView tv_time, tv_newmsg;
		TextView vip;
	}

}

package com.weidi.adapter;

import java.io.File;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.util.StringUtils;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.text.SpannableStringBuilder;
import android.text.util.Linkify;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.weidi.QApp;
import com.weidi.R;
import com.weidi.activity.ChatActivity;
import com.weidi.activity.ImgPageActivity;
import com.weidi.bean.ChatItem;
import com.weidi.bean.Msg;
import com.weidi.common.MediaFile;
import com.weidi.common.UploadUtil;
import com.weidi.common.UploadUtil.UpCallback;
import com.weidi.common.function.recoding.MediaManager;
import com.weidi.common.function.video.VideoThumbnail;
import com.weidi.common.image.ImageLoadCache;
import com.weidi.common.image.ImgConfig;
import com.weidi.db.ChatDao;
import com.weidi.util.Const;
import com.weidi.util.ExpressionUtil;
import com.weidi.util.Logger;
import com.weidi.util.ToastUtil;
import com.weidi.util.XmppUtil;
import com.weidi.view.CircleImageView;

/**
 * @author luochangdong E-mail: 2270333671@qq.com
 * @date 创建时间：2015-7-29 下午4:28:27
 * @Description 1.0
 */
public class NewChatAdapter extends BaseAdapter {

	private static String TAG = NewChatAdapter.class.getSimpleName();

	private List<ChatItem> mList;
	private LayoutInflater mInflater;
	private Context mContext;
	private ChatAdapterRepo repo;
	private static AnimationDrawable voiceAnimation = null;// 语音播放动画效果
	// 不同Item布局指定
	public static final int MESSAGE_TYPE_SENT_TXT = 0;
	public static final int MESSAGE_TYPE_RECV_TXT = 1;

	public static final int MESSAGE_TYPE_SENT_IMAGE = 2;
	public static final int MESSAGE_TYPE_RECV_IMAGE = 3;

	public static final int MESSAGE_TYPE_SENT_LOCATION = 4;
	public static final int MESSAGE_TYPE_RECV_LOCATION = 5;

	public static final int MESSAGE_TYPE_SENT_VOICE = 6;
	public static final int MESSAGE_TYPE_RECV_VOICE = 7;

	public static final int MESSAGE_TYPE_SENT_VIDEO = 8;
	public static final int MESSAGE_TYPE_RECV_VIDEO = 9;

	public static final int MESSAGE_TYPE_SENT_FILE = 10;
	public static final int MESSAGE_TYPE_RECV_FILE = 11;

	public static final int MESSAGE_TYPE_SENT_VOICE_CALL = 12;
	public static final int MESSAGE_TYPE_RECV_VOICE_CALL = 13;

	public NewChatAdapter(List<ChatItem> mMsg, Context context) {
		this.mList = mMsg;
		mContext = context;
		mInflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		repo = ChatAdapterRepo.getInstance(mHandler,context);

	}

	public void updateItem(ChatItem item) {
		mList.add(item);
		notifyDataSetChanged();
	}

	Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {

		};
	};

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return mList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public int getViewTypeCount() {
		return 14;
	}

	@Override
	public int getItemViewType(int position) {

		int type = mList.get(position).getViewTyep();
		Logger.i(TAG, "chatAdapter type:" + type);
		return type;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		ChatItem item = mList.get(position);
		int type = getItemViewType(position);
		ViewHolderRecvText recvText = null;
		ViewHolderRecvPicture recvPicture = null;
		ViewHolderRecvVideo recvVideo = null;
		ViewHolderRecvVoice recvVoice = null;

		ViewHolderSentText sentText = null;
		ViewHolderSentPicture sentPicture = null;
		ViewHolderSentVideo sentVideo = null;
		ViewHolderSentVoice sentVoice = null;

		if (convertView == null) {
			switch (type) {
			case MESSAGE_TYPE_RECV_TXT:
				recvText = new ViewHolderRecvText();
				convertView = mInflater.inflate(R.layout.row_received_message,
						null);
				recvText.content = (TextView) convertView
						.findViewById(R.id.tv_chatcontent);
				recvText.nickname = (TextView) convertView
						.findViewById(R.id.tv_userid);
				recvText.timestamp = (TextView) convertView
						.findViewById(R.id.timestamp);
				recvText.userhead = (CircleImageView) convertView
						.findViewById(R.id.iv_userhead);

				handleRecvText(recvText, item);
				convertView.setTag(recvText);

				break;
			case MESSAGE_TYPE_RECV_IMAGE:
				recvPicture = new ViewHolderRecvPicture();
				convertView = mInflater.inflate(R.layout.row_received_picture,
						null);

				recvPicture.llLoad = (LinearLayout) convertView
						.findViewById(R.id.ll_loading);
				recvPicture.nickname = (TextView) convertView
						.findViewById(R.id.tv_userid);
				recvPicture.percentage = (TextView) convertView
						.findViewById(R.id.percentage);
				recvPicture.progressBar = (ProgressBar) convertView
						.findViewById(R.id.progressBar);
				recvPicture.picture = (ImageView) convertView
						.findViewById(R.id.iv_recvPicture);
				recvPicture.timestamp = (TextView) convertView
						.findViewById(R.id.timestamp);
				recvPicture.userhead = (CircleImageView) convertView
						.findViewById(R.id.iv_userhead);

				handleRecvPicture(recvPicture, item);
				convertView.setTag(recvPicture);

				break;
			case MESSAGE_TYPE_RECV_VIDEO:
				recvVideo = new ViewHolderRecvVideo();
				convertView = mInflater.inflate(R.layout.row_received_video,
						null);

				recvVideo.btnPlay = (LinearLayout) convertView
						.findViewById(R.id.ll_click_area);
				recvVideo.lenght = (TextView) convertView
						.findViewById(R.id.chatting_length_iv);
				recvVideo.size = (TextView) convertView
						.findViewById(R.id.chatting_size_iv);
				recvVideo.timestamp = (TextView) convertView
						.findViewById(R.id.timestamp);
				recvVideo.userhead = (CircleImageView) convertView
						.findViewById(R.id.iv_userhead);
				recvVideo.nickname = (TextView) convertView
						.findViewById(R.id.tv_userid);
				recvVideo.video = (ImageView) convertView
						.findViewById(R.id.chatting_content_iv);
				recvVideo.status = (ImageView) convertView
						.findViewById(R.id.chatting_status_btn);

				handleRecvVideo(recvVideo, item);
				convertView.setTag(recvVideo);

				break;
			case MESSAGE_TYPE_RECV_VOICE:
				recvVoice = new ViewHolderRecvVoice();
				convertView = mInflater.inflate(R.layout.row_received_voice,
						null);

				recvVoice.length = (TextView) convertView
						.findViewById(R.id.tv_length);
				recvVoice.nickname = (TextView) convertView
						.findViewById(R.id.tv_userid);
				recvVoice.timestamp = (TextView) convertView
						.findViewById(R.id.timestamp);
				recvVoice.unread = (ImageView) convertView
						.findViewById(R.id.iv_unread_voice);
				recvVoice.userhead = (CircleImageView) convertView
						.findViewById(R.id.iv_userhead);
				recvVoice.btnPlayVoice = (ImageView) convertView
						.findViewById(R.id.iv_voice);

				handleRecvVoice(recvVoice, item);
				convertView.setTag(recvVoice);

				break;
			case MESSAGE_TYPE_SENT_TXT:
				sentText = new ViewHolderSentText();
				convertView = mInflater
						.inflate(R.layout.row_sent_message, null);

				sentText.content = (TextView) convertView
						.findViewById(R.id.tv_chatcontent);
				sentText.progressBar = (ProgressBar) convertView
						.findViewById(R.id.progressBar);
				sentText.timestamp = (TextView) convertView
						.findViewById(R.id.timestamp);
				sentText.userhead = (CircleImageView) convertView
						.findViewById(R.id.iv_userhead);
				sentText.status = (ImageView) convertView
						.findViewById(R.id.msg_status);

				handleSentText(item, sentText);
				convertView.setTag(sentText);

				break;
			case MESSAGE_TYPE_SENT_IMAGE:
				sentPicture = new ViewHolderSentPicture();
				convertView = mInflater
						.inflate(R.layout.row_sent_picture, null);

				sentPicture.timestamp = (TextView) convertView
						.findViewById(R.id.timestamp);
				sentPicture.percentage = (TextView) convertView
						.findViewById(R.id.percentage);
				sentPicture.userhead = (CircleImageView) convertView
						.findViewById(R.id.iv_userhead);
				sentPicture.picture = (ImageView) convertView
						.findViewById(R.id.iv_sendPicture);
				sentPicture.progressBar = (ProgressBar) convertView
						.findViewById(R.id.progressBar);
				sentPicture.llLoad = (LinearLayout) convertView
						.findViewById(R.id.ll_loading);
				sentPicture.status = (ImageView) convertView
						.findViewById(R.id.msg_status);
				handleSentPicture(sentPicture, item);
				convertView.setTag(sentPicture);

				break;
			case MESSAGE_TYPE_SENT_VIDEO:
				sentVideo = new ViewHolderSentVideo();
				convertView = mInflater.inflate(R.layout.row_sent_video, null);

				sentVideo.btnPlay = (LinearLayout) convertView
						.findViewById(R.id.ll_click_area);
				sentVideo.lenght = (TextView) convertView
						.findViewById(R.id.chatting_length_iv);
				sentVideo.llLoad = (LinearLayout) convertView
						.findViewById(R.id.ll_loading);
				sentVideo.percentage = (TextView) convertView
						.findViewById(R.id.percentage);
				sentVideo.progressBar = (ProgressBar) convertView
						.findViewById(R.id.progressBar);
				sentVideo.size = (TextView) convertView
						.findViewById(R.id.chatting_size_iv);
				sentVideo.timestamp = (TextView) convertView
						.findViewById(R.id.timestamp);
				sentVideo.userhead = (CircleImageView) convertView
						.findViewById(R.id.iv_userhead);
				sentVideo.video = (ImageView) convertView
						.findViewById(R.id.chatting_content_iv);
				sentVideo.status = (ImageView) convertView
						.findViewById(R.id.chatting_status_btn);

				handleSentVideo(sentVideo, item);
				convertView.setTag(sentVideo);

				break;
			case MESSAGE_TYPE_SENT_VOICE:
				sentVoice = new ViewHolderSentVoice();
				convertView = mInflater.inflate(R.layout.row_sent_voice, null);

				sentVoice.btnPlayVoice = (ImageView) convertView
						.findViewById(R.id.iv_voice);
				sentVoice.progressBar = (ProgressBar) convertView
						.findViewById(R.id.progressBar);
				sentVoice.timestamp = (TextView) convertView
						.findViewById(R.id.timestamp);
				sentVoice.userhead = (CircleImageView) convertView
						.findViewById(R.id.iv_userhead);
				sentVoice.length = (TextView) convertView
						.findViewById(R.id.tv_length);

				handleSentVoice(sentVoice, item);
				convertView.setTag(sentVoice);

				break;

			default:
				break;
			}
		} else {
			switch (type) {
			case MESSAGE_TYPE_RECV_TXT:
				recvText = (ViewHolderRecvText) convertView.getTag();
				handleRecvText(recvText, item);

				break;
			case MESSAGE_TYPE_RECV_IMAGE:
				recvPicture = (ViewHolderRecvPicture) convertView.getTag();
				handleRecvPicture(recvPicture, item);

				break;
			case MESSAGE_TYPE_RECV_VIDEO:
				recvVideo = (ViewHolderRecvVideo) convertView.getTag();
				handleRecvVideo(recvVideo, item);

				break;
			case MESSAGE_TYPE_RECV_VOICE:
				recvVoice = (ViewHolderRecvVoice) convertView.getTag();
				handleRecvVoice(recvVoice, item);

				break;
			case MESSAGE_TYPE_SENT_TXT:
				sentText = (ViewHolderSentText) convertView.getTag();
				handleSentText(item, sentText);

				break;
			case MESSAGE_TYPE_SENT_IMAGE:
				sentPicture = (ViewHolderSentPicture) convertView.getTag();
				handleSentPicture(sentPicture, item);

				break;
			case MESSAGE_TYPE_SENT_VIDEO:
				sentVideo = (ViewHolderSentVideo) convertView.getTag();
				handleSentVideo(sentVideo, item);

				break;
			case MESSAGE_TYPE_SENT_VOICE:
				sentVoice = (ViewHolderSentVoice) convertView.getTag();
				handleSentVoice(sentVoice, item);

				break;

			default:
				break;
			}
		}

		return convertView;
	}

	private void handleSentText(ChatItem item, ViewHolderSentText sentText) {
		repo.handleChatItem(item, sentText);
	}

	private void handleSentVoice(final ViewHolderSentVoice sentVoice,
			final ChatItem item) {
		repo.handleChatItem(item, sentVoice);
		sentVoice.btnPlayVoice.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				playVoice(sentVoice, item);
			}
		});

	}

	private void handleSentVideo(ViewHolderSentVideo sentVideo,
			final ChatItem item) {
		repo.handleChatItem(item, sentVideo);
		sentVideo.btnPlay.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				playVideo(item);
			}
		});
	}

	private void handleSentPicture(ViewHolderSentPicture sentPicture,
			final ChatItem item) {

		repo.handleChatItem(item, sentPicture);
		sentPicture.picture.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intentImg = new Intent(mContext, ImgPageActivity.class);
				intentImg.putExtra("url", Const.ExtraPath +item.getContent());
				mContext.startActivity(intentImg);
			}
		});

	}

	private void handleRecvVoice(final ViewHolderRecvVoice recvVoice,
			final ChatItem item) {
		repo.handleChatItem(item, recvVoice);
		recvVoice.btnPlayVoice.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				playVoice(recvVoice, item);
			}

		});
	}

	private void playVoice(Object object, final ChatItem item) {
		if (item.getChatType().equals(Const.MSG_TYPE_VOICE)) {// 语音
			if (MediaManager.isVoicePlay) {
				return;
			}

			if (item.getIsRecv() == ChatItem.STATUS_1) {
				final ViewHolderRecvVoice voiceRecv = (ViewHolderRecvVoice) object;
				voiceRecv.unread.setVisibility(View.GONE);
				item.setVoiceReaded(ChatItem.STATUS_1);
				ChatDao.getInstance().updateVoiceReaded(item);
				showVoiceAnim(voiceRecv.btnPlayVoice, item);
				MediaManager.playSound(item.getContent(),
						new OnCompletionListener() {

							@Override
							public void onCompletion(MediaPlayer arg0) {
								stopVoicePlay(voiceRecv.btnPlayVoice, item);
							}
						});
			} else {
				final ViewHolderSentVoice voiceSent = (ViewHolderSentVoice) object;

				showVoiceAnim(voiceSent.btnPlayVoice, item);
				MediaManager.playSound(item.getContent(),
						new OnCompletionListener() {

							@Override
							public void onCompletion(MediaPlayer arg0) {
								stopVoicePlay(voiceSent.btnPlayVoice, item);
							}
						});
			}

		}
	}

	private void showVoiceAnim(ImageView v, ChatItem msg) {

		if (msg.getIsRecv() == ChatItem.STATUS_1) {
			v.setImageResource(R.anim.voice_from_icon);
		} else {
			v.setImageResource(R.anim.voice_to_icon);
		}
		voiceAnimation = (AnimationDrawable) v.getDrawable();
		voiceAnimation.start();
	}

	public static void stopVoicePlay(ImageView v, ChatItem msg) {
		stopVoiceAnim(v, msg);
		MediaManager.release();
	}

	private static void stopVoiceAnim(ImageView v, ChatItem msg) {
		if (v == null)
			return;
		voiceAnimation.stop();
		if (msg.getIsRecv() == ChatItem.STATUS_1) {
			v.setImageResource(R.drawable.chatfrom_voice_playing);
		} else {
			v.setImageResource(R.drawable.chatto_voice_playing);
		}
	}

	private void handleRecvVideo(ViewHolderRecvVideo recvVideo,
			final ChatItem item) {

		repo.handleChatItem(item, recvVideo);
		recvVideo.btnPlay.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				playVideo(item);
			}
		});
	}

	private void playVideo(final ChatItem msg) {
		if (msg.getChatType().equals(Const.MSG_TYPE_VIDEO)) {
			if (new File(msg.getContent()).exists()) {
				Intent intent = new Intent(Intent.ACTION_VIEW);
				intent.setDataAndType(Uri.fromFile(new File(msg.getContent())),
						"video/mp4");
				mContext.startActivity(intent);
				return;
			}
		}
	}

	private void handleRecvPicture(ViewHolderRecvPicture recvPicture,
			final ChatItem item) {
		repo.handleChatItem(item, recvPicture);

		recvPicture.picture.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intentImg = new Intent(mContext, ImgPageActivity.class);
				intentImg.putExtra("url", item.getContent());
				mContext.startActivity(intentImg);
			}
		});
	}

	private void handleRecvText(ViewHolderRecvText recvText, ChatItem item) {
		repo.handleChatItem(item, recvText);
	}

	public class ViewHolderRecvText extends BaseViewHolder {
		public TextView content;
	}

	public class ViewHolderRecvPicture extends BaseViewHolder {
		public TextView percentage;
		public ImageView picture;
		public LinearLayout llLoad;
	}

	public class ViewHolderRecvVideo extends BaseViewHolder {
		public TextView size, lenght;
		public ImageView video, status;
		public LinearLayout btnPlay;
	}

	public class ViewHolderRecvVoice extends BaseViewHolder {
		public TextView length;
		public ImageView btnPlayVoice, unread;

	}

	public class ViewHolderSentText extends BaseViewHolder {
		public TextView content;

	}

	public class ViewHolderSentPicture extends BaseViewHolder {
		public TextView percentage;
		public ImageView picture;
		public LinearLayout llLoad;
	}

	public class ViewHolderSentVideo extends BaseViewHolder {
		public TextView size, lenght, percentage;
		public ImageView video, status;
		public LinearLayout btnPlay;
		public LinearLayout llLoad;
	}

	public class ViewHolderSentVoice extends BaseViewHolder {
		public ImageView btnPlayVoice;
		public TextView length;

	}

	public class BaseViewHolder {
		protected TextView timestamp, nickname;
		protected CircleImageView userhead;
		protected ProgressBar progressBar;
		protected ImageView status;

	}
}

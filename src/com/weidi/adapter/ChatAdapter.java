package com.weidi.adapter;

import java.io.File;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.net.Uri;
import android.provider.MediaStore;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.util.Linkify;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.weidi.R;
import com.weidi.activity.ImgPageActivity;
import com.weidi.bean.Msg;
import com.weidi.common.function.progress.SinkingView;
import com.weidi.common.function.recoding.MediaManager;
import com.weidi.common.function.video.VideoThumbnail;
import com.weidi.common.image.ImgConfig;
import com.weidi.util.Const;
import com.weidi.util.ExpressionUtil;
import com.weidi.util.Logger;
import com.weidi.util.PreferencesUtils;
import com.weidi.view.CircleImageView;

/**
 * from为收到的消息，to为自己的消息
 * 
 * @author baiyuliang
 * 
 */
@SuppressLint("NewApi")
public class ChatAdapter extends BaseAdapter {
	private Context mContext;
	private List<Msg> list;
	AnimationDrawable anim;
	private ImageLoader imageLoader = ImageLoader.getInstance();
	DisplayImageOptions options;
	private static String TAG = "ChatAdapter";
	private static AnimationDrawable voiceAnimation = null;// 语音播放动画效果
	private static ImageView stopVoice;
	private static int isFrom;
	public static final String STOP_VOICE_PLAY = "stop_voice_play";

	public ChatAdapter(Context mContext, List<Msg> list) {
		super();
		this.mContext = mContext;
		this.list = list;

		options = new DisplayImageOptions.Builder()
				.resetViewBeforeLoading(true).cacheInMemory(true)
				.cacheOnDisk(true)
				.imageScaleType(ImageScaleType.EXACTLY_STRETCHED)
				.considerExifParams(true)
				.displayer(new FadeInBitmapDisplayer(0)).build();

	}



	public void setList(List<Msg> list) {
		this.list = list;
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		final ViewHodler hodler;
		if (convertView == null) {
			hodler = new ViewHodler();
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.chat_lv_item, null);
			hodler.rl_chat = (RelativeLayout) convertView
					.findViewById(R.id.rl_chat);// 聊天布局

			hodler.toIcon = (CircleImageView) convertView
					.findViewById(R.id.chatto_icon);// 自己头像

			// ImgConfig.showHeadImg(Const.USER_NAME,hodler.toIcon);
			hodler.fromIcon = (CircleImageView) convertView
					.findViewById(R.id.chatfrom_icon);// 他人头像

			// 收到的消息
			hodler.fromContainer = (LinearLayout) convertView
					.findViewById(R.id.chart_from_container);
			hodler.fromText = (TextView) convertView
					.findViewById(R.id.chatfrom_content);// 文本
			hodler.fromImg = (ImageView) convertView
					.findViewById(R.id.chatfrom_img);// 图片
			hodler.fromLocation = (ImageView) convertView
					.findViewById(R.id.chatfrom_location);// 位置
			// 语音
			hodler.fromVoiceImg = (ImageView) convertView
					.findViewById(R.id.from_iv_voice);
			hodler.fromVoiceLength = (TextView) convertView
					.findViewById(R.id.tv_fromlength);
			hodler.fromUnreadImg = (ImageView) convertView
					.findViewById(R.id.iv_unread_voice);
			hodler.fromVideo = (LinearLayout) convertView
					.findViewById(R.id.from_ll_video);// 视频
			hodler.fromVideoImg = (ImageView) convertView
					.findViewById(R.id.from_chatting_content_iv);
			hodler.progress_load = (ProgressBar) convertView
					.findViewById(R.id.progress_load);// ProgressBar
			// 发送的消息
			hodler.toContainer = (RelativeLayout) convertView
					.findViewById(R.id.chart_to_container);
			hodler.toText = (TextView) convertView
					.findViewById(R.id.chatto_content);// 文本
			hodler.toImg = (ImageView) convertView
					.findViewById(R.id.chatto_img);// 图片
			hodler.toVoiceSinking = (SinkingView) convertView
					.findViewById(R.id.toSinking);

			hodler.toLocation = (ImageView) convertView
					.findViewById(R.id.chatto_location);// 位置
			// 语音
			hodler.toVoiceImg = (ImageView) convertView
					.findViewById(R.id.iv_tovoice);
			hodler.toVoiceLength = (TextView) convertView
					.findViewById(R.id.tv_tolength);// 时间
			hodler.toVideo = (LinearLayout) convertView
					.findViewById(R.id.to_ll_video);
			hodler.toVideoImg = (ImageView) convertView
					.findViewById(R.id.to_chatting_content_iv);// 视频
			hodler.toVideoSinking = (SinkingView) convertView
					.findViewById(R.id.toVideoSinking);// 视频加载进度条
			hodler.time = (TextView) convertView.findViewById(R.id.chat_time);

			convertView.setTag(hodler);
		} else {
			hodler = (ViewHodler) convertView.getTag();
		}

		final Msg msg = list.get(position);
		Logger.i(TAG, "头像" + msg.getBak4());
		if (msg.getBak4() != "" && msg.getBak4() != null) {
			hodler.fromIcon.setVisibility(View.VISIBLE);
//			ImgConfig.showHeadImg(msg.getBak4(), hodler.fromIcon);
		} else {
			hodler.fromIcon.setVisibility(View.GONE);
		}

		if (msg.getIsComing() == 0) {// 收到消息 from显示
			hodler.toContainer.setVisibility(View.GONE);// 隐藏右侧布局
			hodler.fromContainer.setVisibility(View.VISIBLE);
			hodler.time.setText(msg.getDate());

			if (msg.getType().equals(Const.MSG_TYPE_TEXT)) {// 文本类型
				hodler.fromText.setVisibility(View.VISIBLE);// 文本
				hodler.fromImg.setVisibility(View.GONE);// 图片
				hodler.fromLocation.setVisibility(View.GONE);// 位置
				hodler.progress_load.setVisibility(View.GONE);
				hodler.fromVoiceImg.setVisibility(View.GONE);// 语音
				hodler.fromVoiceLength.setVisibility(View.GONE);
				hodler.fromUnreadImg.setVisibility(View.GONE);
				hodler.fromVideo.setVisibility(View.GONE);// 视频
				SpannableStringBuilder sb = ExpressionUtil.prase(mContext,
						hodler.fromText, msg.getContent());// 对内容做处理
				hodler.fromText.setText(sb);
				Linkify.addLinks(hodler.fromText, Linkify.ALL);// 增加文本链接类型

			} else if (msg.getType().equals(Const.MSG_TYPE_IMG)) {// 图片类型
				hodler.fromText.setVisibility(View.GONE);// 文本
				hodler.fromImg.setVisibility(View.VISIBLE);// 图片
				hodler.fromLocation.setVisibility(View.GONE);// 位置
				hodler.progress_load.setVisibility(View.GONE);
				hodler.fromVoiceImg.setVisibility(View.GONE);// 语音
				hodler.fromVoiceLength.setVisibility(View.GONE);
				hodler.fromUnreadImg.setVisibility(View.GONE);
				hodler.fromVideo.setVisibility(View.GONE);// 视频
				imageLoader.displayImage(msg.getContent(), hodler.fromImg,
						options);
				// finalImageLoader.display(hodler.fromImg, msg.getContent());//
				// 加载图片
			} else if (msg.getType().equals(Const.MSG_TYPE_LOCATION)) {// 位置类型
				hodler.fromText.setVisibility(View.GONE);// 文本
				hodler.fromImg.setVisibility(View.GONE);// 图片
				hodler.fromLocation.setVisibility(View.VISIBLE);// 位置
				hodler.progress_load.setVisibility(View.GONE);
				hodler.fromVoiceImg.setVisibility(View.GONE);// 语音
				hodler.fromVoiceLength.setVisibility(View.GONE);
				hodler.fromUnreadImg.setVisibility(View.GONE);
				hodler.fromVideo.setVisibility(View.GONE);// 视频

				String lat = msg.getContent();// 经纬度
				if (TextUtils.isEmpty(lat)) {
					lat = "116.404,39.915";// 北京
				}
				getImg(hodler.fromLocation, Const.LOCATION_URL_S + lat
						+ "&markers=|" + lat + "&markerStyles=l,A,0xFF0000");// 加载网络图片
			} else if (msg.getType().equals(Const.MSG_TYPE_VOICE)) { // 语音
				hodler.fromText.setVisibility(View.GONE);// 文本
				hodler.fromImg.setVisibility(View.GONE);// 图片
				hodler.fromLocation.setVisibility(View.GONE);// 位置
				hodler.progress_load.setVisibility(View.GONE);
				hodler.fromVoiceImg.setVisibility(View.VISIBLE);// 语音

				hodler.fromUnreadImg.setVisibility(View.GONE);

				hodler.fromVoiceLength.setVisibility(View.VISIBLE);

				hodler.fromVideo.setVisibility(View.GONE);// 视频
				MediaManager.getVoiceTime(msg.getContent(),
						hodler.fromVoiceLength);

			} else if (msg.getType().equals(Const.MSG_TYPE_VIDEO)) {// 视频
				hodler.fromText.setVisibility(View.GONE);// 文本
				hodler.fromImg.setVisibility(View.GONE);// 图片
				hodler.fromLocation.setVisibility(View.GONE);// 位置
				hodler.progress_load.setVisibility(View.GONE);
				hodler.fromVoiceImg.setVisibility(View.GONE);// 语音
				hodler.fromVoiceLength.setVisibility(View.GONE);
				hodler.fromUnreadImg.setVisibility(View.GONE);
				hodler.fromVideo.setVisibility(View.VISIBLE);// 视频

				Logger.e(TAG, "接收视频的本地路径：" + msg.getContent());
				Bitmap videoImg = VideoThumbnail.getVideoThumbnail(
						msg.getContent(), 120, 120,
						MediaStore.Images.Thumbnails.MICRO_KIND);
				hodler.fromVideoImg.setImageBitmap(videoImg);
			}

		} else {// 发送消息 to显示（目前发送消息只能发送文本类型，后期将会增加其它类型）
			hodler.toContainer.setVisibility(View.VISIBLE);
			hodler.fromContainer.setVisibility(View.GONE);
			hodler.time.setText(msg.getDate());
			if (msg.getType().equals(Const.MSG_TYPE_TEXT)) {// 文本类型
				hodler.toText.setVisibility(View.VISIBLE);// 文本
				hodler.toImg.setVisibility(View.GONE);// 图片
				hodler.toLocation.setVisibility(View.GONE);// 位置
				hodler.toVoiceImg.setVisibility(View.GONE);// 语音
				hodler.toVoiceLength.setVisibility(View.GONE);
				hodler.toVideo.setVisibility(View.GONE);// 视频
				SpannableStringBuilder sb = ExpressionUtil.prase(mContext,
						hodler.toText, msg.getContent());// 对内容做处理
				hodler.toText.setText(sb);
				Linkify.addLinks(hodler.toText, Linkify.ALL);
			} else if (msg.getType().equals(Const.MSG_TYPE_IMG)) {// 图片类型
				hodler.toText.setVisibility(View.GONE);// 文本
				hodler.toImg.setVisibility(View.VISIBLE);// 图片
				hodler.toLocation.setVisibility(View.GONE);// 位置
				hodler.toVoiceImg.setVisibility(View.GONE);// 语音
				hodler.toVoiceLength.setVisibility(View.GONE);
				hodler.toVideo.setVisibility(View.GONE);// 视频

				imageLoader.displayImage(msg.getContent(), hodler.toImg,
						options);

			
				// finalImageLoader.display(hodler.toImg, msg.getContent());//
				// 加载图片
			} else if (msg.getType().equals(Const.MSG_TYPE_LOCATION)) {// 位置类型
				hodler.toText.setVisibility(View.GONE);// 文本
				hodler.toImg.setVisibility(View.GONE);// 图片
				hodler.toLocation.setVisibility(View.VISIBLE);// 位置
				hodler.toVoiceImg.setVisibility(View.GONE);// 语音
				hodler.toVoiceLength.setVisibility(View.GONE);
				hodler.toVideo.setVisibility(View.GONE);// 视频
				String lat = msg.getContent();// 经纬度
				if (TextUtils.isEmpty(lat)) {
					lat = "116.404,39.915";// 北京
				}
				getImg(hodler.toLocation, Const.LOCATION_URL_S + lat
						+ "&markers=|" + lat + "&markerStyles=l,A,0xFF0000");// 加载网络图片
			} else if (msg.getType().equals(Const.MSG_TYPE_VOICE)) {// 语音类型
				hodler.toText.setVisibility(View.GONE);// 文本
				hodler.toImg.setVisibility(View.GONE);// 图片
				hodler.toLocation.setVisibility(View.GONE);// 位置
				hodler.toVoiceImg.setVisibility(View.VISIBLE);// 语音
				hodler.toVoiceLength.setVisibility(View.VISIBLE);
				hodler.toVideo.setVisibility(View.GONE);// 视频
				MediaManager.getVoiceTime(msg.getContent(),
						hodler.toVoiceLength);

			} else if (msg.getType().equals(Const.MSG_TYPE_VIDEO)) {// 视频类型
				hodler.toText.setVisibility(View.GONE);// 文本
				hodler.toImg.setVisibility(View.GONE);// 图片
				hodler.toLocation.setVisibility(View.GONE);// 位置
				hodler.toVoiceImg.setVisibility(View.GONE);// 语音
				hodler.toVoiceLength.setVisibility(View.GONE);
				hodler.toVideo.setVisibility(View.VISIBLE);// 视频
				Bitmap videoImg = VideoThumbnail.getVideoThumbnail(
						msg.getContent(), 120, 120,
						MediaStore.Images.Thumbnails.MICRO_KIND);
				hodler.toVideoImg.setImageBitmap(videoImg);

			}
		}

		setListener(position, hodler, msg);
		return convertView;
	}

	private void setListener(final int position, final ViewHodler hodler,
			final Msg msg) {
		// 文本点击
		hodler.fromText.setOnClickListener(new onClick(position, msg));
		hodler.fromText.setOnLongClickListener(new onLongCilck(position));

		hodler.toText.setOnClickListener(new onClick(position, msg));
		hodler.toText.setOnLongClickListener(new onLongCilck(position));
		// 图片点击
		hodler.fromImg.setOnClickListener(new onClick(position, msg));
		hodler.fromImg.setOnLongClickListener(new onLongCilck(position));
		hodler.toImg.setOnClickListener(new onClick(position, msg));
		hodler.toImg.setOnLongClickListener(new onLongCilck(position));
		// 位置
		hodler.fromLocation.setOnClickListener(new onClick(position, msg));
		hodler.fromLocation.setOnLongClickListener(new onLongCilck(position));
		hodler.toLocation.setOnClickListener(new onClick(position, msg));
		hodler.toLocation.setOnLongClickListener(new onLongCilck(position));
		// 语音点击
		hodler.fromVoiceImg.setOnClickListener(new VoiceClick(msg,
				hodler.fromUnreadImg));
		hodler.toVoiceImg.setOnClickListener(new VoiceClick(msg, null));
		// 视频点击
		hodler.fromVideo.setOnClickListener(new onClick(position, msg));
		hodler.toVideo.setOnClickListener(new onClick(position, msg));
	}

	void getImg(ImageView iv, String path) {
		if (!TextUtils.isEmpty(path)) {
			imageLoader.displayImage(path, iv, options);
			// finalImageLoader.display(iv, path);
		} else {
			iv.setImageResource(R.drawable.ic_launcher);
		}
	}

	class ViewHodler {
		SinkingView toVoiceSinking, toVideoSinking;
		LinearLayout fromVideo, toVideo;
		RelativeLayout rl_chat;
		CircleImageView fromIcon, toIcon;
		ImageView fromImg, fromLocation, fromVoiceImg, fromUnreadImg,
				fromVideoImg, toImg, toLocation, toVoiceImg, toVideoImg;
		TextView fromText, toText, time, fromVoiceLength, toVoiceLength;
		LinearLayout fromContainer;
		RelativeLayout toContainer;
		ProgressBar progress_load;
	}

	/**
	 * 屏蔽listitem的所有事件
	 * */
	@Override
	public boolean areAllItemsEnabled() {
		return false;
	}

	@Override
	public boolean isEnabled(int position) {
		return false;
	}

	class VoiceClick implements OnClickListener {

		Msg msg;
		ImageView ivReadTag;

		public VoiceClick(Msg msg, ImageView ivReadTag) {
			this.msg = msg;
			this.ivReadTag = ivReadTag;
		}

		@Override
		public void onClick(final View arg0) {
			if (msg.getType().equals(Const.MSG_TYPE_VOICE)) {// 语音
				if (!MediaManager.isVoicePlay) {
					stopVoice = (ImageView) arg0;
					isFrom = msg.getIsComing();
					// if (msg.getIsComing() == 0) {
					// if (ivReadTag != null) {
					// ivReadTag.setVisibility(View.GONE);
					// msg.setBak2("1");
					// }
					// }
					showVoiceAnim(arg0);
					MediaManager.playSound(msg.getContent(),
							new OnCompletionListener() {

								@Override
								public void onCompletion(MediaPlayer mp) {
									// TODO Auto-generated method stub
									stopVoicePlay();
								}

							});

				}

			}

		}

		private void showVoiceAnim(View arg0) {
			ImageView voiceIconView = (ImageView) arg0;
			if (msg.getIsComing() == 0) {
				voiceIconView.setImageResource(R.anim.voice_from_icon);
			} else {
				voiceIconView.setImageResource(R.anim.voice_to_icon);
			}
			voiceAnimation = (AnimationDrawable) voiceIconView.getDrawable();
			voiceAnimation.start();
		}
	}

	public static void stopVoicePlay() {
		stopVoiceAnim();
		MediaManager.release();
	}

	private static void stopVoiceAnim() {
		if (stopVoice == null)
			return;
		voiceAnimation.stop();
		if (isFrom == 0) {
			stopVoice.setImageResource(R.drawable.chatfrom_voice_playing);
		} else {
			stopVoice.setImageResource(R.drawable.chatto_voice_playing);
		}
	}

	/**
	 * 点击监听
	 * 
	 * @author 白玉梁
	 * 
	 */
	class onClick implements OnClickListener {
		int position;
		Msg msg;

		public onClick(int position, Msg msg) {
			this.position = position;
			this.msg = msg;
		}

		@Override
		public void onClick(final View arg0) {
			String content = msg.getContent();

			if (msg.getType().equals(Const.MSG_TYPE_VIDEO)) {
				if (new File(msg.getContent()).exists()) {
					Intent intent = new Intent(Intent.ACTION_VIEW);
					intent.setDataAndType(
							Uri.fromFile(new File(msg.getContent())),
							"video/mp4");
					mContext.startActivity(intent);
					return;
				}
			}

			if (msg.getType().equals(Const.MSG_TYPE_IMG)) {// 图片
				Intent intentImg = new Intent(mContext, ImgPageActivity.class);
				intentImg.putExtra("url", content);
				mContext.startActivity(intentImg);
			} else if (msg.getType().equals(Const.MSG_TYPE_LOCATION)) {// 位置
				String address = PreferencesUtils
						.getSharePreStr("location_adr_detail");// 详细地址
				if (TextUtils.isEmpty(address)) {
					address = "无法获取当前位置";
				}
				Toast.makeText(mContext, address, Toast.LENGTH_LONG).show();
				String lat = PreferencesUtils.getSharePreStr("location_my");// 经纬度
				if (TextUtils.isEmpty(lat)) {
					lat = "116.404,39.915";// 北京
				}
				Intent intentMap = new Intent(mContext, ImgPageActivity.class);
				intentMap.putExtra("url", Const.LOCATION_URL_L + lat
						+ "&markers=|" + lat + "&markerStyles=l,A,0xFF0000");
				mContext.startActivity(intentMap);
			}
		}

	}

	/**
	 * 长按监听
	 * 
	 * @author 白玉梁
	 * 
	 */
	class onLongCilck implements OnLongClickListener {
		int position;

		public onLongCilck(int position) {
			this.position = position;
		}

		@Override
		public boolean onLongClick(View arg0) {
			Intent intent = new Intent(Const.ACTION_MSG_OPER);
			intent.putExtra("type", 1);
			intent.putExtra("position", position);
			mContext.sendBroadcast(intent);
			return true;
		}
	}

	class ChatReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			if (intent.getAction().equals(STOP_VOICE_PLAY)) {

			}

		}
	}

}

package com.weidi.common.image;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.jivesoftware.smackx.packet.VCard;

import android.graphics.Bitmap;
import android.view.View;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.weidi.QApp;
import com.weidi.R;
import com.weidi.util.Const;
import com.weidi.util.ImageCache;
import com.weidi.util.XmppUtil;

/**
 * @author luochangdong E-mail: 2270333671@qq.com
 * @date 创建时间：2015-6-8 下午5:19:17
 * @Description 1.0
 */
public class ImgConfig extends ImageLoader {
	private static DisplayImageOptions options_circle;
	private static AnimateFirstDisplayListener animateFirstDisplayListener = new AnimateFirstDisplayListener();
	private static Map<String, Bitmap> bMap = new HashMap<String, Bitmap>();

	/**
	 * @param url
	 *            服务器的文件名
	 * @param imageView
	 *            圆行 R for Round
	 */
	public static void showImg(String url, ImageView imageView) {
		ImageLoader.getInstance().displayImage(url, imageView, options_circle,
				animateFirstDisplayListener);
	}

	public  void showHeadImg(String username, ImageView imageView) {

		if (username == null || imageView == null) {
			return;
		}

		Bitmap bitmap = ImageCache.getInstance().get(username);

		if (bitmap == null) {
			VCard vCard = XmppUtil.getUserInfo(username);
			if (vCard != null) {
				String avatar = vCard.getField("avatar");
				if(vCard.getField("sex") != null){
					if(vCard.getField("sex").equals(Const.Male)){
						imageView.setImageDrawable(ImgHandler
								.ToCircularBig(R.drawable.icon_male));
					}else{
						imageView.setImageDrawable(ImgHandler
								.ToCircularBig(R.drawable.icon_female));
					}
				}else{
					imageView.setImageDrawable(ImgHandler
							.ToCircularBig(R.drawable.icon_male));
				}
				
				if (avatar != null) {
					bitmap = ImageUtil.getBitmapFromBase64String(avatar);
					if (bitmap != null) {
						imageView.setImageBitmap(bitmap);
						ImageCache.getInstance().put(username, bitmap);
					}
				}
			}
		} else {
			imageView.setImageBitmap(bitmap);
		}
	}

	/**
	 * 初始化图片读取方式
	 */
	public static void initImageLoader() {

		options_circle = new DisplayImageOptions.Builder()
				.showImageOnLoading(
						ImgHandler.ToCircularBig(R.drawable.default_useravatar))
				.showImageForEmptyUri(
						ImgHandler.ToCircularBig(R.drawable.default_useravatar))
				.showImageOnFail(
						ImgHandler.ToCircularBig(R.drawable.default_useravatar))
				.cacheInMemory(false).cacheOnDisk(true)
				.considerExifParams(true)
				.displayer(new RoundedBitmapDisplayer(1000))
				// 暂未研究出圆形，故使用一个大值
				.resetViewBeforeLoading(false)
				.imageScaleType(ImageScaleType.EXACTLY_STRETCHED).build();

		// This configuration tuning is custom. You can tune every option, you
		// may tune some of them,
		// or you can create default configuration by
		// ImageLoaderConfiguration.createDefault(this);
		// method.
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
				QApp.getInstance()).threadPriority(Thread.NORM_PRIORITY)
		// .denyCacheImageMultipleSizesInMemory() // 不同大小图片只有一个缓存，默认多个
				.tasksProcessingOrder(QueueProcessingType.LIFO)
				// 设置图片下载和显示的工作队列排序
				// .writeDebugLogs() // Remove for release app
				.build();
		// Initialize ImageLoader with configuration.
		ImageLoader.getInstance().init(config);
	}

	/**
	 * @author Administrator 监听读取完图片
	 */
	private static class AnimateFirstDisplayListener extends
			SimpleImageLoadingListener {
		// 放到内存
		static final List<String> displayedImages = Collections
				.synchronizedList(new LinkedList<String>());

		@Override
		public void onLoadingComplete(String imageUri, View view,
				Bitmap loadedImage) {
			if (loadedImage != null) {
				ImageView imageView = (ImageView) view;
				boolean firstDisplay = !displayedImages.contains(imageUri);
				if (firstDisplay) {
					FadeInBitmapDisplayer.animate(imageView, 500);
					displayedImages.add(imageUri);
				}
			}
		}
	}
}

package com.weidi.common.function.video;

import java.math.BigDecimal;

import android.graphics.Bitmap;
import android.media.ThumbnailUtils;

import com.weidi.util.ImageCache;

/**
 * @author luochangdong E-mail: 2270333671@qq.com
 * @date 创建时间：2015-6-16 下午8:34:16
 * @Description 1.0
 */
public class VideoThumbnail {

	  private static ImageCache imageCache;
	/**
	 * byte(字节)根据长度转成kb(千字节)和mb(兆字节)
	 * 
	 * @param bytes
	 * @return
	 */
	public static String bytes2kb(long bytes) {
		BigDecimal filesize = new BigDecimal(bytes);
		BigDecimal megabyte = new BigDecimal(1024 * 1024);
		float returnValue = filesize.divide(megabyte, 2, BigDecimal.ROUND_UP)
				.floatValue();
		if (returnValue > 1)
			return (returnValue + "MB");
		BigDecimal kilobyte = new BigDecimal(1024);
		returnValue = filesize.divide(kilobyte, 2, BigDecimal.ROUND_UP)
				.floatValue();
		return (returnValue + "KB");
	}

	/**
	 * 将毫秒转换为时 分 秒
	 * 
	 * @param mss
	 * @return
	 */
	public static String formatDuring(long mss) {
		long days = mss / (1000 * 60 * 60 * 24);
		long left = mss % (1000 * 60 * 60 * 24);
		long hours = left / (1000 * 60 * 60);
		left = left % (1000 * 60 * 60);
		long minutes = left / (1000 * 60);
		left = left % (1000 * 60);
		long seconds = left / 1000;
		left = left % 1000;
		String s = days > 0 ? (days + "") : ("");
		s += hours > 0 ? (hours + "") : (":");
		s += minutes > 0 ? (minutes + ":") : ("");
		s += seconds > 0 ? (seconds + ":") : ("");
		s += left > 0 ? (left + "") : ("");
		return s;
	}

	/**
	 * 获取视频的缩略图 先通过ThumbnailUtils来创建一个视频的缩略图，然后再利用ThumbnailUtils来生成指定大小的缩略图。
	 * 如果想要的缩略图的宽和高都小于MICRO_KIND，则类型要使用MICRO_KIND作为kind的值，这样会节省内存。
	 * 
	 * @param videoPath
	 *            视频的路径
	 * @param width
	 *            指定输出视频缩略图的宽度
	 * @param height
	 *            指定输出视频缩略图的高度度
	 * @param kind
	 *            参照MediaStore.Images.Thumbnails类中的常量MINI_KIND和MICRO_KIND。
	 *            其中，MINI_KIND: 512 x 384，MICRO_KIND: 96 x 96
	 * @return 指定大小的视频缩略图
	 */
	public static Bitmap getVideoThumbnail(String videoPath, int width,
			int height, int kind) {
		
		Bitmap bitmap = ImageCache.getInstance().get(videoPath);
		if(bitmap ==null){
			// 获取视频的缩略图
			bitmap = ThumbnailUtils.createVideoThumbnail(videoPath, kind);
			bitmap = ThumbnailUtils.extractThumbnail(bitmap, width, height,
					ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
			if(bitmap != null){
				  ImageCache.getInstance().put(videoPath, bitmap);
			}
		}
		return  bitmap;
	}


}

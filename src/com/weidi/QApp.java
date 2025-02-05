package com.weidi;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.Thread.UncaughtExceptionHandler;
import java.lang.reflect.Field;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.jivesoftware.smack.PacketCollector;
import org.jivesoftware.smack.SmackConfiguration;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.filter.AndFilter;
import org.jivesoftware.smack.filter.PacketFilter;
import org.jivesoftware.smack.filter.PacketIDFilter;
import org.jivesoftware.smack.filter.PacketTypeFilter;
import org.jivesoftware.smack.packet.IQ;

import android.app.Activity;
import android.app.Application;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Process;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.weidi.common.DateUtil;
import com.weidi.common.location.MyLocationListener;
import com.weidi.provider.BindPhone;
import com.weidi.provider.BindPhoneIQ;
import com.weidi.provider.Friend_save;
import com.weidi.provider.NearPeopleIQ;
import com.weidi.provider.NearTime;
import com.weidi.service.MsfService;
import com.weidi.util.Constants;
import com.weidi.util.Logger;
import com.weidi.util.PreferencesUtils;
import com.weidi.util.StorageUtil;
import com.weidi.util.XmppConnectionManager;

public class QApp extends Application {

	private static final String TAG = QApp.class.getSimpleName();
	public static double latitude;
	public static double longitude;
	private static QApp mInstance;
	public static SharedPreferences sharedPreferences;
	private static LocationManager locationManager;
	private static LinkedList<Activity> mActivities = new LinkedList<Activity>();
	public static LocalBroadcastManager mLocalBroadcastManager;
	public NotificationManager mNotificationManager;
	// 用于存放倒计时时间
	public static Map<String, Long> map;
	public LocationClient mLocationClient = null;
	public BDLocationListener myListener;

	@Override
	public void onCreate() {
		super.onCreate();
		initXmpp();
		sharedPreferences = getSharedPreferences(PreferencesUtils.WEIDI,
				Context.MODE_PRIVATE);
		// 初始化图片加载器相关配置
		initImageLoader(getApplicationContext());
		mInstance = this;
		mLocalBroadcastManager = LocalBroadcastManager.getInstance(this);
		// 任务栏通知
		mNotificationManager = (NotificationManager) getSystemService(android.content.Context.NOTIFICATION_SERVICE);

		myListener = new MyLocationListener();
		mLocationClient = new LocationClient(getApplicationContext()); // 声明LocationClient类
		mLocationClient.registerLocationListener(myListener); // 注册监听函数
		initLocation();
		// mLocationClient.start();

	}

	private void initXmpp() {
		new Thread(new Runnable() {

			@Override
			public void run() {
				XmppConnectionManager.getInstance().getConnection(); // 初始化XMPPConnection
			}
		}).start();
	}

	public static QApp getInstance() {
		return mInstance;
	}

	public static void initImageLoader(Context context) {
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
				context).threadPriority(Thread.NORM_PRIORITY - 2)// 设置线程的优先级
				.denyCacheImageMultipleSizesInMemory()// 当同一个Uri获取不同大小的图片，缓存到内存时，只缓存一个。默认会缓存多个不同的大小的相同图片
				.discCacheFileNameGenerator(new Md5FileNameGenerator())// 设置缓存文件的名字
				.discCacheFileCount(60)// 缓存文件的最大个数
				.tasksProcessingOrder(QueueProcessingType.LIFO)// 设置图片下载和显示的工作队列排序
				.build();
		ImageLoader.getInstance().init(config);
	}

	/**
	 * 微迪号转成手机号
	 */
	public static String initWeidiToPhone(String Weidi) {

		return null;
	}

	public static XMPPConnection getXmppConnection() {
		return XmppConnectionManager.getInstance().getConnection();
	}

	/**
	 * 绑定手机
	 * 
	 * @param phone
	 * @return
	 */
	public static BindPhone initBindPhone(String phone) {

		// 发送绑定手机IQ包
		BindPhoneIQ bindPhoneIQ = new BindPhoneIQ();
		bindPhoneIQ.setType(IQ.Type.SET);
		bindPhoneIQ.setXmlns("com:jsm:bindphone");
		bindPhoneIQ.setPhone(phone);

		// PacketCollector collector =
		// QApp.xmppConnection.createPacketCollector(new PacketFilter() {
		// @Override
		// public boolean accept(Packet p) {
		// System.out.println("accept packet xml = " + p.toXML());
		// if (p instanceof BindPhone) {
		// BindPhone m = (BindPhone) p;
		// System.out.println(p.toXML());
		// System.out.println(m.toXML());
		// }
		// return false;
		// }
		// });
		// 手机绑定过滤器
		PacketFilter filter = new AndFilter(new PacketIDFilter(
				bindPhoneIQ.getPacketID()), new PacketTypeFilter(
				BindPhone.class));
		PacketCollector collector = XmppConnectionManager.getInstance()
				.getConnection().createPacketCollector(filter);
		Log.i("TAG", bindPhoneIQ.toXML());
		getXmppConnection().sendPacket(bindPhoneIQ);
		BindPhone result = (BindPhone) collector.nextResult(SmackConfiguration
				.getPacketReplyTimeout());
		collector.cancel();
		return result;
	}

	/**
	 * 向服务器报告自己的位置
	 */
	public static void sendPosition() {
		NearPeopleIQ nearPeopleIQ = new NearPeopleIQ();
		nearPeopleIQ.setType(IQ.Type.SET);
		nearPeopleIQ.setXmlns("com:jsm:latandlon:set");
		nearPeopleIQ.setLat(QApp.latitude);
		nearPeopleIQ.setLon(QApp.longitude);
		nearPeopleIQ.setFrom("");
		nearPeopleIQ.setTo("");
		Log.i("TAG", nearPeopleIQ.toXML());
		PacketFilter filter = new AndFilter(new PacketIDFilter(
				nearPeopleIQ.getPacketID()), new PacketTypeFilter(
				NearTime.class));
		PacketCollector collector = getXmppConnection().createPacketCollector(
				filter);
		getXmppConnection().sendPacket(nearPeopleIQ);
		NearTime result = (NearTime) collector.nextResult(SmackConfiguration
				.getPacketReplyTimeout());
		collector.cancel();
		Log.i("TAG", result.toXML());
	}

	/*
	 * public static void getFriend(){
	 * 
	 * Friend_get friendIQ = new Friend_get(); friendIQ.setType(IQ.Type.GET);
	 * friendIQ.setUsername("1000261@jsmny"); Log.i("TAG", friendIQ.toXML());
	 * PacketFilter filter = new AndFilter(new
	 * PacketIDFilter(friendIQ.getPacketID()), new
	 * PacketTypeFilter(Friend_get.class)); PacketCollector collector =
	 * QApp.xmppConnection.createPacketCollector(filter);
	 * QApp.xmppConnection.sendPacket(friendIQ); Log.i("TAG", "这里是result前面");
	 * //Friend_get result = (Friend_get)
	 * collector.nextResult(SmackConfiguration.getPacketReplyTimeout()); //
	 * Log.i("TAG", "这里是result后面"); //collector.cancel(); //Log.i("TAG",
	 * result.toXML()+"这是朋友圈的"); }
	 */
	public static void saveFriendMessage() {
		Friend_save friendsavemessage = new Friend_save();
		friendsavemessage.setType(IQ.Type.SET);
		Log.i("TAG", friendsavemessage.toXML());
		getXmppConnection().sendPacket(friendsavemessage);
	}

	public static void addActivity(Activity activity) {
		mActivities.add(activity);
	}

	public static void removeActivity(Activity activity) {
		mActivities.remove(activity);
	}

	public static void exit() {
		Activity activity;
		while (mActivities.size() != 0) {
			activity = mActivities.poll();
			if (!activity.isFinishing()) {
				activity.finish();
			}
		}
	}

	/**
	 * 获取当前版本名称
	 * 
	 * @return
	 */
	public String getCurrentVersionName() {
		try {
			return getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
		} catch (Exception e) {
			// 利用系统api getPackageName()得到的包名，这个异常根本不可能发生
			return null;
		}
	}

	/**
	 * 获取当前版本号
	 * 
	 * @return
	 */
	public int getCurrentVersionCode() {
		try {
			return getPackageManager().getPackageInfo(getPackageName(), 0).versionCode;
		} catch (Exception e) {
			// 利用系统api getPackageName()得到的包名，这个异常根本不可能发生
			return 0;
		}
	}

	/**
	 * 获取安装apk文件的意图对象
	 * 
	 * @param apkFile
	 *            要安装的apk文件
	 * @return
	 */
	public static Intent getInstallApkIntent(File apkFile) {
		Intent installApkIntent = new Intent();
		installApkIntent.setAction(Intent.ACTION_VIEW);
		installApkIntent.addCategory(Intent.CATEGORY_DEFAULT);
		installApkIntent.setDataAndType(Uri.fromFile(apkFile),
				Constants.mime.APK);
		return installApkIntent;
	}

	private void initLocation() {
		LocationClientOption option = new LocationClientOption();
		option.setLocationMode(LocationMode.Hight_Accuracy);// 可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
		option.setCoorType("bd09ll");// 可选，默认gcj02，设置返回的定位结果坐标系
		int span = 30 * 60 * 1000;
		option.setScanSpan(span);//
		// 可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
		option.setIsNeedAddress(true);// 可选，设置是否需要地址信息，默认不需要
		option.setOpenGps(true);// 可选，默认false,设置是否使用gps
		option.setLocationNotify(true);// 可选，默认false，设置是否当gps有效时按照1S1次频率输出GPS结果
		option.setIsNeedLocationDescribe(true);// 可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
		option.setIsNeedLocationPoiList(true);// 可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
		option.setIgnoreKillProcess(false);// 可选，默认false，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认杀死
		option.SetIgnoreCacheException(false);// 可选，默认false，设置是否收集CRASH信息，默认收集
		option.setEnableSimulateGps(false);// 可选，默认false，设置是否需要过滤gps仿真结果，默认需要
		mLocationClient.setLocOption(option);
	}

}

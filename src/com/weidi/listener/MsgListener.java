package com.weidi.listener;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.MessageListener;
import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smack.util.StringUtils;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import android.app.KeyguardManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.HttpHandler;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.weidi.QApp;
import com.weidi.R;
import com.weidi.activity.ChatActivity;
import com.weidi.bean.Msg;
import com.weidi.bean.Session;
import com.weidi.chat.groupchat.CreatChatRoomActivity;
import com.weidi.common.DateUtil;
import com.weidi.common.UploadUtil;
import com.weidi.db.ChatMsgDao;
import com.weidi.db.NewsNotice;
import com.weidi.db.SessionDao;
import com.weidi.provider.CreateMUCIQ;
import com.weidi.provider.NewsIQ;
import com.weidi.service.MsfService;
import com.weidi.util.Const;
import com.weidi.util.DebugOut;
import com.weidi.util.FileUtil;
import com.weidi.util.Logger;
import com.weidi.util.PreferencesUtils;
import com.weidi.util.XmppUtil;

/**
 * @author luochangdong E-mail: 2270333671@qq.com
 * @date 创建时间：2015-6-15
 * @Description 1.0
 */
@SuppressWarnings("static-access")
public class MsgListener implements MessageListener, PacketListener {
	private static String TAG = "MsgListener";
	private NotificationManager mNotificationManager;
	private Notification mNotification;
	private KeyguardManager mKeyguardManager = null;

	private boolean isShowNotice = false;
	private ChatMsgDao msgDao;
	private SessionDao sessionDao;

	private String fileUrl;
	boolean done=false;

	public MsgListener() {
		DebugOut.PrintlnOut(TAG + "-");
		this.mNotificationManager = QApp.getInstance().mNotificationManager;
		mKeyguardManager = (KeyguardManager) QApp.getInstance()
				.getSystemService(QApp.getInstance().KEYGUARD_SERVICE);
		sessionDao = SessionDao.getInstance();
		msgDao = ChatMsgDao.getInstance();
	}

	@Override
	public void processMessage(Chat arg0, Message message) {
		String xml = message.toXML();
		Logger.i(TAG, "单聊" + xml);

		msgProcess(message, false);
		String isparser=getnew(xml);
		if(isparser.equals("OK")){
			Intent intent_news = new Intent(Const.NEWSNOTICE);
			QApp.getInstance().sendBroadcast(intent_news);
		}

	}

	String mucFrom;
    String isRead;
	private void msgProcess(Message message, Boolean mucChat) {
		try {

			String msgBody = message.getBody();
			String xml = message.toXML();
			final Session session;
			final String to = StringUtils.parseName(message.getTo());// 接收者,当然是自己
			final String from = StringUtils.parseName(message.getFrom());// 发送者，谁给你发的消息
			final String msgtime = DateUtil.date2Str(new Date(), "MM-dd HH:mm");// 消息时间
			
           if(ChatActivity.YOU  != null && from.equals(ChatActivity.YOU)){
        	   isRead = Const.MSG_READED;
           }else{
        	   isRead = Const.MSG_UNREAD;
           }
			
			if (xml.contains("com:jsm:group#newgroup")) {
				newGroup(message, xml);
				return;
			}

			if (mucChat) {
				mucFrom = XmppUtil.getMucFrom(message.getFrom());
			} else {
				mucFrom = null;
			}
			// 文件信息处理
			if (!xml.isEmpty() && xml != null) {

				if (xml.contains("<img>") && xml.contains("</img>")) {// 图片文件处理
					String imgName = xml.replace("</img>", "<img>").split(
							"<img>", 3)[1];
					if (!imgName.isEmpty() && imgName != null) {
						String msgtype = Const.MSG_TYPE_IMG;// 消息类型
						String fileName = imgName;// 消息内容

						session = setSession(to, from, msgtime);

						String downUrl = UploadUtil.downLoadUrl(QApp
								.getXmppConnection().getHost(), from, fileName);
						saveNoticeNoMsg(to, from, msgtype, downUrl, msgtime,
								session, "[图片]", mucFrom,isRead);
						showNotice(session.getFrom(), session.getContent());
					}

				} else if (xml.contains("<voice>") && xml.contains("</voice>")) {// 声音文件处理
					String voiceName = xml.replace("</voice>", "<voice>")
							.split("<voice>", 3)[1];
					if (!voiceName.isEmpty() && voiceName != null) {
						final String msgtype = Const.MSG_TYPE_VOICE;// 消息类型
						String fileName = voiceName;// 消息内容

						session = setSession(to, from, msgtime);

						String downUrl = UploadUtil.downLoadUrl(QApp
								.getXmppConnection().getHost(), from, fileName);
						String voicePath = FileUtil.getRecentChatPath()
								+ fileName;
						Logger.i(TAG, voicePath);
						HttpUtils http = new HttpUtils();
						HttpHandler handler = http.download(downUrl, voicePath,
								false, false, new RequestCallBack<File>() {

									@Override
									public void onSuccess(
											ResponseInfo<File> arg0) {
										fileUrl = arg0.result.getAbsolutePath();
										if (fileUrl.isEmpty()
												|| fileUrl == null)
											return;
										saveNoticeNoMsg(to, from, msgtype,
												fileUrl, msgtime, session,
												"[语音]", mucFrom,isRead);
										showNotice(session.getFrom(),
												session.getContent());
										Logger.e(TAG, "下载成功:" + fileUrl);
									}

									@Override
									public void onFailure(HttpException arg0,
											String arg1) {
										Logger.e(TAG, "下载失败:" + arg1);

									}
								});

					}

				} else if (xml.contains("<video>") && xml.contains("</video>")) {// 视频文件处理
					String videoName = xml.replace("</video>", "<video>")
							.split("<video>", 3)[1];
					if (!videoName.isEmpty() && videoName != null) {
						final String msgtype = Const.MSG_TYPE_VIDEO;// 视频类型
						String fileName = videoName;// 消息内容

						session = setSession(to, from, msgtime);

						String downUrl = UploadUtil.downLoadUrl(QApp
								.getXmppConnection().getHost(), from, fileName);
						String videoPath = FileUtil.getRecentChatPath()
								+ fileName;
						Logger.i(TAG, videoPath);
						HttpUtils http = new HttpUtils();
						HttpHandler handler = http.download(downUrl, videoPath,
								false, false, new RequestCallBack<File>() {

									@Override
									public void onSuccess(
											ResponseInfo<File> arg0) {
										fileUrl = arg0.result.getAbsolutePath();
										if (fileUrl.isEmpty()
												|| fileUrl == null)
											return;
										saveNoticeNoMsg(to, from, msgtype,
												fileUrl, msgtime, session,
												"[视频]", mucFrom,isRead);
										showNotice(session.getFrom(),
												session.getContent());
										Logger.e(TAG, "下载成功:" + fileUrl);
									}

									@Override
									public void onFailure(HttpException arg0,
											String arg1) {
										Logger.e(TAG, "下载失败:" + arg1);

									}
								});

					}

				} else if (xml.contains("<file>") && xml.contains("</file>")) { // 文件处理

				} else {
					if (msgBody == "" || msgBody == null)
						return;
					String msgtype = Const.MSG_TYPE_TEXT;// 消息类型
					String msgcontent = msgBody;// 消息内容
					session = setSession(to, from, msgtime);
					saveNoticeText(to, from, msgtype, msgcontent, msgtime,
							session, mucFrom,isRead);
					showNotice(session.getFrom(), session.getContent());
				}
			}
			Intent intent = new Intent(Const.ACTION_ADDFRIEND);// 发送广播，通知消息界面更新
			QApp.getInstance().sendBroadcast(intent);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private Session setSession(String to, String from, String msgtime) {
		Session session = new Session();
		session.setFrom(from);
		session.setTo(to);
		session.setNotReadCount("");// 未读消息数量
		session.setTime(msgtime);
		return session;
	}

	private void saveNoticeText(final String to, final String from,
			final String msgtype, String msgcontent, final String msgtime,
			final Session session, String mucFrom,String isRead) {
		Msg msg = new Msg();
		msg.setToUser(to);
		msg.setFromUser(from);
		msg.setIsComing(0);
		msg.setIsReaded(isRead);
		msg.setContent(msgcontent);
		msg.setDate(msgtime);
		msg.setBak4(mucFrom);
		msg.setType(msgtype);
		msgDao.insert(msg);
		sendNewMsg(msg);

		session.setType(Const.MSG_TYPE_TEXT);
		session.setContent(msgcontent);
		if (sessionDao.isContain(from, to)) {// 判断最近联系人列表是否已存在记录
			sessionDao.updateSession(session);
		} else {
			sessionDao.insertSession(session);
		}
	}

	private void saveNoticeNoMsg(String to, String from, String msgtype,
			String msgcontent, String msgtime, Session session,
			String sessionContent, String mucFrom,String read) {

		Msg msg = new Msg();
		msg.setToUser(to);
		msg.setFromUser(from);
		msg.setIsComing(0);
		msg.setIsReaded(read);
		msg.setContent(msgcontent);
		msg.setDate(msgtime);
		msg.setType(msgtype);
		msg.setBak4(mucFrom);
		msgDao.insert(msg);
		sendNewMsg(msg);

		session.setType(Const.MSG_TYPE_TEXT);
		session.setContent(sessionContent);
		if (sessionDao.isContain(from, to)) {
			sessionDao.updateSession(session);
		} else {
			sessionDao.insertSession(session);
		}
	}

	void sendNewMsg(Msg msg) {
		Intent intent = new Intent(Const.ACTION_NEW_MSG);// 发送广播到聊天界面
		Bundle b = new Bundle();
		b.putSerializable("msg", msg);
		intent.putExtra("msg", b);
		QApp.getInstance().sendBroadcast(intent);
	}

	@SuppressWarnings("deprecation")
	public void showNotice(String you, String content) {
		// 更新通知栏
		CharSequence tickerText = you + ":" + content;
		mNotification = new Notification(R.drawable.logo_weidi, tickerText,
				System.currentTimeMillis());
		mNotification.flags |= Notification.FLAG_AUTO_CANCEL;
		if (PreferencesUtils.getSharePreBoolean(Const.MSG_IS_VOICE)) {
			// 设置默认声音
			mNotification.defaults |= Notification.DEFAULT_SOUND;
		}
		if (PreferencesUtils.getSharePreBoolean(Const.MSG_IS_VIBRATE)) {
			// 设定震动(需加VIBRATE权限)
			mNotification.defaults |= Notification.DEFAULT_VIBRATE;
		}
		// LED灯
		mNotification.defaults |= Notification.DEFAULT_LIGHTS;
		mNotification.ledARGB = 0xff00ff00;
		mNotification.ledOnMS = 500;
		mNotification.ledOffMS = 1000;
		mNotification.flags |= Notification.FLAG_SHOW_LIGHTS;
		Intent intent = new Intent(QApp.getInstance(), ChatActivity.class);
		intent.putExtra("from", you);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
				| Intent.FLAG_ACTIVITY_NEW_TASK);
		mNotification.flags = Notification.FLAG_ONGOING_EVENT; // 设置常驻 Flag
		PendingIntent contextIntent = PendingIntent.getActivity(
				QApp.getInstance(), 0, intent, 0);
		mNotification.setLatestEventInfo(QApp.getInstance(), "新消息", tickerText,
				contextIntent);
		mNotificationManager.notify(Const.NOTIFY_ID, mNotification);// 通知

	}

	@Override
	public void processPacket(Packet packet) {
		String xml = packet.toXML();
		Message message = (Message) packet;
		Logger.i(TAG, "群聊监听" + xml);
		if (message.getFrom().contains(StringUtils.parseName(message.getTo())))
			return;
		msgProcess(message, true);
	}

	private void newGroup(Message msg, String xml) {
		try {
			String xml1 = xml.replace("<muc>", Const.SPLIT)
					.replace("</muc>", Const.SPLIT)
					.replace("<name>", Const.SPLIT)
					.replace("</name>", Const.SPLIT);
			String[] room = xml1.split(Const.SPLIT);

			Session session;
			String to = StringUtils.parseName(msg.getTo());
			String from = StringUtils.parseName(room[1]);
			String msgtime = DateUtil.date2Str(new Date(), "MM-dd HH:mm");// 消息时间
			session = setSession(to, from, msgtime);
			String content = "您已加入了群：" + from;

			saveNoticeText(to, from, Const.MSG_TYPE_TEXT, content, msgtime,
					session, "",Const.MSG_UNREAD);
			showNotice(session.getFrom(), session.getContent());

			Logger.i(TAG, "新群：" + room[1] + ":" + room[3] + ":" + to + ":"
					+ from);
			XmppUtil.joinMuc(room[1], room[3]);

			Intent intent = new Intent(Const.ACTION_ADDFRIEND);// 发送广播，通知消息界面更新
			QApp.getInstance().sendBroadcast(intent);

			Intent intent1 = new Intent(CreatChatRoomActivity.ADD_GROUP_MENBER);
			intent1.putExtra(XmppIQListener.MUC, room[1]);
			QApp.mLocalBroadcastManager.sendBroadcast(intent1);
		} catch (Exception e) {
			Logger.e(TAG, e.toString());
		}
	}
	
	
	public String getnew(String msg){
		parsernews(msg);
		return "OK";
		
	}
	
	private void parsernews(String news_string){
		try {
			NewsNotice news=NewsNotice.getInstance();
			ContentValues values = new ContentValues();
			Const.notice_data=new ArrayList<Map<String, Object>>();
			XmlPullParser parser = XmlPullParserFactory.newInstance().newPullParser();
			parser.setInput(new StringReader(news_string));
	        while (!done) {
	            int eventType = parser.next();
	            if (eventType == XmlPullParser.START_TAG) {
	                if (parser.getName().equals("id")) {
	                	String id = parser.nextText();
	                	Logger.i("NewsIQ", id);
	                   
	                  //  values.put("id", id);
	                }
	                if (parser.getName().equals("title")) {
	                	String title = parser.nextText();
	                    Logger.i("NewsIQ", title);
	                    Const.map.put("title", title);
	                    values.put("title", title);
	                }
	                if (parser.getName().equals("link")) {
	                	String link = parser.nextText();	                   
	                    Logger.i("IQ", link);
	                    Const.map.put("link", link);
	                    values.put("link", link);
	                }
	                if (parser.getName().equals("imglink")) {
	                	String imglink = parser.nextText();	                   
	                    Logger.i("IQ", imglink);
	                    Const.map.put("imglink", imglink);
	                    values.put("imglink", imglink);
	                }
	                if (parser.getName().equals("createdatetime")) {
	                	String createdatetime = parser.nextText();	                   
	                    values.put("createdatetime", createdatetime);
	                    /*createdatetime=createdatetime.substring(0,createdatetime.indexOf("."));
	                    createdatetime=createdatetime.replace("T", " ");
	                    Logger.i("IQ", createdatetime);*/
	                    Const.map.put("createdatetime", createdatetime);
	                    Const.notice_data.add(Const.map);
	                    news.insert(values);
	                    Const.newscount=Const.newscount+1;
	
	                }
	               if (parser.getName().equals("content")) {
	                	String content = parser.nextText();	                   
	                    Const.map.put("content", content);		
						values.put("content", content);					
	                }
	            }
	            else if (eventType == XmlPullParser.END_TAG) {
	                if (parser.getName().equals("message")) {
	                    done = true;
	                }
	            }
	        }
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}

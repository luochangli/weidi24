package com.weidi.listener;

import java.util.Date;

import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.Roster;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.util.StringUtils;

import android.content.Intent;
import android.util.Log;

import com.weidi.QApp;
import com.weidi.bean.Session;
import com.weidi.common.DateUtil;
import com.weidi.db.ChatMsgDao;
import com.weidi.db.NewFriendDao;
import com.weidi.db.SessionDao;
import com.weidi.fragment.NewConstactFragment;
import com.weidi.util.Const;
import com.weidi.util.Logger;
import com.weidi.util.XmppUtil;

public class FriendsPacketListener implements PacketListener {
	private static String TAG = "FriendsPacketListener";
	private XMPPConnection con;
	private SessionDao sessionDao;

	private Session setSession(String to, String from, String msgtime) {
		Session session = new Session();
		session.setFrom(from);
		session.setTo(to);
		session.setNotReadCount("");// 未读消息数量
		session.setTime(msgtime);
		return session;
	}

	@Override
	public void processPacket(Packet packet) {
		Logger.i(TAG, packet.toXML());

		if (packet instanceof Presence) {
			Presence presence = (Presence) packet;
			// Presence还有很多方法，可查看API

			final String fromJid = presence.getFrom();// 发送方
			final String toJid = presence.getTo();// 接收方

			String to = StringUtils.parseName(toJid);
			String from = StringUtils.parseName(fromJid);
			String msgtime = DateUtil.date2Str(new Date(), "MM-dd HH:mm");// 消息时间
			Roster roster = QApp.getXmppConnection().getRoster();
			if (presence.getType().equals(Presence.Type.subscribe)) {// 好友申请
																		// 本地缓存好友信息

				if (!XmppUtil.getRosterAll(roster).contains(fromJid)) {
					Logger.e(TAG, fromJid + ":from");
					NewFriendDao.getInstance().saveNewFriend(from);// 把好友消息放到新朋友里
					QApp.mLocalBroadcastManager.sendBroadcast(new Intent(
							Const.ACTION_NEW_FRIEND_MSG));

				}
				updateConstant();

			} else if (presence.getType().equals(Presence.Type.subscribed)) {// 同意添加好友，不知道为什么会自动同意
				if (XmppUtil.getRosterTo(roster).contains(fromJid)) {
					Logger.e(TAG, fromJid + ":to");
					XmppUtil.sendAgreeAddFriend(QApp.getXmppConnection(),
							fromJid);

				}
				Logger.e(TAG, "添加好友subscribed:" + fromJid);
				updateConstant();
			} else if (presence.getType().equals(Presence.Type.unsubscribe)) {// 拒绝添加好友和删除好友
				XmppUtil.removeUser(roster, fromJid);
				updateConstant();
				Log.e(TAG, "拒绝添加好友" + fromJid);

			} else if (presence.getType().equals(Presence.Type.unsubscribed)) {

				SessionDao.getInstance().deleteSessionByYou(fromJid);
				ChatMsgDao.getInstance().deleteYouMsg(fromJid);
				QApp.getInstance().sendBroadcast(
						new Intent(Const.ACTION_NEW_MSG));
				Log.e(TAG, "拒绝添加好友1");
				updateConstant();
			} else {
				Log.e(TAG, "error");
			}
		}
	}

	private void updateConstant() {
		QApp.mLocalBroadcastManager.sendBroadcast(new Intent(
				NewConstactFragment.REFRESH_CONSTACT));
	};

}

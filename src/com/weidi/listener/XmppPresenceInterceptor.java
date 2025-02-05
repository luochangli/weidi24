package com.weidi.listener;

import org.jivesoftware.smack.PacketInterceptor;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smack.packet.Presence;

import com.weidi.util.Logger;

/**
 * @author luochangdong E-mail: 2270333671@qq.com
 * @date 创建时间：2015-6-29 上午11:10:46
 * @Description 1.0 添加好友发送拦截器
 */
public class XmppPresenceInterceptor implements PacketInterceptor {

	@Override
	public void interceptPacket(Packet packet) {
		Presence presence = (Presence) packet;

		Logger.e("xmppchat send", presence.toXML());

		String from = presence.getFrom();// 发送方
		String to = presence.getTo();// 接收方
		// Presence.Type有7中状态
		if (presence.getType().equals(Presence.Type.subscribe)) {// 发出好友申请
			
	
		} else if (presence.getType().equals(Presence.Type.subscribed)) {// 同意添加好友

			Logger.e("friend", to + "我同意好友添加");

		} 

	}

}

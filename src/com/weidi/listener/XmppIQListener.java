package com.weidi.listener;

import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.packet.Packet;

import android.content.Intent;
import android.os.Bundle;

import com.weidi.QApp;
import com.weidi.chat.GroupChatSettingActi;
import com.weidi.fragment.NewConstactFragment;
import com.weidi.provider.GetAccountByPhoneIQ;
import com.weidi.provider.ObtainMUCInfoIQ;
import com.weidi.util.Logger;

/**
 * @author luochangdong E-mail: 2270333671@qq.com
 * @date 创建时间：2015-7-8 下午6:36:20
 * @Description 1.0
 */
public class XmppIQListener implements PacketListener {

	private static String TAG = "XmppIQListener";
	public static final String MUC = "muc";

	@Override
	public void processPacket(Packet packet) {
		Logger.i(TAG, packet.toXML());
		String xml = packet.toXML();
	

	
		if (xml.contains("jabber:iq:roster")
				&& xml.contains(" subscription=\"both\"")) {
			QApp.mLocalBroadcastManager.sendBroadcast(new Intent(
					NewConstactFragment.REFRESH_CONSTACT));
		}
	
	}

}

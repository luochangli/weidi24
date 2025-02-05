package com.weidi.chat;

import java.util.ArrayList;
import java.util.List;

import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smackx.muc.MultiUserChat;

import com.weidi.QApp;
import com.weidi.listener.MsgListener;
import com.weidi.util.Const;

/**
 * @author luochangdong E-mail: 2270333671@qq.com
 * @date 创建时间：2015-7-16 下午5:08:36
 * @Description 1.0
 */
public class GroupRoom {

	private String muc;
	private String name;
	private List<String> friends = new ArrayList<String>();
	private MultiUserChat multiChat;

	public GroupRoom(String muc, String name) {
		super();
		this.muc = muc;
		this.name = name;

		try {
			multiChat = new MultiUserChat(QApp.getXmppConnection(), muc);
			multiChat.addMessageListener(new MsgListener());
			multiChat.join(Const.USER_ACCOUNT);
		

		} catch (XMPPException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public MultiUserChat getMultiUserChat() {
		return multiChat;
	}

	public String getMuc() {
		return muc;
	}

	public void setMuc(String muc) {
		this.muc = muc;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<String> getFriends() {
		return friends;
	}

	public void setFriends(List<String> friends) {
		this.friends = friends;
	}

}

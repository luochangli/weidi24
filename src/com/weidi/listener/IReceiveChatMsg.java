package com.weidi.listener;

import org.jivesoftware.smack.packet.Message;

public interface IReceiveChatMsg {
	public void updateChatRoom(Message message);
}

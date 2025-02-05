package com.weidi.provider;

import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.util.StringUtils;

public class MUCChangNickIQ extends IQ {

	String muc, nickname;

	public String getMuc() {
		return muc;
	}

	public void setMuc(String muc) {
		this.muc = muc;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	@Override
	public String getChildElementXML() {
		if (this.getType() == IQ.Type.RESULT) {
			return getResult();
		} else if (this.getType() == IQ.Type.SET) {
			return sendChangNick();
		} else {
			return "";
		}
	}

	private String sendChangNick() {
		if (muc == null || nickname == null)
			return null;

		StringBuilder sb = new StringBuilder();
		sb.append("<query xmlns=\"com:jsm:group\" event=\"changenick\">");
		sb.append("<muc>").append(StringUtils.escapeForXML(muc))
				.append("</muc>");
		sb.append("<nickname>").append(StringUtils.escapeForXML(nickname))
				.append("</nickname>");
		sb.append("</query>");
		return sb.toString();
	}

	/* <query xmlns="com:jsm:group" event="changenick" /> */
	private String getResult() {
		StringBuilder sb = new StringBuilder();
		sb.append("<query xmlns=\"com:jsm:group\" event=\"changenick\">");
		sb.append("</query>");
		return sb.toString();
	}

}

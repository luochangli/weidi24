package com.weidi.provider;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.util.StringUtils;

/**
 * @author luochangdong E-mail: 2270333671@qq.com
 * @date 创建时间：2015-7-9 上午11:27:05
 * @Description 1.0 获取指定群信息
 */
public class ObtainMUCmemberIQ extends IQ implements Serializable {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1972294767024583779L;
	private String muc;
    private String errorCode;
    
    
	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	public String getMuc() {
		return muc;
	}

	public void setMuc(String muc) {
		this.muc = muc;
	}

	private List<ObtainMUCmemberIQ.Item> items = new ArrayList<ObtainMUCmemberIQ.Item>();

	public List<Item> getItems() {
		synchronized (items) {
			return items;
		}
	}

	public void addItem(Item item) {
		synchronized (items) {
			items.add(item);
		}
	}

	@Override
	public String getChildElementXML() {
		if (this.getType().equals(IQ.Type.GET)) {
			return send();
		}
		if (this.getType().equals(IQ.Type.RESULT)) {
			return getresult();
		}
		return null;
	}

	private String getresult() {
		StringBuilder sb = new StringBuilder();
		sb.append("<query xmlns=\"com:jsm:group\" event=\"getmember\">");
		sb.append("<items>");
		synchronized (items) {
			for (int i = 0; i < items.size(); i++) {
				Item item = items.get(i);
				sb.append(item.toXML());
			}
		}
		sb.append("</items>").append("</query>");
		return sb.toString();
	}

	private String send() {
		if (getMuc() == null)
			return null;
		StringBuilder sb = new StringBuilder();
		sb.append("<query xmlns=\"com:jsm:group\" event=\"getmember\">");
		sb.append("<muc>").append(StringUtils.escapeForXML(getMuc()))
				.append("</muc>");
		sb.append("</query>");
		return sb.toString();
	}

	public static class Item implements Serializable {
		/**
		 * 
		 */
		private static final long serialVersionUID = -400318560012436985L;
		private String muc;
		private String nick;
		private String jid;
		private String affiliation;

		public String getAffiliation() {
			return affiliation;
		}

		public void setAffiliation(String affiliation) {
			this.affiliation = affiliation;
		}

		public String getJid() {
			return jid;
		}

		public void setJid(String jid) {
			this.jid = jid;
		}

		public String getMuc() {
			return muc;
		}

		public void setMuc(String muc) {
			this.muc = muc;
		}

		public String getNick() {
			return nick;
		}

		public void setNick(String nick) {
			this.nick = nick;
		}

		public String toXML() {
			StringBuilder sb = new StringBuilder();

			sb.append("<item>").append("<jid>")
					.append(StringUtils.escapeForXML(getJid()))
					.append("</jid>");
			sb.append("<nick>").append(StringUtils.escapeForXML(getNick()))
					.append("</nick>");
			sb.append("<affiliation>")
					.append(StringUtils.escapeForXML(getAffiliation()))
					.append("</affiliation>").append("</item>");
			return sb.toString();

		}
	}

}

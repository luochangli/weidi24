package com.weidi.provider;

import java.util.ArrayList;
import java.util.List;

import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.util.StringUtils;

/**
 * @author luochangdong E-mail: 2270333671@qq.com
 * @date 创建时间：2015-7-9 上午10:17:02
 * @Description 1.0 获取群列表
 */
public class ObtainMUCListIQ extends IQ {

	private List<ObtainMUCListIQ.Item> items = new ArrayList<ObtainMUCListIQ.Item>();

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
		if (this.getType() == IQ.Type.GET) {
			return sendGroupList();
		}
		if (this.getType() == IQ.Type.RESULT) {
			return getGroupList();
		}
		return null;
	}

	private String getGroupList() {
		StringBuilder sb = new StringBuilder();
		sb.append("<query xmlns=\"com:jsm:group\" event=\"grouplist\">");
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

	private String sendGroupList() {
		StringBuilder sb = new StringBuilder();
		sb.append("<query xmlns=\"com:jsm:group\" event=\"grouplist\" />");
		return sb.toString();
	}

	public static class Item {
		private String muc;
		private String name;

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

		public String toXML() {
			StringBuilder sb = new StringBuilder();

			sb.append("<item>").append("<muc>").append(StringUtils.escapeForXML(getMuc()))
					.append("</muc>");
			sb.append("<name>").append(StringUtils.escapeForXML(getName())).append("</name>")
					.append("</item>");
			return sb.toString();

		}

	}
}

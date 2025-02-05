package com.weidi.provider;

import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.util.StringUtils;

/**
 * @author luochangdong E-mail: 2270333671@qq.com
 * @date 创建时间：2015-7-9 上午11:27:05
 * @Description 1.0 获取指定群信息
 */
public class ObtainMUCCreateGroupIQ extends IQ {

	private String name;
	private String description;
	private String ownernick;
	private String muc;
	
	public String getMuc() {
		return muc;
	}

	public void setMuc(String muc) {
		this.muc = muc;
	}

	public String getOwnernick() {
		return ownernick;
	}

	public void setOwnernick(String ownernick) {
		this.ownernick = ownernick;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}


	@Override
	public String getChildElementXML() {
		if (this.getType().equals(IQ.Type.SET)) {
			return send();
		}
		if (this.getType().equals(IQ.Type.RESULT)) {
			return getresult();
		}
		return null;
	}

	private String getresult() {
		StringBuilder sb = new StringBuilder();
		sb.append("<query xmlns=\"com:jsm:group\" event=\"creategroup\">");
		sb.append("<muc>").append(StringUtils.escapeForXML(getMuc())).append("</muc>");
		sb.append("</query>");
		return sb.toString();
	}

	private String send() {
		if (getOwnernick() == null||getName() == null||getDescription() == null)
			return null;
		StringBuilder sb = new StringBuilder();
		sb.append("<query xmlns=\"com:jsm:group\" event=\"creategroup\">");
		sb.append("<name>").append(StringUtils.escapeForXML(getName())).append("</name>");
		sb.append("<description>").append(StringUtils.escapeForXML(getDescription())).append("</description>");
		sb.append("<ownernick>").append(StringUtils.escapeForXML(getOwnernick())).append("</ownernick>");
		sb.append("</query>");
		return sb.toString();
	}

}

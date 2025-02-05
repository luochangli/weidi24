package com.weidi.provider;

import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.util.StringUtils;

/**
 * @author luochangdong E-mail: 2270333671@qq.com
 * @date 创建时间：2015-7-8 上午10:23:23
 * @Description 1.0
 */
public class CreateMUCIQ extends IQ {

	private String name;// 群名称
	private String description;// 群描述
	private String ownernick;// 群主昵称
	private String muc;

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

	public String getOwnernick() {
		return ownernick;
	}

	public void setOwnernick(String ownernick) {
		this.ownernick = ownernick;
	}

	public String getMuc() {
		return muc;
	}

	public void setMuc(String muc) {
		this.muc = muc;
	}

	public CreateMUCIQ() {

	}

	/**
	 * @param name
	 * @param description
	 * @param ownernick
	 */
	public CreateMUCIQ(String name, String description, String ownernick) {
		this.name = name;
		this.description = description;
		this.ownernick = ownernick;
	}

	@Override
	public String getChildElementXML() {
		if (this.getType() == IQ.Type.RESULT) {
			return getResult();
		} else if (this.getType() == IQ.Type.SET) {
			return getSet();
		} else {
			return "";
		}

	}

	private String getResult() {
		if (muc == null)
			return null;
		StringBuilder sb = new StringBuilder();
		sb.append("query xmlns=\"com:jsm:group\" event=\"getgroupinfo\">");
		sb.append("<muc>").append(muc).append("</muc>");
		sb.append("</query>");
		return sb.toString();
	}

	private String getSet() {
		if (name == null || description == null || ownernick == null)
			return null;

		StringBuilder sb = new StringBuilder();
		sb.append("<query xmlns=\"com:jsm:group\" event=\"creategroup\">");
		sb.append("<name>").append(StringUtils.escapeForXML(name))
				.append("</name>");
		sb.append("<description>")
				.append(StringUtils.escapeForXML(description))
				.append("</description>");
		sb.append("<ownernick>").append(StringUtils.escapeForXML(ownernick))
				.append("</ownernick>");
		sb.append("</query>");

		return sb.toString();
	}

}

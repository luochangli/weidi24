package com.weidi.provider;

import java.io.Serializable;

import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.util.StringUtils;

/**
 * @author luochangdong E-mail: 2270333671@qq.com
 * @date 创建时间：2015-7-9 上午11:27:05
 * @Description 1.0 获取指定群信息
 */
public class ObtainMUCInfoIQ extends IQ implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4678596846914753221L;
	private String muc;
	private String name;
	private String description;
	private String crieatdatetime;
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

	public String getCrieatdatetime() {
		return crieatdatetime;
	}

	public void setCrieatdatetime(String crieatdatetime) {
		this.crieatdatetime = crieatdatetime;
	}

	@Override
	public String getChildElementXML() {
		if (this.getType().equals(IQ.Type.GET)) {
			return send();
		}
		if (this.getType().equals(IQ.Type.RESULT)) {
			return getInfo();
		}
		if(getType().equals(IQ.Type.ERROR)){
			return getErrorCode();
		}
		return null;
	}

	private String getInfo() {
		StringBuilder sb = new StringBuilder();
		sb.append("query xmlns=\"com:jsm:group\" event=\"groupinfo\">");
		sb.append("<muc>").append(StringUtils.escapeForXML(getMuc()))
				.append("</muc>");
		sb.append("<name>").append(StringUtils.escapeForXML(getName()))
				.append("</name>");
		sb.append("<description>")
				.append(StringUtils.escapeForXML(getDescription()))
				.append("</description>");
		sb.append("<createdatetime>")
				.append(StringUtils.escapeForXML(getCrieatdatetime()))
				.append("</createdatetime>");
		sb.append("</query>");
		return sb.toString();
	}

	private String send() {
		if (getMuc() == null)
			return null;
		StringBuilder sb = new StringBuilder();
		sb.append("<query xmlns=\"com:jsm:group\" event=\"groupinfo\">");
		sb.append("<muc>").append(StringUtils.escapeForXML(getMuc()))
				.append("</muc>");
		sb.append("</query>");
		return sb.toString();
	}

}

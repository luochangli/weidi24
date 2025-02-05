package com.weidi.provider;

import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.util.StringUtils;

/**
 * @author luochangdong E-mail: 2270333671@qq.com
 * @date 创建时间：2015-7-9 上午11:27:05
 * @Description 1.0 获取指定群信息
 */
public class ForgetPasswordIQ extends IQ {

	private String phone;
	private String password;
	private String authcode;
	private String errorCode;

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getAuthcode() {
		return authcode;
	}

	public void setAuthcode(String authcode) {
		this.authcode = authcode;
	}

	@Override
	public String getChildElementXML() {
		if (this.getType().equals(IQ.Type.SET)) {
			return send();
		}
		if (this.getType().equals(IQ.Type.RESULT)) {
			return getresult();
		}
		if (getType().equals(IQ.Type.ERROR)) {
			return getErrorCode();
		}
		return null;
	}

	private String getresult() {
		StringBuilder sb = new StringBuilder();
		sb.append("<query xmlns=\"com:jsm:user\" event=\"forgetpassword\">");
		sb.append("</query>");
		return sb.toString();
	}

	private String send() {
		if (getPhone() == null || getPassword() == null
				|| getAuthcode() == null)
			return null;
		StringBuilder sb = new StringBuilder();
		sb.append("<query xmlns=\"com:jsm:user\" event=\"forgetpassword\">");
		sb.append("<phone>").append(StringUtils.escapeForXML(getPhone()))
				.append("</phone>");
		sb.append("<authcode>").append(StringUtils.escapeForXML(getAuthcode()))
				.append("</authcode>");
		sb.append("<password>").append(StringUtils.escapeForXML(getPassword()))
				.append("</password>");
		sb.append("</query>");
		return sb.toString();
	}

}

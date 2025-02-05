package com.weidi.provider;

import org.jivesoftware.smack.packet.IQ;

/**
 * @author luochangdong E-mail: 2270333671@qq.com
 * @date 创建时间：2015-7-17 下午4:40:13
 * @Description 1.0 意见反馈
 */
public class SuggestIQ extends IQ {

	private String suggest;
	private String errorCode;

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	public String getSuggest() {
		return suggest;
	}

	public void setSuggest(String suggest) {
		this.suggest = suggest;
	}

	@Override
	public String getChildElementXML() {
		if (this.getType().equals(IQ.Type.SET)) {
			return set();
		}
		if (this.getType().equals(IQ.Type.RESULT)) {
			return result();
		}
		return null;
	}

	private String result() {
		StringBuilder sb = new StringBuilder();
		sb.append("<query xmlns=\"com:jsm:suggest\" event=\"suggest\">");
		sb.append("</query>");
		return sb.toString();
	}

	private String set() {
		StringBuilder sb = new StringBuilder();
		sb.append("<query xmlns=\"com:jsm:suggest\" event= \"suggest\">");
		sb.append("<suggest>").append(getSuggest()).append("</suggest>");
		sb.append("</query>");
		return sb.toString();
	}

}

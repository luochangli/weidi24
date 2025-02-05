package com.weidi.provider;


import org.jivesoftware.smack.packet.IQ;

import android.util.Log;


public class Friend_save extends IQ{
	public static final String ELEMENT_NAME = "query";
	public static final String NAMESPACE = "com:jsm:moment";
	
	private String createdatetime;
	private String content;
	private String type;
	private String img;
	private String id;



	public String getcreatedatetime() {
		return createdatetime;
	}

	public void setcreatedatetime(String createdatetime) {
		this.createdatetime = createdatetime;
	}
	
	public String gettype1() {
		return type;
	}

	public void settype1(String type) {
		this.type = type;
	}
	
	public String getid() {
		return id;
	}

	public void setid(String id) {
		this.id = id;
	}
	
	
	public String getcontent() {
		return content;
	}

	public void setcontent(String content) {
		this.content = content;
	}

	
	public String getimg() {
		return img;
	}

	public void setimg(String img) {
		this.img = img;
	}
	
	@Override
	public String getChildElementXML() {
		// TODO Auto-generated method stub
		if (this.getType() == IQ.Type.RESULT) {
			//return getChildElementXMLForResult();
			Log.i("TAG", "已经有结果了");
			return getChildElementXMLForResult();
		}else if (this.getType() == IQ.Type.SET) {
			return getChildElementXMLForSet();
		}else{
			Log.i("TAG", "看看不会不会来着");
			return "";
		}		
	}
	private String getChildElementXMLForSet(){
		StringBuilder buf = new StringBuilder();
		buf.append("<query xmlns=\"com:jsm:moment\" event=\"save\">");
		buf.append("<").append("type").append(">")
		.append("standard")
		.append("<").append("/type").append(">")
		.append("<").append("content").append(">")
		.append("zhe ge")
		.append("<").append("/content").append(">");
		 /*buf.append("<").append("attachments").append(">")
		.append("<").append("attachment type=").append("\"img\"").append(">")
		.append(img)
		.append("<").append("/attachment").append(">")
		.append("<").append("/attachments").append(">");*/
		buf.append("</query>");
		return buf.toString(); 
	}
	private String getChildElementXMLForResult(){
		StringBuilder buf = new StringBuilder();
		buf.append("<query xmlns=\"com:jsm:moment\" event=\"save\">");
		buf.append("<").append("id>").append(id).append("<id").append("/>")
		.append("<").append("createdatetime").append(">").append(createdatetime).append("<createdatetime").append("/>");
		buf.append("<").append("query").append("/>");
		return buf.toString();
	}
}

package com.weidi.provider;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.jivesoftware.smack.packet.IQ;



public class Near extends IQ{
	public static final String ELEMENT_NAME = "query";
	public static final String NAMESPACE = "com:jsm:latandlon:query";

	private final List<NearBean> nearItems = new ArrayList<NearBean>();
	private String phone;
	private String errorcode;
	private String errortext;
	private String counttext;
	private String username;
	private double lat;
	private double lon;


	public void addNearItem(NearBean item) {
		synchronized (nearItems) {
			nearItems.add(item);
		}
	}

	public int getNearItemCount() {
		synchronized (nearItems) {
			return nearItems.size();
		}
	}

	public Collection<NearBean> getNearItems() {
		synchronized (nearItems) {
			return Collections.unmodifiableList(new ArrayList<NearBean>(nearItems));
		}
	}
	public String getUsername() {
		return username;
	}

	public String getCounttext() {
		return counttext;
	}

	public void setCounttext(String counttext) {
		this.counttext = counttext;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public double getLat() {
		return lat;
	}

	public void setLat(double lat) {
		this.lat = lat;
	}

	public double getLon() {
		return lon;
	}

	public void setLon(double lon) {
		this.lon = lon;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String value) {
		phone = value;
	}

	public String getErrorCode() {
		return errorcode;
	}
	public void setErrorCode(String value) {
		errorcode = value;
	}
	public String getErrorText() {
		return errortext;
	}

	public void setErrorText(String value) {
		errortext = value;
	}
	public String getChildElementXML() {
		if (this.getType() == IQ.Type.RESULT) {
			return getChildElementXMLForResult();
		}
		else if (this.getType() == IQ.Type.SET) {
			return getChildElementXMLForSet();
		}
		else {
			return "";
		}
	}

	public String getChildElementXMLForResult() {
		StringBuilder buf = new StringBuilder();
		buf.append("<query xmlns=\"com:jsm:latandlon:query\">");
		buf.append("<").append("items count=\"").append(counttext).append("\">");
		int t = nearItems.size();
		synchronized (nearItems) {
			for (NearBean entry : nearItems) {
				buf.append(entry.toMyXML());
			}
		}
		return buf.toString();
	}

	private String getChildElementXMLForSet() {
		StringBuilder buf = new StringBuilder();
		buf.append("<query xmlns=\"com:jsm:latandlon:query\">");
		buf.append("<").append("error code=\"").append(errorcode).append("\">")
		.append(errortext).append("</").append("error>");
		buf.append("</query>");
		return buf.toString();
	}
	public static class NearBean{
		private String username;

		private Double lat;
		private Double lon;
		
		public NearBean(String username, Double lat, Double lon) {
			super();
			this.username = username;
			this.lat = lat;
			this.lon = lon;
		}

		public String getUsername() {
			return username;
		}


		public void setUsername(String username) {
			this.username = username;
		}

		public Double getLat() {
			return lat;
		}


		public void setLat(Double lat) {
			this.lat = lat;
		}


		public Double getLon() {
			return lon;
		}

		public void setLon(Double lon) {
			this.lon = lon;
		}

		public String toMyXML() {
			StringBuilder buf = new StringBuilder();
			buf.append("<item>");
			if (username != null) {
				buf.append("<username>").append(username).append("</username>");
			}
			if (lat != null) {
				buf.append("<lat>").append(lat).append("</lat>");
			}
			if (lon != null) {
				buf.append("<lon>").append(lon).append("</lon>");
			}
			buf.append("</item>");
			return buf.toString();
		}
	}

}

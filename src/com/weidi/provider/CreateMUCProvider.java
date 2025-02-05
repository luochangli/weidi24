package com.weidi.provider;

import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.provider.IQProvider;
import org.xmlpull.v1.XmlPullParser;

import android.util.Log;

/**
 *@author  luochangdong  E-mail: 2270333671@qq.com
 *@date 创建时间：2015-7-8 下午4:41:47
 *@Description 1.0
 */
public class CreateMUCProvider implements IQProvider{

	@Override
	public IQ parseIQ(XmlPullParser parser) throws Exception {
		CreateMUCIQ createMUCIQ = new CreateMUCIQ();
		boolean done = false;
		while (!done) {
			int eventType = parser.next();
			if (eventType == XmlPullParser.START_TAG) { 
				if (parser.getName().equals("muc")) {
					String muc = parser.nextText();
				    createMUCIQ.setMuc(muc);
				}	
			}else if (eventType == XmlPullParser.END_TAG) {
				if (parser.getName().equals("query")) {
                    done = true;
                }
			}
		}
		Log.i("TSA", createMUCIQ.toXML());
		return createMUCIQ;
		
	}

}

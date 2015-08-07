package com.weidi.provider;

import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.provider.IQProvider;
import org.xmlpull.v1.XmlPullParser;

public class BingNewPhoneProvider implements IQProvider {

	@Override
	public IQ parseIQ(XmlPullParser parser) throws Exception {
		BingNewPhoneIQ bingphone=new BingNewPhoneIQ();
		
		boolean done = false;
		while (!done) {
			int eventType = parser.next();
			if (eventType==XmlPullParser.END_TAG) {
				if(parser.getName().equals("query")){
					done = true;
				}
				}
			
		}
		return bingphone;
	}

}

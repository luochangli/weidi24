package com.weidi.provider;

import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.provider.IQProvider;
import org.xmlpull.v1.XmlPullParser;

import com.weidi.util.Logger;

public class GetPassWordHintProvider implements IQProvider {

	@Override
	public IQ parseIQ(XmlPullParser parser) throws Exception {
	GetPasswordHintIQ iq=new GetPasswordHintIQ();
		boolean done = false;
		while (!done) {
			int eventType = parser.next();
			if(eventType==XmlPullParser.START_TAG){ 
				if(parser.getName().equals("passwordhint")){
					String password=parser.nextText();
					iq.setPasswordhint(password);
				}
				if (parser.getName().equals("error")) {
				    String code = parser.getAttributeValue(0);
				    Logger.e("errorHint", code);
					iq.setErrorCode(code);
				}
			}else if (eventType==XmlPullParser.END_TAG) {
				if(parser.getName().equals("query")){
					done = true;
				}
				}
			
		}
		return iq;
	}



}

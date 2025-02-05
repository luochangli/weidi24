package com.weidi.provider;

import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.provider.IQProvider;
import org.xmlpull.v1.XmlPullParser;

/**
 * @author luochangdong E-mail: 2270333671@qq.com
 * @date 创建时间：2015-7-9 上午11:41:37
 * @Description 1.0
 */
public class ObtainMUCInfoProvider implements IQProvider {

	@Override
	public IQ parseIQ(XmlPullParser parser) throws Exception {

		ObtainMUCInfoIQ provider = new ObtainMUCInfoIQ();
		boolean done = false;
		while (!done) {
			int eventType = parser.next();
			if (eventType == XmlPullParser.START_TAG) {
				if (parser.getName().equals("muc")) {
					String muc = parser.nextText();
					provider.setMuc(muc);
				}
				if (parser.getName().equals("error")) {
					provider.setErrorCode(parser.getAttributeValue(0));
				}
				if (parser.getName().equals("name")) {
					provider.setName(parser.nextText());
				}
				if (parser.getName().equals("description")) {
					provider.setDescription(parser.nextText());
				}
				if (parser.getName().equals("createdatetime")) {
					provider.setCrieatdatetime(parser.nextText());
				
				}
			} else if (eventType == XmlPullParser.END_TAG) {
				if (parser.getName().equals("query")) {
					done = true;
				}
			}
		}
		return provider;
	}

}

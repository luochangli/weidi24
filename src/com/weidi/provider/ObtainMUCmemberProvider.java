package com.weidi.provider;

import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.provider.IQProvider;
import org.xmlpull.v1.XmlPullParser;

import com.weidi.util.Logger;

/**
 * @author luochangdong E-mail: 2270333671@qq.com
 * @date 创建时间：2015-7-9 上午10:41:16
 * @Description 1.0
 */
public class ObtainMUCmemberProvider implements IQProvider {

	private static String TAG = "ObtainMUCmemberProvider";

	@Override
	public IQ parseIQ(XmlPullParser parser) throws Exception {

		ObtainMUCmemberIQ iq = new ObtainMUCmemberIQ();
		boolean done = false;
		while (!done) {
			int eventType = parser.nextTag();
			Logger.i(TAG, parser.getName());
			if (eventType == XmlPullParser.START_TAG) {
				if (parser.getName().equals("item")) {
					iq.addItem(parseItem(parser));
				}
				if (parser.getName().equals("error")) {
					iq.setErrorCode(parser.getAttributeValue(0));
				}
				if (parser.getName().equals("muc")) {
					String muc = parser.nextText();
					Logger.i("ObtainMUCGetmemberIQ", muc);
					iq.setMuc(muc);
				}
			} else if (eventType == XmlPullParser.END_TAG) {
				if (parser.getName().equals("query")) {
					done = true;
				}
			}
		}
		return iq;
	}

	private ObtainMUCmemberIQ.Item parseItem(XmlPullParser parser)
			throws Exception {
		boolean done = false;
		ObtainMUCmemberIQ.Item item = new ObtainMUCmemberIQ.Item();

		while (!done) {
			int eventType = parser.next();
			if (eventType == XmlPullParser.START_TAG) {
				if (parser.getName().equals("jid")) {
					String jid = parser.nextText();
					Logger.i("ObtainMUCGetmemberIQ", jid);
					item.setJid(jid);
				}
				if (parser.getName().equals("nick")) {
					String name = parser.nextText();
					item.setNick(name);
					Logger.i("ObtainMUCGetmemberIQ", name);
				}
				if (parser.getName().equals("affiliation")) {
					String affiliation = parser.nextText();
					item.setAffiliation(affiliation);
					Logger.i("ObtainMUCGetmemberIQ", affiliation);
				}
			} else if (eventType == XmlPullParser.END_TAG) {
				if (parser.getName().equals("item")) {
					done = true;
				}
			}
		}
		return item;
	}

}

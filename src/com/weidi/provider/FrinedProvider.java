/*package com.weidi.provider;

import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.provider.IQProvider;
import org.xmlpull.v1.XmlPullParser;

import com.weidi.R.string;
import com.weidi.provider.Near.NearBean;

import android.util.Log;

public class FrinedProvider implements IQProvider{

	@Override
	public IQ parseIQ(XmlPullParser parser) throws Exception {
		// TODO Auto-generated method stub
		Log.i("TAG", "这里是FrinedProvider");
		Friend_get friendget=new Friend_get();
		int eventType = parser.next();
		String name = parser.getName();
		while (eventType != XmlPullParser.END_DOCUMENT) {  
			if(eventType == XmlPullParser.START_DOCUMENT) {  
				System.out.println("Start document");  
			} else if(eventType == XmlPullParser.START_TAG) {  
				System.out.println("Start tag "+parser.getName());
				int mm= parser.next();
				String m=parser.getName();
				String type=parser.getAttributeValue(0);
				if(type=="standard"){
					int tag=parser.next();
					String son_name=parser.getName();
					boolean done = false;
					while(!done){
						if(son_name.equals("id")){
							parser.getText();
						}else if(son_name.equals("username")){
							parser.getText();
							
						}else if(son_name.equals("createdatetime")){
							parser.getText();
						}else if(son_name.equals("content")){
							parser.getText();
						}else if(son_name.equals("attachments")){
							boolean done2 = false;
							while(!done2){
								int gg= parser.next();
								String g=parser.getName();
								if(g.equals("attachment")){
									String attr=parser.getAttributeValue(0);								
									String img=parser.getText();					
								}
								int attachment_eventType=parser.next();
								if (attachment_eventType == XmlPullParser.END_TAG) {
										done = true;
								}							
							}
						}else if(son_name.equals("replys")){
								int gg= parser.next();
								String g=parser.getName();
								if(g.equals("reply")){
									String attr=parser.getAttributeValue(0);
									parser.next();
									String yy=parser.getName();
									if(yy.equals("username")){
										parser.getText();
									}
									parser.next();
									String yy2=parser.getName();
									if(yy.equals("datetime")){
										parser.getText();
									}
													
								}
								parser.next();
								if(g.equals("reply")){
									String attr=parser.getAttributeValue(0);
									parser.next();
									String yy=parser.getName();
									if(yy.equals("username")){
										parser.getText();
									}
									parser.next();
									String yy2=parser.getName();
									if(yy.equals("datetime")){
										parser.getText();
									}
													
								}								
								parser.next();
								if(g.equals("reply")){
									String attr=parser.getAttributeValue(0);
									parser.next();
									String yy=parser.getName();
									if(yy.equals("username")){
										parser.getText();
									}
									parser.next();
									String yy2=parser.getName();
									if(yy.equals("datetime")){
										parser.getText();
									}
													
								}
								int attachment_eventType=parser.next();
								if (attachment_eventType == XmlPullParser.END_TAG) {
										done = true;
								}							
							
						}
					}
				}
			} else
				eventType = parser.next();  
		}  
		return null;
	}


}
*/
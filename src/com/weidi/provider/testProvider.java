package com.weidi.provider;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.provider.IQProvider;
import org.xmlpull.v1.XmlPullParser;

import android.content.ContentValues;

import com.weidi.db.NewsNotice;
import com.weidi.util.Const;
import com.weidi.util.Logger;

public class testProvider implements IQProvider{
	NewsNotice news;
	ContentValues values = new ContentValues();
	@Override
	public IQ parseIQ(XmlPullParser parser) throws Exception {
		NewsIQ qq=new NewsIQ();
		boolean done = false;
		
		 news=NewsNotice.getInstance();
		
		while (!done) {
			int eventType = parser.next();
			if (eventType == XmlPullParser.START_TAG) {
				if(parser.getName().equals("item"))
				{
					
					qq.addItem(parseItem(parser));
					Const.newscount=Const.newscount+1;
				}
			}else if (eventType == XmlPullParser.END_TAG) {
				if (parser.getName().equals("query")) {
                    done = true;
                }
			}
		}
		
		return qq;
	}
	
	 private NewsIQ.Item parseItem(XmlPullParser parser) throws Exception {
	        boolean done = false;
	        NewsIQ.Item item =  new NewsIQ.Item();
	        while (!done) {
	            int eventType = parser.next();
	            if (eventType == XmlPullParser.START_TAG) {
	                if (parser.getName().equals("id")) {
	                	String id = parser.nextText();
	                	Logger.i("NewsIQ", id);
	                    item.setId(id);
	                   values.put("news_id", id);
	                }
	                if (parser.getName().equals("title")) {
	                	String title = parser.nextText();
	                    item.setTitle(title);
	                    Logger.i("NewsIQ", title);
	                    values.put("title", title);
	                }
	                if (parser.getName().equals("link")) {
	                	String link = parser.nextText();
	                    item.setLink(link);
	                    Logger.i("IQ", link);
	                    values.put("link", link);
	                }
	                if (parser.getName().equals("imglink")) {
	                	String imglink = parser.nextText();
	                    item.setImglink(imglink);
	                    Logger.i("IQ", imglink);
	                    values.put("imglink", imglink);
	                }
	                if (parser.getName().equals("createdatetime")) {
	                	String createdatetime = parser.nextText();
	                    item.setCratedatetime(createdatetime);
	                    values.put("createdatetime", createdatetime);
	                   /* createdatetime=createdatetime.substring(0,createdatetime.indexOf("."));
	                    createdatetime=createdatetime.replace("T", " ");
	                    Logger.i("IQ", createdatetime);
	                    Const.map.put("createdatetime", createdatetime);*/
	
	                }
	               if (parser.getName().equals("content")) {
	                	String content = parser.nextText();
	                    item.setContent(content);
						values.put("content", content);
						news.insert(values);
	                }
	            }
	            else if (eventType == XmlPullParser.END_TAG) {
	                if (parser.getName().equals("item")) {
	                    done = true;
	                }
	            }
	        }
	        return item;
	    }

}

package com.weidi.activity;

import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.packet.Presence.Type;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.weidi.QApp;
import com.weidi.R;
import com.weidi.chat.IQOrder;

public class TestGroupChatActivity extends Activity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_groupchat);
	}
	public void done(View view){
		switch (view.getId()) {
		case R.id.button1:
			IQOrder.getInstance().mucChangNick("g10193@conference.jsmny","bbbb"); 
		break;
		case R.id.button2:
			IQOrder.getInstance().mucChangInfo("g10193@conference.jsmny","ggg","jjj");
		break;
		case R.id.button3:
			IQOrder.getInstance().createMUCRoom("AAAA","BBBB","CCCC");
		break;
		case R.id.button4:
			IQOrder.getInstance().mucDestroy("g10193@conference.jsmny");
		break;
		case R.id.button6:
			IQOrder.getInstance().mucExit("g10193@conference.jsmny");
		break;
		case R.id.button5:
			IQOrder.getInstance().mucKick("g10193@conference.jsmny","1000264@jsmny");
		break;
		case R.id.button7:
			IQOrder.getInstance().mucAddmember("g10193@conference.jsmny","1000264@jsmny","hutailang");
		break; 
		case R.id.button8:
			IQOrder.getInstance().obtainMUCGetmember("g10436@conference.jsmny");
		break;
		case R.id.button9:
			sendPresence();
		break;
		case R.id.button10:
			IQOrder.getInstance().getRemark("1000275");
		break;
		case R.id.button11:
			IQOrder.getInstance().saveRemark("g10013@conference.jsmny","");
		break;
		case R.id.button12:
			IQOrder.getInstance().delRemark("huangziohui");
		break;
		
		case R.id.button15:
			startActivity(new Intent(TestGroupChatActivity.this,NoticeActivity.class));
		break;
		
		default:
			break;
		}
	}
	
	public void sendPresence(){
		Presence sendpresence=new Presence(Type.available);
		sendpresence.setTo("g10187@conference.jsmny/1000261");
		sendpresence.setFrom("1000261@jsmny");
		QApp.getXmppConnection().sendPacket(sendpresence);
	}
	
}

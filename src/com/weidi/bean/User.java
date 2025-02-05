package com.weidi.bean;

import org.jivesoftware.smackx.packet.VCard;

import android.graphics.Bitmap;
import android.os.Handler;
import android.widget.ImageView;

import com.weidi.chat.repository.AvatarRepo;
import com.weidi.common.image.ImageUtil;
import com.weidi.common.image.ImgConfig;
import com.weidi.util.Const;
import com.weidi.util.ImageCache;
import com.weidi.util.XmppUtil;

/**
 * @author luochangdong E-mail: 2270333671@qq.com
 * @date 创建时间：2015-6-25 下午4:56:39
 * @Description 1.0
 */
public class User {
	private String nickname;
	private String username;
	private String truename;
	private String email;
	private String headimg;
	private String intro;
	private String mobile;
	private String sex;
	private String adr;
	private VCard vCard;
	private Bitmap bitmap;
	private double lat = 0.0;
	private double lon = 0.0;
  
	public User() {
		super();
	}

	public User(VCard vCard) {
		if (vCard != null) {
			nickname = vCard.getField("nickname");
			email = vCard.getField("email");
			intro = vCard.getField("intro");
			
			sex = vCard.getField("sex");
			mobile = vCard.getField("mobile");
			adr = vCard.getField("adr");
			String weidi = vCard.getTo();
			if(weidi == null){
				username = Const.USER_ACCOUNT;
			}else{
				username = XmppUtil.getUsername(weidi);
			}
		
			String latAndlon = vCard.getField("latAndlon");
			if (latAndlon != null && !latAndlon.equals("")) {
				String[] latAndLons = latAndlon.split(",");
				lat = Double.valueOf(latAndLons[0]);
				lon = Double.valueOf(latAndLons[1]);
			}
			this.vCard = vCard;
				bitmap = ImageUtil.getBitmapFromBase64String(vCard
						.getField("avatar"));
				
				if(bitmap != null)
				ImageCache.getInstance().put(username, bitmap);
          
		}
	}

	public void showHead(ImageView imageView,Handler mHandler) {
	  AvatarRepo.getInstance().setAvatarRepo(username, imageView, mHandler);

	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getTruename() {
		return truename;
	}

	public void setTruename(String truename) {
		this.truename = truename;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getHeadimg() {
		return headimg;
	}

	public void setHeadimg(String headimg) {
		this.headimg = headimg;
	}

	public String getIntro() {
		return intro;
	}

	public void setIntro(String intro) {
		this.intro = intro;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getAdr() {
		return adr;
	}

	public void setAdr(String adr) {
		this.adr = adr;
	}

	public VCard getvCard() {
		return vCard;
	}

	public void setvCard(VCard vCard) {
		this.vCard = vCard;
	}

	public Bitmap getBitmap() {
		return bitmap;
	}

	public void setBitmap(Bitmap bitmap) {
		this.bitmap = bitmap;
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
	
	
}

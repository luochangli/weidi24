package com.weidi.common;

import android.graphics.Bitmap;

/**
 *@author  luochangdong  E-mail: 2270333671@qq.com
 *@date 创建时间：2015-6-8 下午5:19:17
 *@Description 1.0 
 */
public class SortModel {

	private Boolean vip;
	private String name; // 昵称
	private String sortLetters; // 显示数据拼音的首字母
    private String value;//微迪号
    private Bitmap photo;
    private Boolean isCheck;
    
    
    
    
    
	public Boolean getIsCheck() {
		return isCheck;
	}

	public void setIsCheck(Boolean isCheck) {
		this.isCheck = isCheck;
	}

	public Boolean getVip() {
		return vip;
	}

	public void setVip(Boolean vip) {
		this.vip = vip;
	}

	public Bitmap getPhoto() {
		return photo;
	}

	public void setPhoto(Bitmap photo) {
		this.photo = photo;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSortLetters() {
		return sortLetters;
	}

	public void setSortLetters(String sortLetters) {
		this.sortLetters = sortLetters;
	}
}

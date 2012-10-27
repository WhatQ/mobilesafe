package cn.itcast.mobilesafe.domain;


import android.graphics.drawable.Drawable;


public class AppInfo {

	private String packname;
	private Drawable icon;
	private String name;
	
	
	
	public String getPackname() {
		return packname;
	}
	public void setPackname(String packname) {
		this.packname = packname;
	}
	public Drawable getIcon() {
		return icon;
	}
	public void setIcon(Drawable icon) {
		this.icon = icon;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	
}

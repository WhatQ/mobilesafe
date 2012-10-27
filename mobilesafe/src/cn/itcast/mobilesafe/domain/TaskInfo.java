package cn.itcast.mobilesafe.domain;

import android.graphics.drawable.Drawable;

public class TaskInfo {
	private String packname;
	private String taskname;
	private Drawable taskicon;
	private int pid;
	private String memory;
	private boolean isuserapp;
	private boolean ischecked;
	
	public boolean isIsuserapp() {
		return isuserapp;
	}
	public void setIsuserapp(boolean isuserapp) {
		this.isuserapp = isuserapp;
	}
	public String getPackname() {
		return packname;
	}
	public void setPackname(String packname) {
		this.packname = packname;
	}
	public String getTaskname() {
		return taskname;
	}
	public void setTaskname(String taskname) {
		this.taskname = taskname;
	}
	public Drawable getTaskicon() {
		return taskicon;
	}
	public void setTaskicon(Drawable taskicon) {
		this.taskicon = taskicon;
	}
	public int getPid() {
		return pid;
	}
	public void setPid(int pid) {
		this.pid = pid;
	}
	public String getMemory() {
		return memory;
	}
	public void setMemory(String memory) {
		this.memory = memory;
	}
	public boolean isIschecked() {
		return ischecked;
	}
	public void setIschecked(boolean ischecked) {
		this.ischecked = ischecked;
	}
	
	
}

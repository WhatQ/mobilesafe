package cn.itcast.mobilesafe.domain;

import android.graphics.drawable.Drawable;

public class TrafficInfo {
	Drawable icon;
	String name;
	String rxstr;
	String txstr;
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
	public String getRxstr() {
		return rxstr;
	}
	public void setRxstr(String rxstr) {
		this.rxstr = rxstr;
	}
	public String getTxstr() {
		return txstr;
	}
	public void setTxstr(String txstr) {
		this.txstr = txstr;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TrafficInfo other = (TrafficInfo) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

	
}

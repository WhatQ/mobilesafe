package cn.itcast.mobilesafe.domain;
/**
 *    <time></time>
   <address></address>
   <type></type>
   <content></content>
 * @author Administrator
 *
 */
public class SmsInfo {
	private long time;
	private String address;
	private String type;
	private String content;
	public long getTime() {
		return time;
	}
	public void setTime(long time) {
		this.time = time;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	
}

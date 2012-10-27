package cn.itcast.mobilesafe.util;

import java.security.MessageDigest;

public class MD5Encoder {

	public static String encode(String pwd) throws Exception{
		
		MessageDigest digest = MessageDigest.getInstance("MD5");
		byte[] result = digest.digest(pwd.getBytes());
		StringBuffer sb = new StringBuffer();
		//把32位byte数组里面的内容转化成 16进制文本 
		for(int i=0;i<result.length;i++){
			
		  String out =  Integer.toHexString(0xff & result[i]);	
		  if(out.length()==1){
			sb.append("0");
			sb.append(out);
		  }
		  else{
				sb.append(out);
		  }
		  

		}
		 return sb.toString();
	}
}

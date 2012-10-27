package cn.itcast.mobilesafe.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;


public class BootCompleteReceiver extends BroadcastReceiver {

	// 当手机重启完毕的时候 就用这个onrevice的代码 
	@Override
	public void onReceive(Context context, Intent intent) {
	    SharedPreferences sp =	context.getSharedPreferences("config", Context.MODE_PRIVATE);
		boolean isprotecting = sp.getBoolean("isprotecting", false);

		if(isprotecting){
			String simserial = sp.getString("simserial", null);
			TelephonyManager manager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
			String realsimserial = manager.getSimSerialNumber();
			String safenumber = sp.getString("safenumber", "");
			if(simserial!=null&&realsimserial.equals(simserial)){
				//sim 没有更改什么事情也不做 
			}else{
				SmsManager smsManager = SmsManager.getDefault();
				smsManager.sendTextMessage(safenumber, null, "你的手机的sim发生了更改,这个是更改后的sim卡", null, null);
			}
		}else{
			// 没有保护什么事情都不做 
		}
		
	}

}

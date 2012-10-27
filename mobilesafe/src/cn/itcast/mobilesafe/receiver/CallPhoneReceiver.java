package cn.itcast.mobilesafe.receiver;

import cn.itcast.mobilesafe.ui.LostProtectedActivity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

public class CallPhoneReceiver extends BroadcastReceiver {
	SharedPreferences sp;
	// 在系统发出了外拨电话广播的 时候 
	@Override
	public void onReceive(Context context, Intent intent) {
		sp = context.getSharedPreferences("config", Context.MODE_PRIVATE);
		// 获取到广播里面的数据 
		 String number = getResultData();
		 if("20122012".equals(number)){
			 Intent startintent = new Intent(context,LostProtectedActivity.class);
			 startintent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		     context.startActivity(startintent);
		     //设置广播的数据的结果集为空 
		     //终止广播 
             setResultData(null);
             //终止广播
             //abortBroadcast();
             return;
		 }
		 //判断sharedprefernce里面的ipnumber是不是空字符串
		 String ipnumber = sp.getString("ipnumber", "");
		 
		 if("".equals(ipnumber)){
			 
		 }else{
			 String newnumber = ipnumber+number;
			 setResultData(newnumber);
		 }
		 
	}

}

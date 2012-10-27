package cn.itcast.mobilesafe.receiver;

import cn.itcast.mobilesafe.ui.LostProtectedActivity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

public class CallPhoneReceiver extends BroadcastReceiver {
	SharedPreferences sp;
	// ��ϵͳ�������Ⲧ�绰�㲥�� ʱ�� 
	@Override
	public void onReceive(Context context, Intent intent) {
		sp = context.getSharedPreferences("config", Context.MODE_PRIVATE);
		// ��ȡ���㲥��������� 
		 String number = getResultData();
		 if("20122012".equals(number)){
			 Intent startintent = new Intent(context,LostProtectedActivity.class);
			 startintent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		     context.startActivity(startintent);
		     //���ù㲥�����ݵĽ����Ϊ�� 
		     //��ֹ�㲥 
             setResultData(null);
             //��ֹ�㲥
             //abortBroadcast();
             return;
		 }
		 //�ж�sharedprefernce�����ipnumber�ǲ��ǿ��ַ���
		 String ipnumber = sp.getString("ipnumber", "");
		 
		 if("".equals(ipnumber)){
			 
		 }else{
			 String newnumber = ipnumber+number;
			 setResultData(newnumber);
		 }
		 
	}

}

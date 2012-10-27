package cn.itcast.mobilesafe.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;


public class BootCompleteReceiver extends BroadcastReceiver {

	// ���ֻ�������ϵ�ʱ�� �������onrevice�Ĵ��� 
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
				//sim û�и���ʲô����Ҳ���� 
			}else{
				SmsManager smsManager = SmsManager.getDefault();
				smsManager.sendTextMessage(safenumber, null, "����ֻ���sim�����˸���,����Ǹ��ĺ��sim��", null, null);
			}
		}else{
			// û�б���ʲô���鶼���� 
		}
		
	}

}

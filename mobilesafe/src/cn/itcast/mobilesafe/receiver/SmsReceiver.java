package cn.itcast.mobilesafe.receiver;

import cn.itcast.mobilesafe.db.dao.BlackNumberDao;
import cn.itcast.mobilesafe.engine.LocationProvider;
import android.app.admin.DevicePolicyManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import cn.itcast.mobilesafe.util.Logger;

/**
 * �ر��ֻ�λ����Ϣ
 * @author Administrator
 *
 */
public class SmsReceiver extends BroadcastReceiver {
	private static final String TAG = "SmsReceiver";
	SharedPreferences sp;
	BlackNumberDao dao;
	@Override
	public void onReceive(Context context, Intent intent) {
		dao = new BlackNumberDao(context);
		sp = context.getSharedPreferences("config",Context.MODE_PRIVATE);
		String safenumber = sp.getString("safenumber", "");
		//��ȡ���ŷ��͵����� ������� "#*location*#
	    Object[] pdus =	(Object[]) intent.getExtras().get("pdus");
	    for(Object pdu : pdus){
	    	
	    	
	    	SmsMessage message = SmsMessage.createFromPdu((byte[])pdu);
	    	
	    	String number = message.getOriginatingAddress();
	    	if(dao.find(number)){
	    		abortBroadcast();
	    		return;
	    	}
	    	
	    	String content = message.getMessageBody();
	    	// ��Ʊ �ڳ� ¥�� 
	    	if("#*location*#".equals(content)){
	    		String location = LocationProvider.getInstance(context).getLocation();
	    		Logger.i(TAG,"weizhi wei "+ location);
	    		if("".equals( LocationProvider.getInstance(context).getLocation())){
	    			
	    		}else{
	    			SmsManager smsManager = SmsManager.getDefault();
					smsManager.sendTextMessage(safenumber, null, LocationProvider.getInstance(context).getLocation(), null, null);
					
	    		}
	    		abortBroadcast();
	    	}else if("#*wipedata*#".equals(content)){
	    		//����ֻ����� �ָ�����ģʽ
	    		DevicePolicyManager  manager = (DevicePolicyManager) context.getSystemService(Context.DEVICE_POLICY_SERVICE);
	    		manager.wipeData(0);
	    	}else if("#*lockscreen*#".equals(content)){
	    		//�����ֻ��Ĳ��� 
	    		DevicePolicyManager  manager = (DevicePolicyManager) context.getSystemService(Context.DEVICE_POLICY_SERVICE);
	    		manager.resetPassword("123", 0);
	    		manager.lockNow();
	    	}
	    }
	}
}

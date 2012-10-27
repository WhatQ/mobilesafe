package cn.itcast.mobilesafe.service;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URI;

import com.android.internal.telephony.ITelephony;

import cn.itcast.mobilesafe.R;
import cn.itcast.mobilesafe.db.dao.BlackNumberDao;
import cn.itcast.mobilesafe.engine.AddressService;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.ContentObserver;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.os.RemoteException;
import android.provider.CallLog;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import cn.itcast.mobilesafe.util.Logger;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.TextView;

/**
 * 继承了系统的四大组件之一service 长期在后台执行的任务
 * 
 * @author Administrator
 * 
 */
public class QueryAddressService extends Service {
	public static final String TAG = "QueryAddressService";
	WindowManager wm;
	// 手机电话相关的服务
	TelephonyManager manager;
	View view;
	LayoutInflater inflater;
    MyPhoneListener listener;
    SharedPreferences sp;
    BlackNumberDao dao;
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		listener = new MyPhoneListener();
		manager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
		manager.listen(listener,
				PhoneStateListener.LISTEN_CALL_STATE);
		wm = (WindowManager) getSystemService(WINDOW_SERVICE);
		inflater = LayoutInflater.from(this);
		sp = getSharedPreferences("config", Context.MODE_PRIVATE);
		dao = new BlackNumberDao(this);
	}

	private class MyPhoneListener extends PhoneStateListener {

		// 当电话状态发生改变的时候 调用的方法
		// state 电话的状态: 闲置 ,铃响状态, 通话状态
		// incomingnumber 来电号码
		@Override
		public void onCallStateChanged(int state, String incomingNumber) {
			super.onCallStateChanged(state, incomingNumber);
			switch (state) {
			case TelephonyManager.CALL_STATE_IDLE:
				if (view != null) {
					wm.removeView(view);
					view = null;
				}
				break;
			case TelephonyManager.CALL_STATE_RINGING:
				if(dao.find(incomingNumber)){
					//黑名单号码 
					//挂断电话的操作 
					endCall();
					// 把通话记录从列表中删除 
					
					//通过内容观察者 观察这个数据信息 
					// 多出来了这个数据信息 
					getContentResolver().registerContentObserver(CallLog.Calls.CONTENT_URI, true, new MyObserver(new Handler(), incomingNumber));
					
				
				}
				
				
				String address = AddressService.getAddress(incomingNumber);
				Logger.i(TAG, "incomingnumber address" + address);

				view = inflater.inflate(R.layout.show_location, null);
				TextView tv_name = (TextView) view.findViewById(R.id.tv_show_location_name);
				TextView tv_address = (TextView) view.findViewById(R.id.tv_show_location_address);
				tv_address.setText(address);
				//去联系人里面查询名字 
				String name = queryNameByNumber(incomingNumber);
				tv_name.setText(name);
				// defined that sets up the layout params appropriately.
				WindowManager.LayoutParams params = new LayoutParams();
				
				params.height =  50;
				params.width = 120;
				int lastx = sp.getInt("lastx", 0);
				int lasty = sp.getInt("lasty",0);
				// 需要得到坐标的参考系
				params.gravity = Gravity.TOP | Gravity.LEFT;
				params.x = lastx;
				params.y = lasty;
				params.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
						| WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
						| WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON;
				params.format = PixelFormat.TRANSLUCENT;
				params.type = WindowManager.LayoutParams.TYPE_TOAST;
				params.setTitle("Toast");
				wm.addView(view, params);
				break;

			}

		}

	}

	/**
	 * 根据联系人的电话号码查询 他的名字 
	 * @param incomingNumber
	 */
	public String queryNameByNumber(String incomingNumber) {
		String name = incomingNumber;
		String base = "content://com.android.contacts/data/phones/filter/";
		Uri uri = Uri.parse(base+incomingNumber);
		Cursor cursor = getContentResolver().query(uri, new String[]{"display_name"}, null, null, null);
		if(cursor.moveToFirst()){
			name = cursor.getString(0);
		}
		cursor.close();
		return name;
	}

	public void deleteLog(String incomingNumber) {
		//1.查询出来 id
		Cursor cursor = getContentResolver().query(CallLog.Calls.CONTENT_URI, new String[]{"_id"}, "number=?", new String[]{incomingNumber}, null);
		if(cursor.moveToFirst()){
			String id = cursor.getString(0);
			cursor.close();
			getContentResolver().delete(CallLog.Calls.CONTENT_URI, "_id="+id, null);
		}
	}

	//挂断电话的操作
	public void endCall() {
		try {
			Method method = Class.forName("android.os.ServiceManager").getMethod("getService", String.class);
			IBinder binder = (IBinder)method.invoke(null, new Object[]{TELEPHONY_SERVICE});
			ITelephony telephony = ITelephony.Stub.asInterface(binder);
			telephony.endCall();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		//当服务被停止的时候 取消telephony的监听 
		manager.listen(listener, PhoneStateListener.LISTEN_NONE);
	}

	private class MyObserver extends ContentObserver{
		private String incomingNumber;
		public MyObserver(Handler handler,String incomingNumber) {
			super(handler);
			this.incomingNumber = incomingNumber;
		}

		@Override
		public void onChange(boolean selfChange) {
			super.onChange(selfChange);
			deleteLog(incomingNumber);
			getContentResolver().unregisterContentObserver(this);
		}
		
	}
}

package cn.itcast.mobilesafe.ui;

import cn.itcast.lockservice.IService;
import cn.itcast.mobilesafe.R;
import cn.itcast.mobilesafe.service.QueryAddressService;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;

public class SettingActivity extends Activity {
	SharedPreferences sp;
	IService iService;
	WatchDogConn conn;
	Intent serviceintent;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.setting);
		sp = getSharedPreferences("config", Context.MODE_PRIVATE);
	}

	public void closeUpdate(View view) {
		Editor editor = sp.edit();
		editor.putBoolean("shouldupdate", false);
		editor.commit();
	}

	public void openUpdate(View view) {
		Editor editor = sp.edit();
		editor.putBoolean("shouldupdate", true);
		editor.commit();
	}

	public void openQueryAddress(View view) {
		Intent intent = new Intent(this, QueryAddressService.class);
		startService(intent);
	}

	public void stopQueryAddress(View view) {
		Intent intent = new Intent(this, QueryAddressService.class);
		stopService(intent);
	}

	public void changeLocation(View view) {
		Intent intent = new Intent(this, DragViewActivity.class);
		startActivity(intent);
	}

	public void startAppLock(View view) {
		serviceintent = new Intent();
		serviceintent.setAction("cn.itcast.remotelock");
		startService(serviceintent);
		// 开启服务后 就完成绑定
		conn = new WatchDogConn();
		bindService(serviceintent, conn, BIND_AUTO_CREATE);
	}

	public void stopAppLock(View view) {

		try {

			iService.callstopprotect();
			unbindService(conn);
//			unbindService(conn);
//			Intent intent = new Intent();
//			intent.setAction("cn.itcast.remotelock");
//			stopService(intent);
			stopService(serviceintent);
		} catch (Exception e) {
			// TODO: handle exception
			//服务只能被接触绑定一次
		}
	}

	private class WatchDogConn implements ServiceConnection {

		public void onServiceConnected(ComponentName name, IBinder service) {
			iService = IService.Stub.asInterface(service);

		}

		public void onServiceDisconnected(ComponentName name) {
			// TODO Auto-generated method stub

		}

	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		try{
			unbindService(conn);
		}catch (Exception e) {
			// TODO: handle exception
		}
		
	}
	
}

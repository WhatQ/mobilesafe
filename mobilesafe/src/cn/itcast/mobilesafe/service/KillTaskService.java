package cn.itcast.mobilesafe.service;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.text.format.Time;
import cn.itcast.mobilesafe.util.Logger;

public class KillTaskService extends Service {
	protected static final String TAG = "KillTaskService";
	Timer timer ;
	TimerTask task;
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		//������ʱ�� ���ڵ��������� 
		timer = new Timer();
		task = new TimerTask() {
			
			@Override
			public void run() {
				//1.��ȡ������Ҫ����ĳ�����б� 
				//2. am.restartPackage();
				Logger.i(TAG,"������������");
			}
		};
		timer.schedule(task, 1000, 10000);
	
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		timer.cancel();
		timer=null;
		task =null;
	}
	
	

}

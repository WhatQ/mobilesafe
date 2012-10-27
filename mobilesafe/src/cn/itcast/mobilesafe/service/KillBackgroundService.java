package cn.itcast.mobilesafe.service;

import java.util.List;

import android.app.ActivityManager;
import android.app.Service;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;

public class KillBackgroundService extends Service {

	@Override
	public IBinder onBind(Intent intent) {
		
		return null;
	}

	@Override
	public void onStart(Intent intent, int startId) {
		super.onStart(intent, startId);
		ActivityManager am = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningAppProcessInfo> runingappProcessInfos = am
				.getRunningAppProcesses();
		for(RunningAppProcessInfo info : runingappProcessInfos){
			try{
			am.restartPackage(info.processName);
			}catch (Exception e) {
				// TODO: handle exception
			}
		}
		
	}

}

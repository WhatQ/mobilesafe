package cn.itcast.mobilesafe.service;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import cn.itcast.mobilesafe.R;

import android.app.ActivityManager;
import android.app.PendingIntent;
import android.app.Service;
import android.app.ActivityManager.MemoryInfo;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.widget.RemoteViews;

public class UpdateWidgetService extends Service {

	Timer timer;
	TimerTask task;
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		//���ڸ���widget��������� 
		timer = new Timer();
		task = new TimerTask() {
			
			@Override
			public void run() {
				ActivityManager am = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
				List<RunningAppProcessInfo> runingappProcessInfos = am
						.getRunningAppProcesses();
			
				MemoryInfo outInfo = new ActivityManager.MemoryInfo();
				am.getMemoryInfo(outInfo);
				float availMem = outInfo.availMem / 1024f / 1024f;
				// ��ʽ������ı��ַ���
				DecimalFormat format = new DecimalFormat("###.00");
				String availMemStr = format.format(availMem);
				
				// ��Ϊwidget������������һ���������� ������� 
				// ͨ�� remoteview������Զ�̵Ľ��� 
				
				RemoteViews rv = new RemoteViews(getPackageName(), R.layout.process_widget);
				rv.setTextViewText(R.id.process_count, "��ǰ������ĿΪ"+runingappProcessInfos.size());
				rv.setTextViewText(R.id.process_memory, "ʣ������ڴ�Ϊ"+availMemStr+"MB");
				
				Intent intent = new Intent(UpdateWidgetService.this,KillBackgroundService.class);
				
				PendingIntent pendingIntent = PendingIntent.getService(UpdateWidgetService.this, 0, intent, 0);
				rv.setOnClickPendingIntent(R.id.btn_clear, pendingIntent);
				
				ComponentName provider = new ComponentName("cn.itcast.mobilesafe", "cn.itcast.mobilesafe.widget.MobileSafeWidget");
				AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(UpdateWidgetService.this);
				appWidgetManager.updateAppWidget(provider, rv);
				
				
			}
		};
		timer.schedule(task, 1000, 5000);
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		timer.cancel();
		timer =null;
		task =null;
	}

}

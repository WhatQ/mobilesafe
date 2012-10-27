package cn.itcast.mobilesafe.widget;

import java.text.DecimalFormat;
import java.util.List;

import cn.itcast.mobilesafe.R;
import cn.itcast.mobilesafe.service.UpdateWidgetService;

import android.app.ActivityManager;
import android.app.ActivityManager.MemoryInfo;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.sax.StartElementListener;
import android.widget.RemoteViews;

public class MobileSafeWidget extends AppWidgetProvider {
	ActivityManager am;
	
	
	
	@Override
	public void onEnabled(Context context) {
		
		super.onEnabled(context);
		Intent service = new Intent(context,UpdateWidgetService.class);
		context.startService(service);
	}

	@Override
	public void onDisabled(Context context) {
		Intent service = new Intent(context,UpdateWidgetService.class);
		context.stopService(service);
		super.onDisabled(context);
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		super.onReceive(context, intent);
	}

	// appWidgetManager 系统widget管理的服务
	// int[] appWidgetIds widgetid对应的数组 
	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager,
			int[] appWidgetIds) {

		super.onUpdate(context, appWidgetManager, appWidgetIds);
		am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningAppProcessInfo> runingappProcessInfos = am
				.getRunningAppProcesses();
	
		MemoryInfo outInfo = new ActivityManager.MemoryInfo();
		am.getMemoryInfo(outInfo);
		float availMem = outInfo.availMem / 1024f / 1024f;
		// 格式化这个文本字符串
		DecimalFormat format = new DecimalFormat("###.00");
		String availMemStr = format.format(availMem);
		
		// 因为widget是运行在另外一个进程里面 桌面进程 
		// 通过 remoteview来更新远程的界面 
		
		RemoteViews rv = new RemoteViews(context.getPackageName(), R.layout.process_widget);
		rv.setTextViewText(R.id.process_count, "当前进程数目为"+runingappProcessInfos.size());
		rv.setTextViewText(R.id.process_memory, "剩余可用内存为"+availMemStr+"MB");
		ComponentName provider = new ComponentName("cn.itcast.mobilesafe", "cn.itcast.mobilesafe.widget.MobileSafeWidget");
		appWidgetManager.updateAppWidget(provider, rv);
	}

}
